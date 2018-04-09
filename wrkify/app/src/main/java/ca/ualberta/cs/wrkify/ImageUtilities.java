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
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Class for compressing Bitmaps to CompressedBitmap format
 */
public abstract class ImageUtilities {

    /**
     * Compresses a bitmap to a base 64 String
     * @param bitmap Bitmap you want to compress
     * @param bytes The maximum size of the compressed image data
     * @param minQuality Minimum quality setting for compression
     * @return
     */
    private static String compressBitmapToB64(Bitmap bitmap, int bytes, int minQuality, Bitmap.CompressFormat format) {

        while (true) {
            Log.i("Compress image to B64", "START");

            int low = 0;
            int high = 100;
            int bestQuality = -1;
            String best = "";
            int mid;
            int iterations = 0;

            while (low <= high) {
                mid = (low + high) / 2;

                if (mid < 0 || mid > 100) {
                    break;
                }

                iterations++;
                //Do compress
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(format, mid, os);
                String result = Base64.encodeToString(os.toByteArray(), Base64.DEFAULT);

                if (result.getBytes().length <= bytes) {
                    best = result;
                    bestQuality = mid;
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }

            }

            Log.i("Compress image to B64", "Iterations: " + iterations);
            if (bestQuality >= minQuality) {
                Log.i("Compress image to B64", "Quality: " + bestQuality);
                return best;
            }

            Log.i("Compress image to B64", "Failed, decreasing dimensions and retrying...");
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            int newWidth = (int) (width * (1 - 0.15));
            int newHeight = (int) (height / ((double) width) * newWidth);

            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        }
    }

    /**
     *
     * @param image Bitmap which you want to make a thumbnail for
     * @return a new Bitmap which is scaled to the thumbnail size
     */
    private static Bitmap makeThumbnailBitmap(Bitmap image){
        return Bitmap.createScaledBitmap(image, 100, 100, false);
    }

    private static CompressedBitmap makeCompressedBitmap(Bitmap image, boolean remote, int bytes, int minQuality, Bitmap.CompressFormat format) {
        String data = compressBitmapToB64(image, bytes, minQuality, format);
        CompressedBitmap bm;

        if (remote) {
            bm = (CompressedBitmap) WrkifyClient.getInstance().create(CompressedBitmap.class, data);
        } else {
            bm = (CompressedBitmap) WrkifyClient.getInstance().createLocal(CompressedBitmap.class, data);
        }
        return bm;
    }

    public static CompressedBitmap makeCompressedThumbnail(Bitmap image, boolean remote) {
        Bitmap thumb = makeThumbnailBitmap(image);
        return makeCompressedBitmap(thumb, remote, 10000, 0, Bitmap.CompressFormat.JPEG);
    }

    public static CompressedBitmap makeCompressedImage(Bitmap image, boolean remote) {
        return makeCompressedBitmap(image, remote, 65536, 10, Bitmap.CompressFormat.JPEG);
    }

}
