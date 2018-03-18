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
    @Test
    public void testRemoteReference() {
        MockRemoteClient remoteClient = new MockRemoteClient();
        
        ObjectWithRemoteReference local = remoteClient.create(ObjectWithRemoteReference.class, 123);
        RemoteReferredToObject remote = remoteClient.create(RemoteReferredToObject.class, 456);
        
        local.setRRTO(remote);
        assertEquals(remote, local.getRRTO());
        
        try {
            ObjectWithRemoteReference local1 = remoteClient.download(local.getId(), local.getClass());
            assertEquals(local, local1);
            assertEquals(local.getRRTO(), local1.getRRTO());
    
            RemoteReferredToObject remote1 = remoteClient.download(remote.getId(), remote.getClass());
            assertEquals(remote, remote1);
        } catch (IOException e) {
            fail();
        }
        
        RemoteReferredToObject newRemote = remoteClient.create(RemoteReferredToObject.class, 789);
        
        local.setRRTO(newRemote);
        assertEquals(newRemote, local.getRRTO());
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
        
        public RemoteReferredToObject getRRTO() {
            return this.rrto.getRemote();
        }
        
        public void setRRTO(RemoteReferredToObject rrto) {
            this.rrto = rrto.reference();
        }
    }
}
