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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;

/**
 * Created by peter on 17/03/18.
 */

public class RemoteClient {

    private JestDroidClient client;
    private String index;

    public RemoteClient(String url, String index) {
        DroidClientConfig.Builder builder = new DroidClientConfig.Builder(url);
        DroidClientConfig config = builder.build();

        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);
        this.client = (JestDroidClient) factory.getObject();

        this.index = index;
    }

    protected RemoteClient() {}

    public <T extends RemoteObject> T create(Class<T> type, Object ...conArgs) {
        T instance;
        try {
            instance = newInstance(type, conArgs);
        } catch (Exception e) {
            return null;
        }

        Index index = new Index.Builder(instance).index(this.index).type(type.getName()).build();

        try {
            DocumentResult result = this.client.execute(index);
            instance.setId(result.getId());
        } catch (IOException e) {
            //TODO buffer and return pseudo id
            instance.setId(null);
        }

        instance.setClient(this);
        return instance;
    }

    public void upload(RemoteObject obj) {
        Index index = new Index.Builder(obj)
                .index(this.index).type(obj.getClass().getName())
                .id(obj.getId()).build();
        try {
            DocumentResult result = this.client.execute(index);
        } catch (IOException e) {
            //TODO buffer update
        }
    }

    public <T extends RemoteObject> T download(String id, Class<T> type) throws IOException {
        Get get = new Get.Builder(this.index, id).build();
        DocumentResult result = this.client.execute(get);
        return result.getSourceAsObject(type);
    }

    protected static <T> T newInstance(Class<T> type, Object ...conArgs)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

        Class[] classes = new Class[conArgs.length];
        for (int i = 0; i < conArgs.length; ++i) {
            classes[i] = conArgs[i].getClass();
        }
        Constructor<T> con = type.getConstructor(classes);
        return con.newInstance(conArgs);
    }
}
