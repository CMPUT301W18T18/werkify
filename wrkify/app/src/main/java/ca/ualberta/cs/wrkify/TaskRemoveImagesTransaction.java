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

import java.io.IOException;

public class TaskRemoveImagesTransaction extends Transaction<Task> {
    private Task localTask;
    private String thumbnailId;
    private String fullImageId;

    public TaskRemoveImagesTransaction(Task localTask, RemoteReference<CompressedBitmap> thumbnail, RemoteReference<CompressedBitmap> fullImage) {
        super(localTask, Task.class);
        this.localTask = localTask;
        this.thumbnailId = thumbnail.getId();
        this.fullImageId = fullImage.getId();
    }

    public TaskRemoveImagesTransaction(Task localTask, CompressedBitmap thumbnail, CompressedBitmap fullImage) {
        super(localTask, Task.class);
        this.localTask = localTask;
        this.thumbnailId = thumbnail.getId();
        this.fullImageId = fullImage.getId();
    }

    @Override
    public boolean applyInClient(CachingClient client) throws IOException {
        localTask.removeThumbnailById(thumbnailId);
        localTask.removeImageById(fullImageId);


        //String tid = localTask.getId();

        Log.i("-->", "id before: " + localTask.getId());
        //client.upload(localTask);
        Log.i("-->", "id after: " +localTask.getId());
        return true;
    }
}
