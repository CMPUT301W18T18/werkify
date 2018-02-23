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

import java.util.ArrayList;

/**
 * CheckList models a simple to do list
 *
 * items can be strings
 */
public class CheckList {
    private ArrayList<String> items;
    private ArrayList<Boolean> checked;

    /**
     * the empty construtcor for Checklist
     * creates an empty checklist
     */
    public CheckList() {}

    /**
     * creates a checklist with everything unchecked
     * @param items the items of the checklist
     */
    public CheckList(ArrayList<String> items) {}

    /**
     * gets the item at index
     * @param index an array index
     * @return the item at index
     */
    public String getItem(int index) {
        return "";
    }

    /**
     * gets a boolean indicating checked
     * @param index index of the item
     * @return the boolean status
     */
    public Boolean getStatus(int index) {
        return false;
    }

    /**
     * sets the item description
     * @param index the array index to set
     * @param item the description
     */
    public void setItem(int index, String item) {}

    /**
     * sets the checked status of the item
     * @param index the array index to set at
     * @param status the new status
     */
    public void setStatus(int index, boolean status) {}

    /**
     * add an item with a given status
     * @param item the string of the item
     * @param status whtehr or not the new item is checked
     */
    public void addItem(String item, boolean status) {}

    /**
     * add an item, defaulting to false
     * @param item the new item description
     */
    public void addItem(String item) {}

    /**
     * removes the item at index
     * WARNING: will invalidate other indexs you have lying
     * around
     * @param index the index to delete
     */
    public void removeItem(int index) {}
}
