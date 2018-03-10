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

import java.io.IOException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

/**
 * ElasticObject provides a heritable class for
 * ElasticSearch aware implementations of Objects
 *
 * @see ElasticClient
 * @see ElasticTask
 * @see ElasticUser
 */

public class ElasticObject<T> {

    private ElasticClient client;
    private String elasticId;
    private Class<T> type;
    private T obj;

    /**
     * creates an ElasticObject from an elasticsearch id
     * and the ElasticClient Singleton
     * @param id the elasticsearch id
     * @param type the type of our object
     */
    public ElasticObject(String id, Class<T> type) {
        this.client = ElasticClient.getInstance();
        this.elasticId = id;
        this.type = type;
    }

    /**
     * creates an ElasticObject from an elasticsearch id
     * and a provided ElasticClient
     * @param id the elasticsearch id
     * @param type the type of our object
     * @param client the ElasticClient
     */
    public ElasticObject(String id, Class<T> type, ElasticClient client) {
        this.client = client;
        this.elasticId = id;
        this.type = type;
    }

    /**
     * creates an elasticObject from a new object to
     * be uploaded to eslasticsearch.
     * and the ElasticClient Singleton
     * @param obj the object to upload to elasticsearch
     * @param type the type of our object
     */
    public ElasticObject(T obj, Class<T> type) throws Exception {
        this.client = ElasticClient.getInstance();
        this.obj = obj;
        this.type = type;

        this.elasticId = this.client.create(obj, type);
    }

    /**
     * creates an elasticObject from a new object to
     * be uploaded to eslasticsearch.
     * and an ElasticClient
     * @param obj the object to upload to elasticsearch
     * @param client the ElasticClient
     * @param type the type of our object
     */
    public ElasticObject(T obj, Class<T> type, ElasticClient client) throws Exception {
        this.client = client;
        this.obj = obj;
        this.type = type;

        this.elasticId = this.client.create(obj, type);
    }

    /**
     * updates the object to elasticsearch.
     */
    public void update() {
       this.client.update(this.elasticId, this.obj, this.type);
    }

    /**
     * lazily gets the internal object
     *
     * @throws IOException when you try to load the object with
     *                     no internet
     * @return the T that we are tracking
     */
    public T getObj() throws IOException {
        if (this.obj == null) {
            this.client.get(this.elasticId, this.type);
        }
        return this.obj;
    }

    /**
     * causes the next getObj to be reloaded from elasticsearch.
     */
    public void refresh() {
        this.obj = null;
    }
}
