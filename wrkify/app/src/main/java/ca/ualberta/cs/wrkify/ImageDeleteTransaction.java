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

public class ImageDeleteTransaction extends Transaction<CompressedBitmap> {
    public ImageDeleteTransaction(CompressedBitmap localBitmap) {
        super(localBitmap, CompressedBitmap.class);
    }

    @Override
    public boolean applyInClient(CachingClient client) throws IOException {
        CompressedBitmap bitmap = (CompressedBitmap) client.downloadFromRemote(getId(), getType());
        if (bitmap == null) { return false; }
        client.delete(bitmap);

        return true;
    }
}
