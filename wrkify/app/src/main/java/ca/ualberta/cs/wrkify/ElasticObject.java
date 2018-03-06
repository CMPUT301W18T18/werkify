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
 * Created by peter on 06/03/18.
 */

public class ElasticObject {

    private ElasticClient client;
    private String elasticId;
    private Object obj;

    public ElasticObject(String id) {
        this.client = ElasticClient.getInstance();
        this.elasticId = id;
    }

    public ElasticObject(String id, ElasticClient client) {
        this.client = client;
        this.elasticId = id;
    }

    public ElasticObject(Object obj) throws Exception {
        this.client = ElasticClient.getInstance();

        //TODO support proper index
        Index index = new Index.Builder(obj).index("testing").type(obj.getClass().getName()).build();
        DocumentResult result = this.client.execute(index);

        if (!result.isSucceeded()) {
            //TODO choose a better exception
            throw new Exception();
        }

        this.elasticId = result.getId();
    }

    public ElasticObject(Object obj, ElasticClient client) throws Exception {
        this.client = client;

        //TODO support proper index
        Index index = new Index.Builder(obj).index("testing").type(obj.getClass().getName()).build();
        DocumentResult result = this.client.execute(index);

        if (!result.isSucceeded()) {
            //TODO choose a better exception
            throw new Exception();
        }

        this.elasticId = result.getId();
    }
}
