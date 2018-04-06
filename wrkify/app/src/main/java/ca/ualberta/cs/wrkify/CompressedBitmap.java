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
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CompressedBitmap extends RemoteObject {
    byte[] data;

    public CompressedBitmap(byte[] data) {
        this.data = data.clone();
    }

    public Bitmap getBitmap() {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        return BitmapFactory.decodeStream(is);
    }

    public void setData(byte[] data) {
        this.data = data.clone();
    }

    public byte[] getData() {
        return data;
    }

}
