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
    private Location location;
    private CheckList checkList;
    private ArrayList<Bid> bidList;
    private RemoteReference<User> requester;
    private RemoteReference<User> provider;
    private TaskStatus status;
    private Bid acceptedBid;

    private ArrayList<RemoteReference<CompressedBitmap>> remoteThumbnails;
    private ArrayList<RemoteReference<CompressedBitmap>> remoteImages;


    //TODO remove these later???
    public void setRemoteThumbnails(ArrayList<RemoteReference<CompressedBitmap>> list){
        this.remoteThumbnails = list;
    }

    public void setRemoteImages(ArrayList<RemoteReference<CompressedBitmap>> list){
        this.remoteImages = list;
    }

    //End of functions to (maybe?) remove

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
    }

    public Task(String title, User requester, String description) {
        this(title, requester.<User>reference(), description);
    }

    // begin the setters

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
    public Location getLocation() {
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
        internalSetTitle(title);
    }

    /**
     * sets the description according to internalSetDescription
     *
     * @param description the description
     */
    public void setDescription(String description) {
        internalSetDescription(description);
    }

    /**
     * sets the location
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        this.location = location;
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
     */
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
        bidList.add(bid);
        Collections.sort(this.bidList);
        if (this.status == TaskStatus.REQUESTED) {
            this.status = TaskStatus.BIDDED;
        }
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
        this.imageList.add(image);
    }

    /**
     * deletes an image fromthe image list
     *
     * @param image the image that is being removed
     */
    public void delImage(Bitmap image) {
        this.imageList.remove(image);
    }

    /**
     * sets the status to accepted and gets the provider
     *
     * @param bid the bid that is being accepted
     */
    public void acceptBid(Bid bid) {
        this.acceptedBid = bid;
        this.status = TaskStatus.ASSIGNED;
        this.provider = bid.getBidderReference();
    }

    /**
     * unassign the bid and revert the status
     */
    public void unassign() {
        this.provider = null;
        this.status = TaskStatus.BIDDED;
        this.acceptedBid = null;
    }

    /**
     * marks the task as complete
     */
    public void complete() {
        this.status = TaskStatus.DONE;
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

    public ArrayList<CompressedBitmap> getCompressedThumbnails() throws IOException {
        ArrayList<CompressedBitmap> list = new ArrayList<>();
        for (int i = 0; i < remoteThumbnails.size(); i++) {
            list.add(remoteThumbnails.get(i).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class));
        }
        return list;
    }

    public ArrayList<RemoteReference<CompressedBitmap>> getRemoteImages() throws IOException {
        return remoteImages;
    }

    public ArrayList<CompressedBitmap> getConcreteImages() throws IOException {
        ArrayList<CompressedBitmap> list = new ArrayList<>();
        for (int i = 0; i < remoteImages.size(); i++) {
            list.add(remoteImages.get(i).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class));
        }
        return list;
    }

    public void addImagePair(Bitmap thumbnail, Bitmap fullImage) {
        byte[] c_thumbnail = ImageUtilities.compressBitmapToBytes(thumbnail, 65536,10);
        byte[] c_fullImage = ImageUtilities.compressBitmapToBytes(fullImage, 65536,10);
        CompressedBitmap cb_thumbnail = WrkifyClient.getInstance().create(CompressedBitmap.class, c_thumbnail);
        CompressedBitmap cb_fullImage = WrkifyClient.getInstance().create(CompressedBitmap.class, c_fullImage);
        RemoteReference<CompressedBitmap> remoteThumbnail = cb_thumbnail.reference();
        RemoteReference<CompressedBitmap> remoteFullImage = cb_fullImage.reference();
        remoteThumbnails.add(remoteThumbnail);
        remoteImages.add(remoteFullImage);
    }

    public CompressedBitmap getImage(int position) {
        try {
            return remoteImages.get(position).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class);
        } catch (IOException e) {
            Log.e("Task", "getImage() failed");
        }
        return null;
    }

    public boolean deleteAllImages() {
        boolean success = true;
        for (int i = 0; i < remoteThumbnails.size(); i++) {
            try {
                WrkifyClient.getInstance().delete(remoteThumbnails.get(i).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class));
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            }
        }

        for (int i = 0; i < remoteImages.size(); i++) {
            try {
                WrkifyClient.getInstance().delete(remoteImages.get(i).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class));
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            }
        }
        remoteThumbnails.clear();
        remoteImages.clear();
        return success;
    }

}
