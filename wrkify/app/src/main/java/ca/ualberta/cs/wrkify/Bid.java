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

import java.io.IOException;
import java.io.Serializable;

/**
 * Bid provides a model of a bid, with a user and a price,
 * that is strictly contained by a task
 *
 * @see Task
 * @see User
 */
public class Bid implements Comparable<Bid>, Serializable {
    private Double value;
    private RemoteReference<User> bidder;

    /**
     * creates a Bid from a value and a remote reference to
     * a User. this should not be used in favour of Bid(Double, User)
     * @param value the price the bidder will work for
     * @param bidder a RemoteReference to the bidder
     */
    public Bid(Double value, RemoteReference<User> bidder) {
        this.value = value;
        this.bidder = bidder;
    }

    /**
     * creates a Bid from a value and a bidder
     * @param value the price the bidder will work for
     * @param bidder the bidder
     */
    public Bid(Double value, User bidder) {
        this(value, bidder.<User>reference());
    }

    /**
     * gets the value of the bid
     * @return the value of a bid
     */
    public Double getValue() {
        return value;
    }

    /**
     * sets the value of the bid
     * @param value the value of the bid
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * gets the bidder from a client, dereferencing the bidder
     * @param rc the client we are getting the bidder from
     * @return the bidder User object
     * @throws IOException when we cant get the Bidder
     */
    public User getRemoteBidder(RemoteClient rc) throws IOException {
        if (bidder == null) {
            return null;
        }
        return bidder.getRemote(rc, User.class);
    }

    /**
     * gets the reference to the bidder
     * @return the reference to the bidder
     */
    public RemoteReference<User> getBidderReference() {
        return bidder;
    }

    /**
     * sets the bidder of the bid, by creating a new
     * RemoteReference to it.
     * @param bidder the new bidder
     */
    public void setBidder(User bidder) {
        this.bidder = bidder.reference();
    }

    /**
     * returns 1 if this is smaller in price than bid, -1
     * if greater, 0 if equal
     * @param bid the other bid we are comapring
     * @return an int indicating the comparison order of the two bids
     */
    @Override
    public int compareTo(Bid bid) {
        return this.getValue().compareTo(bid.getValue());
    }
}
