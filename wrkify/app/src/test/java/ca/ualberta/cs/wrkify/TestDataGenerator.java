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


import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;

public class TestDataGenerator {
    private static ArrayList<User> users;
    private static ArrayList<Bid> bids;
    private static ArrayList<Task> tasks;

    public void printAll(ArrayList a){
        for (int i = 0; i < a.size(); i++) {
            System.out.println(a.get(i).toString());
        }
    }

    @Test
    public void testTestDataGenerator(){
        makeData();

        System.out.println("*************Users*************");
        printAll(getAllUsers());
        System.out.println("*************Bids**************");
        printAll(getAllBids());
        System.out.println("************Tasks************");
        printAll(getAllTasks());



    }


    public static void makeData(){
        users = new ArrayList<User>();
        bids = new ArrayList<Bid>();
        tasks = new ArrayList<Task>();

        users.add(new User("Peter2", "peter@pelliott.ca", "780-420-1337"));
        users.add(new User("Stefan4", "stefan@gmail.com", "780-124-7331"));
        users.add(new User("John47", "john2@yahoo.com", "780-500-1002"));
        users.add(new User("Taylor1", "taylor@gmail.com", "780-592-9483"));
        users.add(new User("Joshua17", "joshua@hotmail.com", "212-100-9749"));
        users.add(new User("Gregory420", "gregory@gmail.com", "989-420-3910"));
        users.add(new User("Elliott92", "elliott@yahoo.com", "870-749-9387"));
        users.add(new User("Dorn44", "jd@outlook.com", "876-694-7436"));
        users.add(new User("Chester92", "mememan@yahoo.com", "435-534-4548"));
        users.add(new User("Ben2", "benjamin@gmail.com", "301-820-0396"));
        users.add(new User("Emma", "em@hotmail.com", "219-848-5235"));
        users.add(new User("Sophia", "sp@gmail.com", "226-245-938"));
        users.add(new User("Michelle2846", "redcat@hotmail.com", "281-388-3713"));
        users.add(new User("Karen389", "yellowbox@google.ca", "385-383-5823"));
        users.add(new User("Kimberly", "bluebin@gmail.com", "201-284-2758"));
        users.add(new User("Calvin", "greentree@yahoo.com", "430-534-9284"));
        users.add(new User("Anne89273", "orangeblock@gmail.com", "345-436-2346"));
        users.add(new User("Shannon245", "smh@outlook.com", "339-534-7534"));
        users.add(new User("Allison13", "bendysquare@gmail.com", "581-837-4917"));
        users.add(new User("Jeff", "jeff@outlook.com", "409-634-7533"));

        bids.add(new Bid(768.89, users.get(0)));
        bids.add(new Bid(221.80, users.get(1)));
        bids.add(new Bid(708.5, users.get(2)));
        bids.add(new Bid(33.74, users.get(3)));
        bids.add(new Bid(176.53, users.get(4)));
        bids.add(new Bid(3237.78, users.get(5)));
        bids.add(new Bid(0.34, users.get(6)));
        bids.add(new Bid(2780.12, users.get(7)));
        bids.add(new Bid(0.30, users.get(8)));
        bids.add(new Bid(15.75, users.get(9)));
        bids.add(new Bid(6.2, users.get(10)));
        bids.add(new Bid(2586.100, users.get(11)));
        bids.add(new Bid(251.100, users.get(12)));
        bids.add(new Bid(3433.11, users.get(13)));
        bids.add(new Bid(9.18, users.get(14)));
        bids.add(new Bid(277.74, users.get(15)));
        bids.add(new Bid(3747.83, users.get(16)));
        bids.add(new Bid(0.44, users.get(17)));
        bids.add(new Bid(365.18, users.get(18)));
        bids.add(new Bid(1930.82, users.get(19)));

        // No bids
        Task task1 = new Task("Finish My Assignment", users.get(0), "I have an assignment due at 4:00 PM that I need done");

        // A few bids
        Task task2 = new Task("Cut Down this Tree", users.get(1), "There's a tree outside my house that I need cut down, because it's blocking my window");
        task2.addBid(bids.get(0));
        task2.addBid(bids.get(1));
        task2.addBid(bids.get(2));

        // One bid
        Task task3 = new Task("Pick up Groceries", users.get(2), "I need milk, eggs, lettuce, and tomatos, and banana bread");
        task3.addBid(bids.get(3));

        // Completed
        Task task4 = new Task("Shovel Snow in my Driveway", users.get(3), "My car can't get out of my driveway because there's a lot of snow. I need someone to clear the snow.");
        task4.addBid(bids.get(4));
        task4.acceptBid(bids.get(4));
        task4.complete();

        //Assigned
        Task task5 = new Task("Drive from Edmonton to Mexico", users.get(4), "I need to go from Edmonton to Mexico by car");
        task5.addBid(bids.get(5));
        task5.acceptBid(bids.get(5));

        Task task6 = new Task("Pick up my Dog from the Vet", users.get(4), "My dog is at the Vet, and needs to be picked up. Drop my dog off at 8956 Pinetree Drive");
        task6.addBid(bids.get(6));
        task6.addBid(bids.get(7));
        task6.acceptBid(bids.get(7));

        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);
        tasks.add(task6);
    }

    public static User getUser(int position){
        return users.get(position);
    }

    public static int getUserCount(){
        return users.size();
    }

    public static ArrayList<User> getAllUsers(){
        return users;
    }

    public static Bid getBid(int position){
        return bids.get(position);
    }

    public static int getBidCount(){
        return bids.size();
    }

    public static ArrayList<Bid> getAllBids(){
        return bids;
    }

    public static ArrayList<Task> getAllTasks(){
        return tasks;
    }
}
