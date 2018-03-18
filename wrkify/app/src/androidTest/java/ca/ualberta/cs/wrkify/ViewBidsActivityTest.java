/*
 * Copyright 2018 CMPUT301W18T18
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
 *
 */

package ca.ualberta.cs.wrkify;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Test;
import org.junit.Rule;

import java.util.ArrayList;

public class ViewBidsActivityTest {


    @Rule
    public ActivityTestRule<ViewBidsActivity> activityTestRule = new ActivityTestRule<>(
            ViewBidsActivity.class, false, false);


    @Test
    public void runActivity() {
        Intent intent = new Intent();

        User user1 = new User("UserName1", "UserName@email.com", "780-123-1234");
        User user2 = new User("UserName2", "UserName2@email.com", "780-123-1235");
        User user3 = new User("UserName3", "UserName3@email.com", "780-123-1236");
        User user4 = new User("UserName4", "UserName4@email.com", "780-123-1237");
        User user5 = new User("UserName5", "UserName5@email.com", "780-123-1238");
        User user6 = new User("UserName6", "UserName6@email.com", "780-123-1239");
        User user7 = new User("UserName7", "UserName7@email.com", "780-123-1240");
        User user8 = new User("UserName8", "UserName8@email.com", "780-123-1241");
        User user9 = new User("UserName9", "UserName9@email.com", "780-123-1242");
        User user10 = new User("UserName10", "UserName10@email.com", "780-123-1243");
        User user11 = new User("UserName11", "UserName10@email.com", "780-123-1243");
        User user12 = new User("UserName12", "UserName10@email.com", "780-123-1243");
        User user13 = new User("UserName13", "UserName10@email.com", "780-123-1243");
        User user14 = new User("UserName14", "UserName10@email.com", "780-123-1243");
        User user15 = new User("UserName15", "UserName10@email.com", "780-123-1243");
        User user16 = new User("UserName16", "UserName10@email.com", "780-123-1243");

        ArrayList<Bid> bidList = new ArrayList<Bid>();
        bidList.add(new Bid(20.01, user2));
        bidList.add(new Bid(31.25, user3));
        bidList.add(new Bid(50.12, user4));
        bidList.add(new Bid(0.50, user5));
        bidList.add(new Bid(25.42, user6));
        bidList.add(new Bid(53.23, user7));
        bidList.add(new Bid(500.12, user8));
        bidList.add(new Bid(9000.00, user9));
        bidList.add(new Bid(401.12, user10));
        bidList.add(new Bid(23.12, user11));
        bidList.add(new Bid(12.50, user12));
        bidList.add(new Bid(3.50, user13));
        bidList.add(new Bid(4.12, user14));
        bidList.add(new Bid(9.16, user15));
        bidList.add(new Bid(42.19, user16));



        Task task = new Task();
        task.setRequester(user1);
        task.setBidList(bidList);

        intent.putExtra(ViewBidsActivity.EXTRA_VIEWBIDS_VIEWER, user1); //We are the requester
        //intent.putExtra("viewer", user2); //We are NOT the requester
        intent.putExtra(ViewBidsActivity.EXTRA_VIEWBIDS_TASK, task);




        activityTestRule.launchActivity(intent);

    }


}
