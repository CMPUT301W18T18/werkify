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

/**
*
*/


import android.graphics.Bitmap;
import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;


public class ConcreteTask implements Task {
    private String title;
    private String description;
    private ArrayList<Bitmap> imageList;
    private Location location;
    private CheckList checkList;
    private ArrayList<Bid> bidList;
    private ConcreteUser requester;
    private ConcreteUser provider;
    private TaskStatus status;
    private Double price;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ArrayList<Bitmap> getImageList() {
        return imageList;
    }
    public void setImageList(ArrayList<Bitmap> imageList) {
        this.imageList = imageList;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public CheckList getCheckList() {
        return checkList;
    }
    public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }
    public ArrayList<Bid> getBidList() {
        return bidList;
    }
    public void setBidList(ArrayList<Bid> bidList) {
        this.bidList = bidList;
    }
    public ConcreteUser getRequester() {
        return requester;
    }
    public void setRequester(ConcreteUser requester) {
        this.requester = requester;
    }
    public ConcreteUser getProvider() {
        return provider;
    }
    public void setProvider(ConcreteUser provider) {
        this.provider = provider;
    }
    public TaskStatus getStatus() {
        return status;
    }
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Bid getLowestBid() {
        sortBidList();
        return this.bidList.get(0);
    }
    public void addBid(Bid bid) {
        bidList.add(bid);
        sortBidList();
    }
    public void sortBidList() {
        Collections.sort(this.bidList);
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public void acceptBid(Bid bid) {
        setPrice(bid.getValue());
    }
}
