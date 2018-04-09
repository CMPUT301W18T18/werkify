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

public class ImageUploadTransaction extends Transaction<CompressedBitmap> {
    private CompressedBitmap localBitmap;

    public ImageUploadTransaction(CompressedBitmap localBitmap) {
        super(localBitmap, CompressedBitmap.class);
        this.localBitmap = localBitmap;
    }

    @Override
    public boolean applyInClient(CachingClient client) throws IOException {



        Log.i("--> (IMAGE UP)", "id before: " + localBitmap.getId());
        client.uploadNew(CompressedBitmap.class, localBitmap);
        Log.i("--> (IMAGE UP)", "id after: " + localBitmap.getId());
        return true;
    }
}
