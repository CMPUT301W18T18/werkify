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

import android.graphics.Bitmap;

/**
 * TaskDelImageTransaction models Task.delImage() as
 * a Transaction
 *
 * @see Transaction
 * @see Task
 */
public class TaskDelImageTransaction extends Transaction<Task> {
    private Bitmap image;

    /**
     * create a Transaction from a task and the image you want to delete
     * @param task the task you are delete an image from.
     * @param image the image you are deleting
     */
    public TaskDelImageTransaction(Task task, Bitmap image) {
        super(task, Task.class);
        this.image = image;
    }

    /**
     * deletes the image from the provided task
     * @param task the task you want to delete the image from
     * @return true if successful, false otherwise.
     */
    @Override
    protected Boolean apply(Task task) {
        try {
            task.delImage(this.image);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
