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
package org.one2oneeum.util.blockchain;

/**
 * Abstract one2oneeum contract
 *
 * Created by Anton Nashatyrev on 01.04.2016.
 */
public interface Contract {

    /**
     * Address of the contract. If the contract creation transaction is
     * still in pending state (not included to a block) the address can be missed
     */
    byte[] getAddress();

    /**
     * Submits contract invocation transaction
     */
    void call(byte[] callData);

    /**
     * Returns the interface for accessing contract storage
     */
    ContractStorage getStorage();

    /**
     * Returns the contract code binary Hex encoded
     */
    String getBinary();
}
