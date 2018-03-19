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

public class Bid implements Comparable<Bid>, Serializable {
    private Double value;
    private RemoteReference<User> bidder;

    public Bid(Double value, RemoteReference<User> bidder) {
        this.value = value;
        this.bidder = bidder;
    }
    
    public Bid(Double value, User bidder) {
        this(value, bidder.<User>reference());
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public User getRemoteBidder(RemoteClient rc) throws IOException {
        return bidder.getRemote(rc, User.class);
    }
    
    public RemoteReference<User> getBidderReference() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder.reference();
    }

    public int compareTo(Bid bid) {
        return this.getValue().compareTo(bid.getValue());
    }
}
