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

/**
 * Tests for RemoteReference.
 */
public class RemoteReferenceTest {
    private static final MockRemoteClient remoteClient = new MockRemoteClient();

    @Test
    public void testRemoteReference() {
        ObjectWithRemoteReference local = remoteClient.create(ObjectWithRemoteReference.class, 123);
        RemoteReferredToObject remote = remoteClient.create(RemoteReferredToObject.class, 456);

        try {
            local.setRemoteRRTO(remote);
            assertEquals(remote, local.getRemoteRRTO());

            ObjectWithRemoteReference local1 = remoteClient.download(local.getId(), local.getClass());
            assertEquals(local, local1);
            assertEquals(local.getRemoteRRTO(), local1.getRemoteRRTO());
    
            RemoteReferredToObject remote1 = remoteClient.download(remote.getId(), remote.getClass());
            assertEquals(remote, remote1);
        
            RemoteReferredToObject newRemote = remoteClient.create(RemoteReferredToObject.class, 789);

            local.setRemoteRRTO(newRemote);
            assertEquals(newRemote, local.getRemoteRRTO());
        } catch (IOException e) {
            fail();
        }
    }
    
    /**
     * Simple class to use as a mock referent.
     */
    static class RemoteReferredToObject extends RemoteObject {
        private Integer field;
        
        public RemoteReferredToObject(Integer field) {
            this.field = field;
        }
    }
    
    /**
     * Simple mock class that has a RemoteReference.
     */
    static class ObjectWithRemoteReference extends RemoteObject {
        private Integer field;
        
        private RemoteReference<RemoteReferredToObject> rrto;
        
        public ObjectWithRemoteReference(Integer field) {
            this.field = field;
        }
        
        public RemoteReferredToObject getRemoteRRTO() throws IOException {
            return this.rrto.getRemote(remoteClient);
        }
        
        public void setRemoteRRTO(RemoteReferredToObject rrto) {
            this.rrto = rrto.reference();
        }
    }
}
