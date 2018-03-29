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
 * Created by peter on 28/03/18.
 */

public class Price implements Comparable<Price> {
    private BigDecimal price;

    public Price(String decimalString) {
        this.price = new BigDecimal(decimalString);
    }

    public Price(BigDecimal price) {
        this.price = price;
    }

    public Price(Double price) {
        this.price = BigDecimal.valueOf(price);
    }

    public BigDecimal getValue() {
        return this.price;
    }

    @Override
    public String toString() {
        // from https://stackoverflow.com/questions/3395825/ (2018-03-28)
        return NumberFormat.getCurrencyInstance().format(this.price);
    }

    @Override
    public int compareTo(Price other) {
        return this.price.compareTo(other.price);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!obj.getClass().isInstance(Price.class)) {
            return false;
        }

        Price p = (Price) obj;

        return this.price.equals(p.getValue());
    }
}
