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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CheckList models a simple to do list
 *
 * items can be strings
 */
public class CheckList implements Serializable {
    private List<CheckListItem> items;

    /**
     * the empty construtcor for Checklist
     * creates an empty checklist
     */
    public CheckList() {
        this.items = new ArrayList<>();
    }

    /**
     * creates a checklist with everything unchecked
     * @param descriptions the items of the checklist
     */
    public CheckList(List<String> descriptions) {
        this();

        for (String description: descriptions) {
            this.addItem(description);
        }
    }

    /**
     * Gets the encapsulated list of CheckListItems.
     * @return list of CheckListItems
     */
    public List<CheckListItem> getItems() {
        return items;
    }

    /**
     * Returns the number of items in the CheckList.
     * @return number of items in the CheckList
     */
    public int itemCount() {
        return items.size();
    }

    /**
     * gets the item at index
     * @param index an array index
     * @return the item at index
     */
    public CheckListItem getItem(int index) {
        return this.items.get(index);
    }

    /**
     * add an item with a given status
     * @param description the string of the item
     * @param status whtehr or not the new item is checked
     */
    public void addItem(String description, boolean status) {
        this.items.add(new CheckListItem(description, status));
    }

    /**
     * add an item, defaulting to false
     * @param description the new item description
     */
    public void addItem(String description) {
        this.items.add(new CheckListItem(description));
    }

    /**
     * Removes a CheckListItem from the checklist.
     * @param item item to remove
     */
    public void removeItem(CheckListItem item) { this.items.remove(item); }

    /**
     * A single item in the CheckList.
     * Has a description and a status.
     */
    public static class CheckListItem implements Serializable {
        private String description;
        private boolean status;

        /**
         * Creates a CheckListItem with a description and status.
         * @param description description of the item
         * @param status true if the item is complete; false if it is not complete
         */
        public CheckListItem(String description, boolean status) {
            this.description = description;
            this.status = status;
        }

        /**
         * Creates a CheckListItem with a description, with status set to incomplete.
         * @param description description of the item
         */
        public CheckListItem(String description) {
            this(description, false);
        }

        /**
         * Gets the description of the item.
         * @return description of the item
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the status of the item.
         * @return true if the item is complete; false if it is incomplete
         */
        public boolean getStatus() {
            return status;
        }

        /**
         * Sets the description of the item.
         * @param description new description of the item
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Sets the status of the item.
         * @param status true if the item should be complete; false if it should be incomplete
         */
        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
