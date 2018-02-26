package ca.ualberta.cs.wrkify;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.graphics.Bitmap;
import android.location.Location;

/**
 * Created by Craig on 2018-02-24.
 *
 * Test Cases fot ConcreteTask class
 *
 * Modified 2018-02-25
 */

public class ConcreteTaskTest {

    @Test
    public void testSetGetTitle() {
        ConcreteTask concTask = new ConcreteTask();
        concTask.setTitle("Test Title");

        assertEquals("Test Title", concTask.getTitle());
    }

    @Test
    public void testSetGetDescription() {
        ConcreteTask concTask = new ConcreteTask();
        concTask.setDescription("Test Description");

        assertEquals("Test Description",concTask.getDescription());
    }

    @Test
    public void testSetGetImageList() {
        ConcreteTask concTask = new ConcreteTask();

        Bitmap image = null; // Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888); is not mocked
        ArrayList<Bitmap> imgList = new ArrayList<>();
        imgList.add(image);

        concTask.setImageList(imgList);

        assertEquals(imgList, concTask.getImageList());
    }

    @Test
    public void testSetGetLocation() {
        ConcreteTask concTask = new ConcreteTask();
        Location location = new Location("Test");

        concTask.setLocation(location);

        assertEquals(location, concTask.getLocation());
    }

    @Test
    public void testSetGetChecklist() {
        ConcreteTask concTask = new ConcreteTask();
        CheckList checklist = new CheckList();

        concTask.setCheckList(checklist);

        assertEquals(checklist, concTask.getCheckList());
    }

    @Test
    public void testSetGetBidList() {
        ConcreteTask concTask = new ConcreteTask();
        ArrayList<Bid> bidList = new ArrayList<>();

        concTask.setBidList(bidList);

        assertEquals(bidList, concTask.getBidList());
    }

    @Test
    public void testSetGetRequester() {
        ConcreteTask concTask = new ConcreteTask();
        ConcreteUser concUser = new ConcreteUser("Test", "Test@Test.com", "12 345 67890");

        concTask.setRequester(concUser);

        assertEquals(concUser, concTask.getRequester());

    }

    @Test
    public void testSetGetProvider() {
        ConcreteTask concTask = new ConcreteTask();
        ConcreteUser concUser = new ConcreteUser("Test", "Test@Test.com", "12 345 67890");

        concTask.setProvider(concUser);

        assertEquals(concUser, concTask.getProvider());
    }

    @Test
    public void testSetGetStatus() {
        ConcreteTask concTask = new ConcreteTask();
        TaskStatus taskStat = TaskStatus.REQUESTED;

        concTask.setStatus(taskStat);

        assertEquals(taskStat, concTask.getStatus());
    }

    @Test
    public void testGetLowestBid() {
        Random rand = new Random();
        Double number;// = 0.0; is implied
        Double lowbid = 20.0;
        ConcreteTask concTask = new ConcreteTask();
        ArrayList<Bid> bidList = new ArrayList<>();

        for(int i=0; i < 10; i++){
            number = rand.nextDouble()*20;
            if (number < lowbid) {
                lowbid = number;
            }
            Bid bid = new Bid();
            bid.setValue(number);
            bidList.add(bid);
        }

        concTask.setBidList(bidList);

        assertEquals(lowbid, concTask.getLowestBid().getValue());
    }

    @Test
    public void testAddBid () {
        ConcreteTask concTask = new ConcreteTask();
        Bid bid = new Bid();

        concTask.addBid(bid);

        assertEquals(bid, concTask.getBidList().get(0));
    }

    @Test
    public void testSortBidList () {
        Random rand = new Random();
        Double number; // = 0.0;
        ConcreteTask concTask = new ConcreteTask();
        ArrayList<Bid> bidList = new ArrayList<>();

        for(int i=0; i < 10; i++){
            number = rand.nextDouble()*20;
            Bid bid = new Bid();
            bid.setValue(number);
            bidList.add(bid);
        }
        concTask.setBidList(bidList);

        Collections.sort(bidList);

        concTask.sortBidList();

        assertEquals(bidList, concTask.getBidList());
    }

    @Test
    public void testSetGetPrice() {
        ConcreteTask concTask = new ConcreteTask();
        Double price = 12.0;
        concTask.setPrice(price);

        assertEquals(price, concTask.getPrice());
    }

    @Test
    public void testAcceptBid() {
        ConcreteTask concTask = new ConcreteTask();
        Bid bid = new Bid();
        bid.setValue(20.0);

        concTask.acceptBid(bid);

        assertEquals(bid.getValue(), concTask.getPrice());
    }

}
