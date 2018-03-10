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
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by peter on 09/03/18.
 */

public class ElasticObjectTest {

}

/**
 * MockElasticClient is a fake ElasticClient
 * that uses HashMap internaly.
 *
 * note: the execute function is not supported
 *
 * @author Peter Elliott
 * @see ElasticClient
 */
class MockElasticClient extends ElasticClient {
    private HashMap<String, Object> hmap;

    public MockElasticClient() {
        super("http://example.com");
    }

    public <T> String create(T obj, Class<T> type) {
        String id = UUID.randomUUID().toString();

        hmap.put(id, obj);

        return id;
    }

    public <T> void update(String id, T obj, Class<T> type) {
        hmap.put(id, obj);
    }

    public <T> T get(String id, Class<T> type) {
        return type.cast(hmap.get(id));
    }
}

class SimpleObject {
    public String str;
}
