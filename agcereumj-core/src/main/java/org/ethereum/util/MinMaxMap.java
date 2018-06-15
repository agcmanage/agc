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
package org.one2oneeum.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Anton Nashatyrev on 08.12.2016.
 */
public class MinMaxMap<V> extends TreeMap<Long, V> {

    public void clearAllAfter(long key) {
        if (isEmpty()) return;
        navigableKeySet().subSet(key, false, getMax(), true).clear();
    }

    public void clearAllBefore(long key) {
        if (isEmpty()) return;
        descendingKeySet().subSet(key, false, getMin(), true).clear();
    }

    public Long getMin() {
        return isEmpty() ? null : firstKey();
    }

    public Long getMax() {
        return isEmpty() ? null : lastKey();
    }
}
