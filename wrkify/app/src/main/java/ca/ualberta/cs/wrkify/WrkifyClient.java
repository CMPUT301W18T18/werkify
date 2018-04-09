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

/**
 * WrkifyClient provides a singleton of RemoteClient
 * with the default app settings.
 *
 * @see RemoteClient
 */

public class WrkifyClient {
    /**
     * url is the default server that wrkify uses
     */
    public static final String URL = "http://cmput301.softwareprocess.es:8080";
    /**
     * INDEX the default elasticsearch index that wrkify uses
     */
    public static final String INDEX = "cmput301w18t18";

    private static CachingClient instance;

    /**
     * gets the one instance of RemoteClient
     * used throughout the wrkify app
     * @return the one true instance
     */
    public static CachingClient getInstance() {
        if (instance == null) {
            instance = new CachingClient<>(new ElasticClient(URL, INDEX));
        }
        return instance;
    }

    /**
     * Overrides the global instance.
     * @param instance new RemoteClient to use as the global instance
     */
    public static void setInstance(CachingClient instance) {
        WrkifyClient.instance = instance;
    }

    /**
     * private to prevent instatiation
     */
    private WrkifyClient() {}
}
