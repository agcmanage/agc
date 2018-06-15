/*
 * Copyright (c) [2016] [ <one2one.camp> ]
 * This file is part of the one2oneeumJ library.
 *
 * The one2oneeumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The one2oneeumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the one2oneeumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.one2oneeum.core;

import java.math.BigInteger;

public enum Denomination {

    WEI(newBigInt(0)),
    SZABO(newBigInt(12)),
    FINNEY(newBigInt(15)),
    one2one(newBigInt(18));

    private BigInteger amount;

    private Denomination(BigInteger value) {
        this.amount = value;
    }

    public BigInteger value() {
        return amount;
    }

    public long longValue() {
        return value().longValue();
    }

    private static BigInteger newBigInt(int value) {
        return BigInteger.valueOf(10).pow(value);
    }

    public static String toFriendlyString(BigInteger value) {
        if (value.compareTo(one2one.value()) == 1 || value.compareTo(one2one.value()) == 0) {
            return Float.toString(value.divide(one2one.value()).floatValue()) +  " one2one";
        }
        else if(value.compareTo(FINNEY.value()) == 1 || value.compareTo(FINNEY.value()) == 0) {
            return Float.toString(value.divide(FINNEY.value()).floatValue()) +  " FINNEY";
        }
        else if(value.compareTo(SZABO.value()) == 1 || value.compareTo(SZABO.value()) == 0) {
            return Float.toString(value.divide(SZABO.value()).floatValue()) +  " SZABO";
        }
        else
            return Float.toString(value.divide(WEI.value()).floatValue()) +  " WEI";
    }
}
