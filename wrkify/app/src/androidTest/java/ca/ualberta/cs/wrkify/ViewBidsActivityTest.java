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

        ConcreteUser user1 = new ConcreteUser("UserName1", "UserName@email.com", "780-123-1234");
        ConcreteUser user2 = new ConcreteUser("UserName2", "UserName2@email.com", "780-123-1235");
        ConcreteUser user3 = new ConcreteUser("UserName3", "UserName3@email.com", "780-123-1236");
        ConcreteUser user4 = new ConcreteUser("UserName4", "UserName4@email.com", "780-123-1237");
        ConcreteUser user5 = new ConcreteUser("UserName5", "UserName5@email.com", "780-123-1238");
        ConcreteUser user6 = new ConcreteUser("UserName6", "UserName6@email.com", "780-123-1239");
        ConcreteUser user7 = new ConcreteUser("UserName7", "UserName7@email.com", "780-123-1240");
        ConcreteUser user8 = new ConcreteUser("UserName8", "UserName8@email.com", "780-123-1241");
        ConcreteUser user9 = new ConcreteUser("UserName9", "UserName9@email.com", "780-123-1242");
        ConcreteUser user10 = new ConcreteUser("UserName10", "UserName10@email.com", "780-123-1243");
        ConcreteUser user11 = new ConcreteUser("UserName11", "UserName10@email.com", "780-123-1243");
        ConcreteUser user12 = new ConcreteUser("UserName12", "UserName10@email.com", "780-123-1243");
        ConcreteUser user13 = new ConcreteUser("UserName13", "UserName10@email.com", "780-123-1243");
        ConcreteUser user14 = new ConcreteUser("UserName14", "UserName10@email.com", "780-123-1243");
        ConcreteUser user15 = new ConcreteUser("UserName15", "UserName10@email.com", "780-123-1243");
        ConcreteUser user16 = new ConcreteUser("UserName16", "UserName10@email.com", "780-123-1243");

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



        ConcreteTask task = new ConcreteTask();
        task.setRequester(user1);
        task.setBidList(bidList);

        intent.putExtra("viewer", user1); //We are the requester
        //intent.putExtra("viewer", user2); //We are NOT the requester
        intent.putExtra("task", task);




        activityTestRule.launchActivity(intent);

    }


}
