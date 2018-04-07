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


import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.*;

public class CachingClientTest {
    @Test
    public void testCachingClient() {
        MockRemoteClient remoteClient = new MockRemoteClient();
        CachingClient<MockRemoteClient> cachingClient = new CachingClient<>(remoteClient);

        SimpleRemoteObject object = remoteClient.create(SimpleRemoteObject.class, 12);
        try {
            assertEquals(object, cachingClient.download(object.getId(), SimpleRemoteObject.class));
        } catch (IOException e) {
            fail("IO Exception");
        }

        // Replace the remote object on the 'remote server'.
        // The cache should not notice this change.
        SimpleRemoteObject newObject = new SimpleRemoteObject(object.getId(), 11);
        remoteClient.upload(newObject);

        try {
            // 'Remote server' has the new object
            assertEquals(newObject, remoteClient.download(object.getId(), SimpleRemoteObject.class));
            // Cache should still contain the old object
            assertEquals(object, cachingClient.download(object.getId(), SimpleRemoteObject.class));
        } catch (IOException e) {
            fail("IO Exception");
        }

        // Discard the cached copy.
        // The cache should now re-download the object
        cachingClient.discardCached(object.getId());

        try {
            // Both clients should now have the new object
            assertEquals(newObject, remoteClient.download(object.getId(), SimpleRemoteObject.class));
            assertEquals(newObject, cachingClient.download(object.getId(), SimpleRemoteObject.class));
        } catch (IOException e) {
            fail("IO Exception");
        }

        // Deletes should immediately fall through to the real client.
        cachingClient.delete(newObject);

        try {
            assertNull(remoteClient.download(newObject.getId(), SimpleRemoteObject.class));
            assertNull(cachingClient.download(newObject.getId(), SimpleRemoteObject.class));
        } catch (IOException e) {
            fail("IO Exception");
        }

        // Uploads should also immediately fall through.
        cachingClient.upload(newObject);

        try {
            assertEquals(newObject, remoteClient.download(newObject.getId(), SimpleRemoteObject.class));
            assertEquals(newObject, cachingClient.download(newObject.getId(), SimpleRemoteObject.class));
        } catch (IOException e) {
            fail("IO Exception");
        }
    }
}
