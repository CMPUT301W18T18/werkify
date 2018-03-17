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
    private User requester;
    private User provider;
    private TaskStatus status;
    private Bid acceptedBid;

    private void internalSetTitle(String title) throws IllegalArgumentException {
        if (title.length() > 32) {
            throw new IllegalArgumentException("Title too long");
        } else if (title.trim().length() <= 0) {
            throw new IllegalArgumentException("Title cannot be empty");
        } else {
            this.title = title.trim();
        }
    }

    private void internalSetDescription(String description) {
        if (description.length() > 32) {
            throw new IllegalArgumentException("Description too long");
        } else if (description.trim().length() <= 0) {
            throw new IllegalArgumentException("Desctiption cannot be empty");
        } else {
            this.description = description.trim();
        }
    }

    public ConcreteTask(String title, User requester, String description) {
        internalSetTitle(title);
        internalSetDescription(description);
        this.requester = requester;
        this.status = TaskStatus.REQUESTED;
        this.imageList = new ArrayList<Bitmap>();
        this.bidList = new ArrayList<Bid>();
    }

    public ConcreteTask(String title, User requester) {
        this(title, requester, "");
    }

    // begin the setters

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Bitmap> getImageList() {
        return imageList;
    }

    public Location getLocation() {
        return location;
    }

    public CheckList getCheckList() {
        return checkList;
    }

    public ArrayList<Bid> getBidList() {
        return bidList;
    }

    public User getRequester() {
        return requester;
    }

    public User getProvider() {
        return provider;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Bid getAcceptedBid() {
        return acceptedBid;
    }

    // begin the setters

    public void setTitle(String title) {
        internalSetTitle(title);
    }

    public void setDescription(String description) {
        internalSetDescription(description);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    // begin other functions

    public void addBid(Bid bid) {
        bidList.add(bid);
        Collections.sort(this.bidList);
        this.status = TaskStatus.BIDDED;
    }

    public void cancelBid(Bid bid) {
        bidList.remove(bid);

        if (this.bidList.size() == 0) {
            this.status = TaskStatus.ASSIGNED;
        }
    }

    public void acceptBid(Bid bid) {
        this.acceptedBid = bid;
        this.status = TaskStatus.ASSIGNED;
        this.provider = bid.getBidder();
    }

    public void unassign() {
        this.provider = null;
        this.status = TaskStatus.BIDDED;
        this.acceptedBid = null;
    }

    public void complete() {
        this.status = TaskStatus.DONE;
    }
}
