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
package org.one2oneeum.config.blockchain;

import org.one2oneeum.config.BlockchainConfig;

/**
 * Created by Anton Nashatyrev on 18.07.2016.
 */
public class DaoNoHFConfig extends AbstractDaoConfig {

    {
        supportFork = false;
    }

    public DaoNoHFConfig() {
        initDaoConfig(new HomesteadConfig(), ETH_FORK_BLOCK_NUMBER);
    }

    public DaoNoHFConfig(BlockchainConfig parent, long forkBlockNumber) {
        initDaoConfig(parent, forkBlockNumber);
    }

    @Override
    public String toString() {
        return super.toString() + "(forkBlock:" + forkBlockNumber + ")";
    }
}
