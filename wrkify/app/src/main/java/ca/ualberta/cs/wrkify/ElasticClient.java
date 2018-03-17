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

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.action.Action;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * ElasticClient provides a singleton for a JestDroidClient, with
 * buffering offline behavior
 *
 * @see JestDroidClient
 */

public class ElasticClient {

    private static ElasticClient instance;
    // TODO move defurl to config file
    private static String defurl = "http://cmput301.softwareprocess.es:8080";
    // TODO use proper index
    private static String INDEX = "CMPUT301W18T18";

    private JestDroidClient client;

    /**
     * handles singleton behavior of ElasticClient
     *
     * @return the one true instance of ElasticClient
     */
    public static ElasticClient getInstance() {
        //TODO maybe have a map of instances by url?
        if (instance == null) {
            instance = new ElasticClient(defurl);
        }
        return instance;
    }

    /**
     * instantiates an Elastic client by creating a jest client
     * protected to avoid uncontrolled instantiation.
     *
     * @param url the url of the elasticsearch server
     */
    protected ElasticClient(String url) {
        // from elasticsearch lab (2018-02-27)

        DroidClientConfig.Builder builder = new DroidClientConfig.Builder(url);
        DroidClientConfig config = builder.build();

        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);
        this.client = (JestDroidClient) factory.getObject();
    }

    protected ElasticClient() {}

    /**
     * a wrapper for jest's JestDroidClient.execute
     *
     * @param action the jest action to execute
     * @return a JestResult
     */
    public <T extends JestResult> T execute(Action<T> action) throws IOException {
        return this.client.execute(action);
    }

    /**
     * creates a new document
     *
     * @param obj the object you are uploading
     * @param type the type of the object
     * @return the elasticsearch id (null if offline)
     */
    public <T> String create( T obj, Class<T> type) {
        Index index = new Index.Builder(obj).index(INDEX).type(type.getName()).build();
        try {
            DocumentResult result = this.execute(index);
            return result.getId();
        } catch (IOException e) {
            // TODO buffer creation
            return null;
        }
    }

    /**
     * updates an existing document
     *
     * @param id the elasticsearch id
     * @param obj the object to push
     * @param type the type of obj
     */
    public <T> void update(String id, T obj, Class<T> type) {
        Index index = new Index.Builder(obj)
                .index(INDEX).type(type.getName())
                .id(id).build();
        try {
            DocumentResult result = this.execute(index);
        } catch (IOException e) {
            //TODO buffer update
        }
    }

    /**
     * gets the object by it's id.
     * this function will not work offline
     *
     * @param id the elasticsearch ID
     * @param type the type to be returned
     * @param <T>
     * @return the object related to that id
     * @throws IOException when execute fails
     */
    public <T> T get(String id, Class<T> type) throws IOException {
        Get get = new Get.Builder(INDEX, id).build();
        DocumentResult result = this.execute(get);
        return result.getSourceAsObject(type);
    }

    /**
     * deletes a given object by id.
     *
     * @param id the id of the object
     * @throws IOException when execute fails
     */
    public void delete(String id) throws IOException {
        Delete del = new Delete.Builder(id).build();
        this.execute(del);
    }

    /**
     * searches for a list of elastic objects in given type.
     *
     * this ones a doozy.
     *
     * @param query the elasticsearch query
     * @param elasticType the type of ElasticObject we want
     * @param type the base type used in elasticsearch
     * @return a List of the hits
     * @throws IOException when execute() fails
     */
    public <T, E extends ElasticObject<T>> List<E> search(
            String query, Class<E> elasticType, Class<T> type) throws IOException {

        Search search = new Search.Builder(query).addIndex(INDEX)
                .addType(type.getName()).build();

        SearchResult result = this.client.execute(search);
        List<SearchResult.Hit<T, Void>> hits = result.getHits(type);

        // we've just done the search, now its time to convert
        // our results into their elastic forms

        ArrayList<E> elasticResults = new ArrayList<E>();
        Constructor con;
        try {
            con = elasticType.getConstructor(
                    String.class, Class.class, ElasticClient.class);
        } catch (NoSuchMethodException e) {
            // idk if this can happen
            e.printStackTrace();
            return elasticResults;
        }

        // go through each hit mapping id's and objects
        // to elasticObjects
        for (SearchResult.Hit<T, Void> task : hits) {
            //TODO change elasticObject to support object preloading
            try {
                E elast = (E) con.newInstance(task.id, type, this);
                elasticResults.add(elast);
            } catch (Exception e) {
                // don't add to the list if it fails
                // this will either happen every time or never
                e.printStackTrace();
            }
        }
        return elasticResults;
    }
}
