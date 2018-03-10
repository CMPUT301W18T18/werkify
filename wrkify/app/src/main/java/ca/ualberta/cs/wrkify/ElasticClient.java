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

import io.searchbox.action.Action;
import io.searchbox.client.JestResult;

/**
 * ElasticClient provides a singleton for a JestDroidClient, with
 * buffering offline behavior
 */

public class ElasticClient {

    private static ElasticClient instance;
    // TODO move defurl to config file
    private static String defurl = "http://cmput301.softwareprocess.es:8080";

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
     * private to avoid uncontrolled instantiation.
     *
     * @param url the url of the elasticsearch server
     */
    private ElasticClient(String url) {
        // from elasticsearch lab (2018-02-27)

        DroidClientConfig.Builder builder = new DroidClientConfig.Builder(url);
        DroidClientConfig config = builder.build();

        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);
        this.client = (JestDroidClient) factory.getObject();
    }

    /**
     * a wrapper for jest's JestDroidClient.execute,
     * in order to implement offline behavior.
     *
     * @param action the jest action to execute
     * @return a JestResult
     */
    public <T extends JestResult> T execute(Action<T> action) {
        T res;
        try {
            res = this.client.execute(action);
        } catch (IOException e) {
            // TODO: offline behavior
            res = null;
        }
        return res;
    }
}
