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


import android.graphics.Bitmap;
import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Task provides a standard task interface
 * that can be used as a wrkify task
 */
public interface Task extends Serializable {
    String getTitle();
    String getDescription();
    ArrayList<Bitmap> getImageList();
    Location getLocation();
    CheckList getCheckList();
    ArrayList<Bid> getBidList();
    User getRequester();
    User getProvider();
    TaskStatus getStatus();
    Bid getAcceptedBid();

    void setTitle(String title);
    void setDescription(String description);
    void setLocation(Location location);
    void setCheckList(CheckList checkList);
    void setProvider(User provider);

    void addBid(Bid bid);
    void cancelBid(Bid bid);
    void acceptBid(Bid bid);
    void unassign();
    void complete();
}