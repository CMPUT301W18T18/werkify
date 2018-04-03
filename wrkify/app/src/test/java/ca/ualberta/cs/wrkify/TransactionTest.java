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

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by peter on 28/03/18.
 */

public class TransactionTest {
    private static RemoteClient rc;

    @BeforeClass
    public static void setup() {
        rc= new MockRemoteClient();
    }

    @Test
    public void testTransaction() {
        Method meth;
        try {
            meth = SimpleRemoteObject.class.getMethod("setFieldTo1");
        } catch (NoSuchMethodException e) {
            fail();
            return;
        }
        SimpleRemoteObject sro = new SimpleRemoteObject(5);
        TransactionBak<SimpleRemoteObject> t = new TransactionBak<SimpleRemoteObject>(sro, meth, new Object[0]);

        assertEquals(5, t.getObject().field);
        t.apply(sro);
        assertEquals(1, sro.field);
    }

    @Test
    public void testProxy() {
        SimpleRemoteObject inner = new SimpleRemoteObject(8);
        TransactionManager tm = new TransactionManager();

        SimpleObject obj = (SimpleObject) Proxy.newProxyInstance(
                SimpleRemoteObject.class.getClassLoader(),
                new Class[] {SimpleObject.class},
                new TransactionProxyHandler<SimpleRemoteObject>(tm, inner)
        );

        obj.setFieldTo1();
        obj.setFieldTo2();
        obj.setFieldTo3();

        assertEquals(3, obj.field);

        tm.pop().apply(obj);
        assertEquals(1, obj.field);
        tm.pop().apply(obj);
        assertEquals(2, obj.field);
    }
}