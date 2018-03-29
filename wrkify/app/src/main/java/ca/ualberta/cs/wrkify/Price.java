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

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Price provides an object for the behaviors of a non
 * negative price. implements all of the comparison functions
 * you would expect of a Price.
 *
 * @author Peter Elliott
 */

public class Price implements Comparable<Price> {
    /**
     * price is the internal BigDeicmal that is
     * the entire state of the object. BigDecimal
     * is used to avoid floating point error.
     */
    private BigDecimal price;

    /**
     * creates a Price from the string representation
     * of a price (with no currency symbol).
     *
     * price must be greater than or equal to 0.
     *
     * @param decimalString the string representation of the price
     */
    public Price(String decimalString) {
        this.price = new BigDecimal(decimalString);

        if (this.price.compareTo(new BigDecimal(0)) == -1) {
            throw new IllegalArgumentException("prices cannot be negative");
        }
    }

    /**
     * creates a Price from an existing BigDecimal
     *
     * price must be greater than or equal to 0.
     *
     * @param price the price
     */
    public Price(BigDecimal price) {
        this.price = price;

        if (this.price.compareTo(new BigDecimal(0)) == -1) {
            throw new IllegalArgumentException("prices cannot be negative");
        }
    }

    /**
     * creates a price from a Double
     *
     * price must be greater than or equal to 0.
     *
     * @param price a double of the price
     */
    public Price(Double price) {
        this.price = BigDecimal.valueOf(price);

        if (this.price.compareTo(new BigDecimal(0)) == -1) {
            throw new IllegalArgumentException("prices cannot be negative");
        }
    }

    /**
     * gets the BigDecimal value of the price
     * @return the price
     */
    public BigDecimal getValue() {
        return this.price;
    }

    /**
     * formats the price with a currency symbol to two decimal places
     * @return a string representation of the price.
     */
    @Override
    public String toString() {
        // from https://stackoverflow.com/questions/3395825/ (2018-03-28)
        return NumberFormat.getCurrencyInstance().format(this.price);
    }

    /**
     * compares the price by its internal numerical representation
     * @param other the other price.
     * @return (this>other): 1, (this<other): -1, (this=other): 0
     */
    @Override
    public int compareTo(Price other) {
        return this.price.compareTo(other.price);
    }

    /**
     * checks if two prices are the same
     * @param obj the object to compare to
     * @return true if prices euqal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Price)) {
            return false;

        }

        // we now know obj is a Price
        Price p = (Price) obj;

        return this.price.equals(p.getValue());
    }
}
