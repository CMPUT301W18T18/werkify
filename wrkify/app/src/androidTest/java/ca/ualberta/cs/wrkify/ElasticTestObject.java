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
 * ElasticTestObject wraps ConcreteTestObject and is used for
 * testing ElasticClient
 *
 * @see ConcreteTestObject
 * @see ElasticClientTest
 */
public class ElasticTestObject extends ElasticObject<ConcreteTestObject> {
    public ElasticTestObject(String id) {
        super(id, ConcreteTestObject.class);
    }

    public ElasticTestObject(String id, ElasticClient ec) {
        super(id, ConcreteTestObject.class, ec);
    }

    public ElasticTestObject(ConcreteTestObject obj) {
        super(obj, ConcreteTestObject.class);
    }

    public ElasticTestObject(ConcreteTestObject obj, ElasticClient ec) {
        super(obj, ConcreteTestObject.class, ec);
    }
}