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

    public List<CheckListItem> getItems() {
        return items;
    }

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
     * sets the item description
     * @param index the array index to set
     * @param item the description
     */
    public void setDescriptionAt(int index, String description) {
        this.items.get(index).setDescription(description);
    }

    /**
     * sets the checked status of the item
     * @param index the array index to set at
     * @param status the new status
     */
    public void setStatusAt(int index, boolean status) {
        this.items.get(index).setStatus(status);
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
     * removes the item at index
     * WARNING: will invalidate other indexes you have lying
     * around
     * @param index the index to delete
     */
    public void removeItem(int index) {
        this.items.remove(index);
    }

    public void removeItem(CheckListItem item) { this.items.remove(item); }

    public static class CheckListItem implements Serializable {
        private String description;
        private boolean status;

        public CheckListItem(String description, boolean status) {
            this.description = description;
            this.status = status;
        }

        public CheckListItem(String description) {
            this(description, false);
        }

        public String getDescription() {
            return description;
        }

        public boolean getStatus() {
            return status;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
