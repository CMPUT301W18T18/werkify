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

import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by peter on 28/03/18.
 */

public class PriceTest {

    @Test
    public void testStringConstructor() {
        Price price = new Price("1.50");
        assertEquals(new BigDecimal("1.50"), price.getValue());

        boolean failed = false;
        try {
            price = new Price("-1.50");
        } catch (IllegalArgumentException e) {
            failed = true;
        }
        assertTrue(failed);
    }

    @Test
    public void testBigDecimalConstructor() {
        Price price = new Price(new BigDecimal("1.50"));
        assertEquals(new BigDecimal("1.50"), price.getValue());

        boolean failed = false;
        try {
            price = new Price(new BigDecimal("-1.50"));
        } catch (IllegalArgumentException e) {
            failed = true;
        }
        assertTrue(failed);
    }

    @Test
    public void testDoubleConstructor() {
        Price price = new Price(1.50);
        assertEquals(new BigDecimal(1.50), price);

        boolean failed = false;
        try {
            price = new Price(1.50);
        } catch (IllegalArgumentException e) {
            failed = true;
        }
        assertTrue(failed);
    }

    @Test
    public void testToString() {
        Price price;

        price = new Price("1.0");
        assertEquals("$1.00", price.toString());

        price = new Price("1.5111");
        assertEquals("$1.51", price.toString());

        price = new Price("1.555");
        assertEquals("$1.56", price.toString());

        price = new Price("500.99");
        assertEquals("$500.99", price.toString());
    }

    @Test
    public void testCompareTo() {
        Price p1 = new Price("1.0");
        Price p2 = new Price("2.0");

        assertEquals(0, p1.compareTo(p1));
        assertEquals(-1, p1.compareTo(p2));
        assertEquals(1, p2.compareTo(p1));
    }

    @Test
    public void testEquals() {
        Price p1 = new Price("1.0");
        Price p2 = new Price("2.0");
        Price p3 = new Price("1.0");

        assertTrue(p1.equals(p3));
        assertFalse(p1.equals(p2));
        assertFalse(p1.equals(null));
        assertFalse(p1.equals("hello"));
        assertFalse(p3.equals(false));
    }
}
