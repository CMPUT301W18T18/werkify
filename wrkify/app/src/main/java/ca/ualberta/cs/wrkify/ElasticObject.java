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

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

/**
 * ElasticObject provides a heritable class for
 * ElasticSearch aware implementations of Objects
 *
 * @see ElasticClient
 * @see ElasticTask
 * @see ElasticUser
 */

public class ElasticObject {

    private ElasticClient client;
    private String elasticId;
    private Object obj;

    /**
     * creates an ElasticObject from an elasticsearch id
     * and the ElasticClient Singleton
     * @param id the elasticsearch id
     */
    public ElasticObject(String id) {
        this.client = ElasticClient.getInstance();
        this.elasticId = id;
    }

    /**
     * creates an ElasticObject from an elasticsearch id
     * and a provided ElasticClient
     * @param id the elasticsearch id
     * @param client the ElasticClient
     */
    public ElasticObject(String id, ElasticClient client) {
        this.client = client;
        this.elasticId = id;
    }

    /**
     * creates an elasticObject from a new object to
     * be uploaded to eslasticsearch.
     * and the ElasticClient Singleton
     * @param obj the object to upload to elasticsearch
     */
    public ElasticObject(Object obj) throws Exception {
        this.client = ElasticClient.getInstance();

        //TODO support proper index
        Index index = new Index.Builder(obj).index("testing").type(obj.getClass().getName()).build();
        DocumentResult result = (DocumentResult) this.client.execute(index);

        if (!result.isSucceeded()) {
            //TODO choose a better exception
            throw new Exception();
        }

        this.elasticId = result.getId();
    }

    /**
     * creates an elasticObject from a new object to
     * be uploaded to eslasticsearch.
     * and an ElasticClient
     * @param obj the object to upload to elasticsearch
     * @param client the ElasticClient
     */
    public ElasticObject(Object obj, ElasticClient client) throws Exception {
        this.client = client;

        //TODO support proper index
        Index index = new Index.Builder(obj).index("testing").type(obj.getClass().getName()).build();
        DocumentResult result = (DocumentResult) this.client.execute(index);

        if (!result.isSucceeded()) {
            //TODO choose a better exception
            throw new Exception();
        }

        this.elasticId = result.getId();
    }
}
