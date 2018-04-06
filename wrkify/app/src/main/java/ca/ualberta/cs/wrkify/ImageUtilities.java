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
import android.util.Log;

import java.io.ByteArrayOutputStream;

public abstract class ImageUtilities {

    public static byte[] compressBitmapToBytes(Bitmap bitmap, int bytes, int minQuality) {
        Log.i("Compress image to size", "START");
        //try to get it as a jpeg
        int low = 0;
        int high = 100;
        int bestQuality = -1;
        byte[] best = new byte[0];
        int mid;
        int iterations = 0;
        while (low <= high) {
            mid = (low + high)/2;

            if (mid < 0 || mid > 100) {
                break;
            }

            iterations++;
            //Try to compress
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, mid, os);
            byte[] result = os.toByteArray();

            if (result.length <= bytes) {
                bestQuality = mid;
                best = result;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        Log.i("Compress image to size", "Iterations: " + iterations);

        if (bestQuality >= minQuality) {
            Log.i("Compress image to size", "JPEG quality: " + bestQuality);
            return best;
        }

        Log.i("Compress image to size", "Failed, decreasing size and retrying");
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = (int) (width * (1 - 0.15));
        int newHeight = (int) (height/( (double) width) * newWidth);


        Bitmap retry = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return compressBitmapToBytes(retry, bytes, minQuality);
    }

    public static Bitmap makeThumbnail (Bitmap image){
        return Bitmap.createScaledBitmap(image, 100, 100, false);
    }


    public static CompressedBitmap makeCompressedThumbnail(Bitmap image) {
        Bitmap thumb = makeThumbnail(image);
        CompressedBitmap bm = new CompressedBitmap(compressBitmapToBytes(thumb, 10000, 0));
        return bm;
    }

    public static CompressedBitmap makeCompressedImage(Bitmap image) {
        CompressedBitmap bm = new CompressedBitmap(compressBitmapToBytes(image, 65536, 10));
        return bm;
    }

}
