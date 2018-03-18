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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * unit tests for the Bid class
 *
 * @author Tao Huang
 * @see Bid
 */
public class BidTest {

    @Test
    public void testBid() throws Exception {
        Double value = 123.45;
        User bidder = new User("A", "A@example.com", "(555) 555-555");

        Bid user = new Bid(value, bidder);
        Double resultvalue = user.getValue();
        User resultbidder = user.getBidder();

        assertEquals(resultvalue, value);
        assertEquals(resultbidder, bidder);

    }

    @Test
    public void testGettersAndSetters() throws Exception {
        Double A_value = 123.45;
        Double B_value = 456.78;
        Double C_value = 0.00;

        User A_bidder = new User("ABC", "ABC@example.com", "(555) 555-555");
        User B_bidder = new User("CDE", "CDE@example.com", "(666) 666-666");

        Bid user = new Bid(A_value, A_bidder);
        user.setValue(B_value);
        assertEquals(user.getValue(), B_value);

        user.setBidder(B_bidder);
        assertEquals(user.getBidder(), B_bidder);

        boolean failed = false;
        try {
            user.setValue(C_value);
        } catch (IllegalArgumentException e) {
            failed = true;
        }

        assertTrue(failed);
    }
}