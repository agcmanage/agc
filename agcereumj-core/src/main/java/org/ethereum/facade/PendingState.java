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
package org.one2oneeum.facade;

import org.one2oneeum.core.*;

import java.util.List;
import java.util.Set;

/**
 * @author Mikhail Kalinin
 * @since 28.09.2015
 */
public interface PendingState {

    /**
     * @return pending state repository
     */
    org.one2oneeum.core.Repository getRepository();

    /**
     * @return list of pending transactions
     */
    List<Transaction> getPendingTransactions();
}
