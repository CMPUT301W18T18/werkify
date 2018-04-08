/* Copyright 2018 CMPUT301W18T18
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
 */
package ca.ualberta.cs.wrkify;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for CheckList and CheckListItem.
 *
 * @see CheckList
 * @see CheckList.CheckListItem
 */
public class CheckListTest {
    @Test
    public void testGetItem() {
        ArrayList<String> items = new ArrayList<String>();

        items.add("zero");
        items.add("one");
        items.add("two");
        items.add("three");

        CheckList check = new CheckList(items);

        assertEquals("zero", check.getItem(0).getDescription());
        assertEquals("one", check.getItem(1).getDescription());
        assertEquals("two", check.getItem(2).getDescription());
        assertEquals("three", check.getItem(3).getDescription());
    }

    @Test
    public void testSetDescription() {
        CheckList checkList = new CheckList();

        checkList.addItem("initial");
        checkList.getItem(0).setDescription("edited");

        assertEquals("edited", checkList.getItem(0).getDescription());
    }

    @Test
    public void testListFunctionality() {
        CheckList checkList = new CheckList();

        assertEquals(0, checkList.itemCount());

        checkList.addItem("item");
        assertEquals(1, checkList.itemCount());

        checkList.addItem("item2");
        assertEquals(2, checkList.itemCount());

        checkList.removeItem(checkList.getItem(1));
        assertEquals(1, checkList.itemCount());

        checkList.addItem("item3", true);

        List<CheckList.CheckListItem> items = checkList.getItems();
        assertEquals(2, items.size());
        assertEquals("item", items.get(0).getDescription());
        assertEquals(false, items.get(0).getStatus());
        assertEquals("item3", items.get(1).getDescription());
        assertEquals(true, items.get(1).getStatus());
    }

    @Test
    public void testSetGetStatus() {
        ArrayList<String> items = new ArrayList<String>();

        items.add("zero");
        items.add("one");
        items.add("two");
        items.add("three");

        CheckList check = new CheckList(items);

        check.getItem(0).setStatus(true);
        check.getItem(1).setStatus(false);
        check.getItem(2).setStatus(true);
        check.getItem(3).setStatus(false);
        check.getItem(3).setStatus(false);
        check.getItem(3).setStatus(true);

        assertTrue(check.getItem(0).getStatus());
        assertFalse(check.getItem(1).getStatus());
        assertTrue(check.getItem(2).getStatus());
        assertTrue(check.getItem(3).getStatus());
    }

    @Test
    public void testAddItem() {
        ArrayList<String> items = new ArrayList<String>();

        items.add("zero");
        items.add("one");
        items.add("two");
        items.add("three");

        CheckList check = new CheckList(items);

        check.addItem("four");
        check.addItem("five", true);
        check.addItem("six", false);

        assertFalse(check.getItem(4).getStatus());
        assertTrue(check.getItem(5).getStatus());
        assertFalse(check.getItem(6).getStatus());
    }

    @Test
    public void testRemoveItem() {
        CheckList check = new CheckList();

        check.addItem("zero");
        check.addItem("one");

        check.removeItem(check.getItem(0));
        assertEquals("one", check.getItem(0).getDescription());

        check.removeItem(check.getItem(0));

        boolean get = false;
        try {
            check.getItem(0);
        } catch (IndexOutOfBoundsException e) {
            get = true;
        }

        assertTrue(get);
    }
}
