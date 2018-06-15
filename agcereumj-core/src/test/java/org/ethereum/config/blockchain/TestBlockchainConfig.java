/*
 * Copyright (c) [2017] [ <one2one.camp> ]
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
 *
 *
 */

package org.one2oneeum.config.blockchain;

import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.lang3.tuple.Pair;
import org.one2oneeum.config.Constants;
import org.one2oneeum.config.SystemProperties;
import org.one2oneeum.core.Block;
import org.one2oneeum.core.BlockHeader;
import org.one2oneeum.core.Repository;
import org.one2oneeum.core.Transaction;
import org.one2oneeum.db.BlockStore;
import org.one2oneeum.mine.MinerIfc;
import org.one2oneeum.mine.MinerListener;
import org.one2oneeum.validator.BlockHeaderValidator;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@SuppressWarnings("WeakerAccess")
class TestBlockchainConfig extends AbstractConfig {
    boolean eip161 = false;
    boolean eip198 = false;
    boolean eip206 = false;
    boolean eip211 = false;
    boolean eip212 = false;
    boolean eip213 = false;
    boolean eip214 = false;
    boolean eip658 = false;

    Constants constants = new Constants();
    MinerIfc minerIfc = new TestMinerIfc();
    BigInteger difficulty = BigInteger.valueOf(100);
    BigInteger difficultyMultiplier = BigInteger.valueOf(20);
    long transactionCost = 200L;
    boolean acceptTransactionSignature = true;
    String validateTransactionChanges = "test";
    byte[] extraData = new byte[]{};
    List<Pair<Long, BlockHeaderValidator>> headerValidators = newArrayList();

    public TestBlockchainConfig enableAllEip() {
        return this
                .enableEip161()
                .enableEip198()
                .enableEip206()
                .enableEip211()
                .enableEip212()
                .enableEip213()
                .enableEip214()
                .enableEip658();
    }

    public TestBlockchainConfig enableEip161() {
        this.eip161 = true;
        return this;
    }

    public TestBlockchainConfig enableEip198() {
        this.eip198 = true;
        return this;
    }

    public TestBlockchainConfig enableEip206() {
        this.eip206 = true;
        return this;
    }

    public TestBlockchainConfig enableEip211() {
        this.eip211 = true;
        return this;
    }

    public TestBlockchainConfig enableEip212() {
        this.eip212 = true;
        return this;
    }

    public TestBlockchainConfig enableEip213() {
        this.eip213 = true;
        return this;
    }

    public TestBlockchainConfig enableEip214() {
        this.eip214 = true;
        return this;
    }

    public TestBlockchainConfig enableEip658() {
        this.eip658 = true;
        return this;
    }

    @Override
    public Constants getConstants() {
        return constants;
    }

    @Override
    public MinerIfc getMineAlgorithm(SystemProperties config) {
        return minerIfc;
    }

    @Override
    public BigInteger calcDifficulty(BlockHeader curBlock, BlockHeader parent) {
        return difficulty;
    }

    @Override
    public BigInteger getCalcDifficultyMultiplier(BlockHeader curBlock, BlockHeader parent) {
        return difficultyMultiplier;
    }

    @Override
    public long getTransactionCost(Transaction tx) {
        return transactionCost;
    }

    @Override
    public boolean acceptTransactionSignature(Transaction tx) {
        return acceptTransactionSignature;
    }

    @Override
    public String validateTransactionChanges(BlockStore blockStore, Block curBlock, Transaction tx, Repository repository) {
        return validateTransactionChanges;
    }

    @Override
    public byte[] getExtraData(byte[] minerExtraData, long blockNumber) {
        return extraData;
    }

    @Override
    public List<Pair<Long, BlockHeaderValidator>> headerValidators() {
        return headerValidators;
    }

    @Override
    public boolean eip161() {
        return eip161;
    }

    @Override
    public boolean eip198() {
        return eip198;
    }

    @Override
    public boolean eip212() {
        return eip212;
    }

    @Override
    public boolean eip213() {
        return eip213;
    }


    public class TestMinerIfc implements MinerIfc {
        @Override
        public ListenableFuture<MiningResult> mine(Block block) {
            return null;
        }

        @Override
        public boolean validate(BlockHeader blockHeader) {
            return false;
        }

        @Override
        public void setListeners(Collection<MinerListener> listeners) {}
    }
}
