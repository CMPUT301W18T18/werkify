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

import java.util.ArrayList;



public interface Task {
    public String getTitle();
    public void setTitle(String title);

    public String getDescription();
    public void setDescription(String description);

    public ArrayList<Bitmap> getImageList();
    public void setImageList(ArrayList<Bitmap> imageList);

    public Location getLocation();
    public void setLocation(Location location);

    public CheckList getCheckList();
    public void setCheckList(CheckList checkList);

    public ArrayList<Bid> getBidList();
    public void setBidList(ArrayList<Bid> bidList);

    public ConcreteUser getRequester();
    public void setRequester(ConcreteUser requester);
    public ConcreteUser getProvider();
    public void setProvider(ConcreteUser provider);

    public TaskStatus getStatus();
    public void setStatus(TaskStatus status);

    public Bid getLowestBid();
    public void addBid(Bid bid);
    public void sortBidList();

    public Double getPrice();
    public void setPrice(Double price);

    public void acceptBid(Bid bid);

}