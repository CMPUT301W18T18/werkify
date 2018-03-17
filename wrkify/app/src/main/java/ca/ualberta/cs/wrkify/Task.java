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
    void setTitle(String title);

    String getDescription();
    void setDescription(String description);

    ArrayList<Bitmap> getImageList();
    void setImageList(ArrayList<Bitmap> imageList);

    Location getLocation();
    void setLocation(Location location);

    CheckList getCheckList();
    void setCheckList(CheckList checkList);

    ArrayList<Bid> getBidList();
    void setBidList(ArrayList<Bid> bidList);

    User getRequester();
    void setRequester(User requester);
    User getProvider();
    void setProvider(User provider);

    TaskStatus getStatus();
    void setStatus(TaskStatus status);

    Bid getLowestBid();
    void addBid(Bid bid);
    void sortBidList();

    Double getPrice();
    void setPrice(Double price);

    void acceptBid(Bid bid);
}