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
import android.util.Log;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Task is a RemoteObject that stores the information of
 * a task, and provides operations associated with bidding.
 *
 * @see Bid
 * @see User
 * @see RemoteObject
 */
public class Task extends RemoteObject {
    private String title;
    private String description;
    private ArrayList<Bitmap> imageList;
    private TaskLocation location;
    private CheckList checkList;
    private ArrayList<Bid> bidList;
    private RemoteReference<User> requester;
    private RemoteReference<User> provider;
    private TaskStatus status;
    private Bid acceptedBid;
    private ArrayList<RemoteReference<CompressedBitmap>> remoteThumbnails;
    private ArrayList<RemoteReference<CompressedBitmap>> remoteImages;

    /**
     * the internalSetTitle function provides a private/final
     * way of setting title, for the use of constructors/setters
     *
     * @param title the title
     * @throws IllegalArgumentException when constraints are violated
     */
    private void internalSetTitle(String title) throws IllegalArgumentException {
        if (title.length() > 32) {
            throw new IllegalArgumentException("Title too long");
        } else if (title.trim().length() <= 0) {
            throw new IllegalArgumentException("Title cannot be empty");
        } else {
            this.title = title.trim();
        }
    }

    /**
     * the internalSetDescription function provides a private/final
     * way of setting description, for the use of constructors/setters
     *
     * @param description the description
     * @throws IllegalArgumentException when constraints are violated
     */
    private void internalSetDescription(String description) {
        if (description.length() > 512) {
            throw new IllegalArgumentException("Description too long");
        } else {
            this.description = description.trim();
        }
    }

    /**
     * instantiates a Task by title, requester, and description
     *
     * @param title       a string for the title of the Task
     * @param requester   The User that requested the Task
     * @param description a string for the description of the Task
     */
    public Task(String title, RemoteReference<User> requester, String description) {
        internalSetTitle(title);
        internalSetDescription(description);
        this.requester = requester;
        this.status = TaskStatus.REQUESTED;
        this.checkList = new CheckList();
        this.imageList = new ArrayList<Bitmap>();
        this.bidList = new ArrayList<Bid>();
        this.remoteThumbnails = new ArrayList<>();
        this.remoteImages = new ArrayList<>();
    }

    public Task(String title, User requester, String description) {
        this(title, requester.<User>reference(), description);
    }

    // begin the getters

    /**
     * gets the title
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * gets the description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * gets the array of images of the Task
     *
     * @return the array of images
     */
    public ArrayList<Bitmap> getImageList() {
        return imageList;
    }

    /**
     * gets the location of the task
     *
     * @return the location or null
     */
    public TaskLocation getLocation() {
        return location;
    }

    /**
     * gets the checklist of the task
     *
     * @return checklist or null
     */
    public CheckList getCheckList() {
        return checkList;
    }

    /**
     * gets the list of bids associated with the task
     *
     * @return the title
     */
    public ArrayList<Bid> getBidList() {
        return bidList;
    }

    /**
     * gets the User that requested the Task
     *
     * @return the requester
     */
    public User getRemoteRequester(RemoteClient rc) throws IOException {
        if (requester == null) {
            return null;
        }
        return requester.getRemote(rc, User.class);
    }

    /**
     * gets the provider of the task
     *
     * @return the provider (or null)
     */
    public User getRemoteProvider(RemoteClient rc) throws IOException {
        if (provider == null) {
            return null;
        }
        return provider.getRemote(rc, User.class);
    }

    /**
     * gets the status of teh task
     *
     * @return a TaskStatus
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * gets the accepted bid
     *
     * @return the bid (or null)
     */
    public Bid getAcceptedBid() {
        return acceptedBid;
    }

    // begin the setters

    /**
     * sets the title according to internalSetTitle
     *
     * @param title the title
     */
    public void setTitle(String title) {
        if (this.status == TaskStatus.REQUESTED) {
            internalSetTitle(title);
        } else {
            throw new UnsupportedOperationException("cannot edit task after bidding");
        }
    }

    /**
     * sets the description according to internalSetDescription
     *
     * @param description the description
     */
    public void setDescription(String description) {
        if (this.status == TaskStatus.REQUESTED) {
            internalSetDescription(description);
        } else {
            throw new UnsupportedOperationException("cannot edit task after bidding");
        }
    }

    /**
     * sets the location
     *
     * @param location the location
     */
    public void setLocation(TaskLocation location) {
        if (this.status == TaskStatus.REQUESTED) {
            this.location = location;
        } else {
            throw new UnsupportedOperationException("cannot edit task after bidding");
        }
    }

    /**
     * sets the checklist
     *
     * @param checkList the checklist
     */
    public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }

    /**
     * sets the provider
     *
     * @param provider the provider
     * @deprecated use acceptBid instead.
     */
    @Deprecated
    public void setProvider(User provider) {
        this.provider = provider.reference();
    }

    // begin other functions

    /**
     * adds a bid to the list of bids and changes the status
     * to bidded if applicable
     *
     * @param bid the new bid
     */
    public void addBid(Bid bid) {
        if (this.status == TaskStatus.REQUESTED || this.status == TaskStatus.BIDDED) {
            bidList.add(bid);
            Collections.sort(this.bidList);
            if (this.status == TaskStatus.REQUESTED) {
                this.status = TaskStatus.BIDDED;
            }
        } else {
            throw new UnsupportedOperationException("cannot bid on assigned or done task");
        }
    }

    @Nullable
    public Bid getBidForUser(User user) {
        for (Bid bid: this.bidList) {
            if (bid.getBidderReference().equals(user.<User>reference())) {
                return bid;
            }
        }

        return null;
    }

    /**
     * removes a bid and changes the status to requested
     * if the status is bidded
     *
     * @param bid the bid to remove
     */
    public void cancelBid(Bid bid) {
        bidList.remove(bid);

        if (this.bidList.size() == 0 && this.status == TaskStatus.BIDDED) {
            this.status = TaskStatus.REQUESTED;
        }
    }

    /**
     * adds an image to the image list
     *
     * @param image the image you want to add
     */
    public void addImage(Bitmap image) {
        if (this.status == TaskStatus.REQUESTED) {
            this.imageList.add(image);
        } else {
            throw new UnsupportedOperationException("cannot edit task after bidding");
        }
    }

    /**
     * deletes an image from the image list
     * @param image the image that is being removed
     */
    public void delImage(Bitmap image) {
        if (this.status == TaskStatus.REQUESTED) {
            this.imageList.remove(image);
        } else {
            throw new UnsupportedOperationException("cannot edit task after bidding");
        }
    }

    /**
     * sets the status to accepted and gets the provider
     *
     * @param bid the bid that is being accepted
     */
    public void acceptBid(Bid bid) {
        if (this.status == TaskStatus.REQUESTED || this.status == TaskStatus.BIDDED) {
            this.acceptedBid = bid;
            this.status = TaskStatus.ASSIGNED;
            this.provider = bid.getBidderReference();
        } else {
            throw new UnsupportedOperationException("cannot accept bid when already accepted");
        }
    }

    /**
     * unassign the bid and revert the status
     */
    public void unassign() {
        if (this.status == TaskStatus.ASSIGNED) {
            this.provider = null;
            this.status = TaskStatus.BIDDED;
            this.acceptedBid = null;
        } else {
            throw new UnsupportedOperationException("cannot unassign when already unassigned");
        }
    }

    /**
     * marks the task as complete
     */
    public void complete() {
        if (this.status == TaskStatus.ASSIGNED) {
            this.status = TaskStatus.DONE;
        } else {
            throw new UnsupportedOperationException("cannot complete un assigned task");
        }
    }

    /**
     * gets the price of either the lowest bid or the
     * accepted bid depending on the status
     *
     * @return the current price of the task
     */
    public Price getPrice() {
        if (this.status == TaskStatus.ASSIGNED) {
            return this.getAcceptedBid().getValue();
        } else {
            return this.getBidList().get(0).getValue();
        }
    }

    /**
     * Downloads and returns list of compressed thumbnails
     * @return ArrayList of compressed thumbnails, in CompressedBitmap format
     * @throws IOException
     */
    public ArrayList<CompressedBitmap> getCompressedThumbnails() throws IOException {
        ArrayList<CompressedBitmap> list = new ArrayList<>();
        for (int i = 0; i < remoteThumbnails.size(); i++) {
            list.add(remoteThumbnails.get(i).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class));
        }
        return list;
    }

    /**
     * @return ArrayList of remote thumbnails
     */
    public ArrayList<RemoteReference<CompressedBitmap>> getRemoteThumbnails() {
        return remoteThumbnails;
    }

    /**
     * Attach an ArrayList of remote thumbnails to the Task
     * @param list the remote thumbnails to attach to this task
     */
    public void setRemoteThumbnails(ArrayList<RemoteReference<CompressedBitmap>> list){
        this.remoteThumbnails = list;
    }

    /**
     * @return ArrayList of remote images (full-size images)
     */
    public ArrayList<RemoteReference<CompressedBitmap>> getRemoteImages() {
        return remoteImages;
    }

    /**
     * Attach an ArrayList of remote images to the task
     * @param list the list of remote images to attach to this task
     */
    public void setRemoteImages(ArrayList<RemoteReference<CompressedBitmap>> list){
        this.remoteImages = list;
    }

    /**
     * Deletes all images from the Task, and server
     */
    public void deleteAllImages() {
        if (remoteThumbnails != null) {
            for (int i = 0; i < remoteThumbnails.size(); i++) {
                WrkifyClient.getInstance().deleteByReference(remoteThumbnails.get(i), CompressedBitmap.class);
            }
            remoteThumbnails.clear();
        }

        if (remoteImages != null) {
            for (int i = 0; i < remoteImages.size(); i++) {
                WrkifyClient.getInstance().deleteByReference(remoteImages.get(i), CompressedBitmap.class);
            }
            remoteImages.clear();
        }
    }

    public void addReferencePair(RemoteReference<CompressedBitmap> thumbRef, RemoteReference<CompressedBitmap> fullRef) {
        remoteThumbnails.add(thumbRef);
        remoteImages.add(fullRef);
    }

    public void removeThumbnailById(String id) {
        for (int i = remoteThumbnails.size() - 1; i >= 0; i--) {
            if (remoteThumbnails.get(i).getId() == id) {
                remoteThumbnails.remove(i);
                return;
            }
        }
    }

    public void removeImageById(String id) {
        for (int i = remoteImages.size() - 1; i >= 0; i--) {
            if (remoteImages.get(i).getId() == id) {
                remoteImages.remove(i);
                return;
            }
        }
    }

}
