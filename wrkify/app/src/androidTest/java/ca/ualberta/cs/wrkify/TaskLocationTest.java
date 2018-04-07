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

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import java.io.IOException;

import static android.test.MoreAsserts.assertNotEmpty;
import static junit.framework.Assert.fail;

public class TaskLocationTest {
    @Test
    public void testTaskLocation() {
        try {
            User user = (User) WrkifyClient.getInstance().create(User.class, "User", "a@a", "");
            Task task = (Task) WrkifyClient.getInstance().create(Task.class, "LOCATION4", user, "");

            task.setLocation(new TaskLocation(14.555, 29.192));
            WrkifyClient.getInstance().upload(task);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSearchForTask() {
        try {
            assertNotEmpty(WrkifyClient.getInstance().getSearcher().findTasksByKeywordsNear("LOCATION4",
                    new TaskLocation(14.555, 29.192)));
        } catch (IOException e) {
            fail("IO Exception");
        }
    }
}
