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

import android.util.Log;

/**
 * TaskCheckListTransaction represents a Transaction for
 * Task.setCheckList()
 *
 * @see StateChangeTransaction
 * @see Task
 * @see CheckList
 */
public class ImageAttachTransaction extends StateChangeTransaction<Task> {
    private String thumbId;
    private String fullId;

    public ImageAttachTransaction(Task task, RemoteReference<CompressedBitmap> thumb, RemoteReference<CompressedBitmap> full) {
        super(task, Task.class);
        this.thumbId = thumb.getRefId();
        this.fullId = full.getRefId();
    }

    /**
     * calls task.setCheckList();
     * @param task the task to be updated
     * @return true if sucessful(always);
     */
    @Override
    protected Boolean apply(Task task) {
        String newThumbId = WrkifyClient.getInstance().canonicalize(thumbId);
        String newFullId = WrkifyClient.getInstance().canonicalize(fullId);

        if (newThumbId == null || newFullId == null) {
            Log.e("ImageAttachTransaction", "FAILED");
            return false;
        }


        task.addImagePair(new RemoteReference<CompressedBitmap>(newThumbId), new RemoteReference<CompressedBitmap>(newFullId));

        return true;
    }
}
