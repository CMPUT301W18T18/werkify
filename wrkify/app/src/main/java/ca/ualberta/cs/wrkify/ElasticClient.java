/*
 * Copyright 2018 CMPUT301W18T18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import android.util.Log;

import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * ElasticClient exposes key elasticsearch functionality through
 * the RemoteObject.
 *
 * @see RemoteClient
 * @see RemoteObject
 */
public class ElasticClient extends RemoteClient {
    private JestDroidClient client;
    private String index;

    private Searcher<ElasticClient> searcher = new ElasticSearcher(this);

    /**
     * creates an ElasticClient based on a url and index
     * @param url the url of the elasticsearch instance
     * @param index the index we are operating on
     */
    public ElasticClient(String url, String index) {
        DroidClientConfig.Builder builder = new DroidClientConfig.Builder(url);
        DroidClientConfig config = builder.build();

        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);
        this.client = (JestDroidClient) factory.getObject();

        this.index = index;
    }

    @Override
    Searcher<ElasticClient> getSearcher() {
        return searcher;
    }

    /**
     * create a new RemoteObject from it's class and it's constructors,
     * then push it to elasticsearch.
     * @param type the type of your instance
     * @param conArgs the arguments to your constructor
     * @return the new instance
     */
    @Override
    public <T extends RemoteObject> T create(Class<T> type, Object... conArgs) {
        Log.i("elastic", "CREATE " + type.toString() + " :: " + Arrays.toString(conArgs));

        T instance;
        try {
            instance = newInstance(type, conArgs);
        } catch (Exception e) {
            return null;
        }

        return this.uploadNew(type, instance);
    }

    @Override
    public <T extends RemoteObject> T uploadNew(Class<T> type, T instance) {
        Log.i("elastic", "NEW " + type);
        Index index = new Index.Builder(instance).index(this.index).type(type.getName()).build();

        String originalId = instance.getId();
        try {
            instance.setId(null);
            DocumentResult result = this.client.execute(index);
            instance.setId(result.getId());
        } catch (IOException e) {
            instance.setId(originalId);
            e.printStackTrace();
            return null;
        }

        return instance;
    }

    /**
     * reuploads changes of an existing RemoteObject
     * to this RemoteClient
     * @param obj the object to upload
     */
    @Override
    public void upload(RemoteObject obj) {
        Log.i("elastic", "UPLOAD " + obj.getClass().toString() + ":" + obj.getId());

        Index index = new Index.Builder(obj)
                .index(this.index).type(obj.getClass().getName())
                .id(obj.getId()).build();
        try {
            DocumentResult result = this.client.execute(index);
        } catch (IOException e) {
            //TODO buffer update
        }
    }

    /**
     * deletes the remote object
     * @param obj the remote object
     */
    @Override
    public void delete(RemoteObject obj) {
        Log.i("elastic", "DELETE :" + obj.getId());

        Delete del = new Delete.Builder(obj.getId()).index(this.index)
                .type(obj.getClass().getName()).build();

        try {
            this.client.execute(del);
        } catch (IOException e) {
            //TODO buffer deletion
        }
    }

    /**
     * downloads an object from elasticsearch given type and id
     *
     * @param id the elasticsearch id
     * @param type the type of the object
     * @return the RemoteObject to return
     * @throws IOException when executing fails
     */
    @Override
    public <T extends RemoteObject> T download(String id, Class<T> type) throws IOException {
        Log.i("elastic", "DOWNLOAD " + type.toString() + ":" + id);

        Get get = new Get.Builder(this.index, id).build();
        DocumentResult result = this.client.execute(get);
        T obj = result.getSourceAsObject(type);

        if (obj != null) {
            obj.setId(result.getId());
        }

        return obj;
    }

    /**
     * take an elasticsearch json query, and returns a List of remote
     * objects according to the search.
     *
     * @param query the json elasticsearch query
     * @param type a remote object you are searching for
     * @param <T> the type of remote object you are searching for
     * @return a list<T>
     * @throws IOException according to execute
     */
    public <T extends RemoteObject> List<T> executeQuery(String query, Class<T> type) throws IOException {
        Log.i("elastic", "SEARCH " + type);

        Search search = new Search.Builder(query).addIndex(this.index)
                .addType(type.getName()).build();

        SearchResult result = this.client.execute(search);
        List<SearchResult.Hit<T, Void>> hits = result.getHits(type);

        ArrayList<T> remotehits = new ArrayList<T>();
        for (SearchResult.Hit<T, Void> hit : hits) {
            T obj = hit.source;
            obj.setId(hit.id);
            remotehits.add(obj);
        }

        return remotehits;
    }
}
