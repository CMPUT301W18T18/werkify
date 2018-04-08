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
 * TaskAddImageTransaction models Task.addImage() as
 * a Transaction
 *
 * @see StateChangeTransaction
 * @see Task
 */
public class TaskAddImageTransaction extends StateChangeTransaction<Task> {
    private Bitmap image;

    /**
     * create a Transaction from a task and the image you want to add
     * @param task the task you are adding an image too.
     * @param image the image you are adding.
     */
    public TaskAddImageTransaction(Task task, Bitmap image) {
        super(task, Task.class);
        this.image = image;
    }

    /**
     * adds the image to the provided task
     * @param task the task you want to add an image too.
     * @return true if successful, false otherwise.
     */
    @Override
    protected Boolean apply(Task task) {
        try {
            task.addImage(this.image);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
