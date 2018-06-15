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
import org.one2oneeum.crypto.ECKey;
import org.one2oneeum.listener.one2oneeumListener;
import org.one2oneeum.manager.AdminInfo;
import org.one2oneeum.manager.BlockLoader;
import org.one2oneeum.mine.BlockMiner;
import org.one2oneeum.net.client.PeerClient;
import org.one2oneeum.net.rlpx.Node;
import org.one2oneeum.net.server.ChannelManager;
import org.one2oneeum.net.shh.Whisper;
import org.one2oneeum.vm.program.ProgramResult;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Roman Mandeleil
 * @since 27.07.2014
 */
public interface one2oneeum {

    void startPeerDiscovery();

    void stopPeerDiscovery();

    void connect(InetAddress addr, int port, String remoteId);

    void connect(String ip, int port, String remoteId);

    void connect(Node node);

    Blockchain getBlockchain();

    void addListener(one2oneeumListener listener);

    PeerClient getDefaultPeer();

    boolean isConnected();

    void close();

    /**
     * Gets the current sync state
     */
    SyncStatus getSyncStatus();

    /**
     * Factory for general transaction
     *
     *
     * @param nonce - account nonce, based on number of transaction submited by
     *                this account
     * @param gasPrice - gas price bid by miner , the user ask can be based on
     *                   lastr submited block
     * @param gas - the quantity of gas requested for the transaction
     * @param receiveAddress - the target address of the transaction
     * @param value - the one2one value of the transaction
     * @param data - can be init procedure for creational transaction,
     *               also msg data for invoke transaction for only value
     *               transactions this one is empty.
     * @return newly created transaction
     */
    Transaction createTransaction(BigInteger nonce,
                                 BigInteger gasPrice,
                                 BigInteger gas,
                                 byte[] receiveAddress,
                                 BigInteger value, byte[] data);


    /**
     * @param transaction submit transaction to the net, return option to wait for net
     *                    return this transaction as approved
     */
    Future<Transaction> submitTransaction(Transaction transaction);


    /**
     * Executes the transaction based on the specified block but doesn't change the blockchain state
     * and doesn't send the transaction to the network
     * @param tx     The transaction to execute. No need to sign the transaction and specify the correct nonce
     * @param block  Transaction is executed the same way as if it was executed after all transactions existing
     *               in that block. I.e. the root state is the same as this block's root state and this block
     *               is assumed to be the current block
     * @return       receipt of the executed transaction
     */
    TransactionReceipt callConstant(Transaction tx, Block block);

    /**
     * Executes Txes of the block in the same order and from the same state root
     * as they were executed during regular block import
     * This method doesn't make changes in blockchain state
     *
     * <b>Note:</b> requires block's ancestor to be presented in the database
     *
     * @param block block to be replayed
     * @return block summary with receipts and execution summaries
     *         <b>Note:</b> it doesn't include block rewards info
     */
    BlockSummary replayBlock(Block block);

    /**
     * Call a contract function locally without sending transaction to the network
     * and without changing contract storage.
     * @param receiveAddress hex encoded contract address
     * @param function  contract function
     * @param funcArgs  function arguments
     * @return function result. The return value can be fetched via {@link ProgramResult#getHReturn()}
     * and decoded with {@link org.one2oneeum.core.CallTransaction.Function#decodeResult(byte[])}.
     */
    ProgramResult callConstantFunction(String receiveAddress, CallTransaction.Function function,
                                       Object... funcArgs);


    /**
     * Call a contract function locally without sending transaction to the network
     * and without changing contract storage.
     * @param receiveAddress hex encoded contract address
     * @param senderPrivateKey  Normally the constant call doesn't require a sender though
     *                          in some cases it may affect the result (e.g. if function refers to msg.sender)
     * @param function  contract function
     * @param funcArgs  function arguments
     * @return function result. The return value can be fetched via {@link ProgramResult#getHReturn()}
     * and decoded with {@link org.one2oneeum.core.CallTransaction.Function#decodeResult(byte[])}.
     */
    ProgramResult callConstantFunction(String receiveAddress, ECKey senderPrivateKey,
                                       CallTransaction.Function function, Object... funcArgs);

    /**
     * Returns the Repository instance which always refers to the latest (best block) state
     * It is always better using {@link #getLastRepositorySnapshot()} to work on immutable
     * state as this instance can change its state between calls (when a new block is imported)
     *
     * @return - repository for all state data.
     */
    Repository getRepository();

    /**
     * Returns the latest (best block) Repository snapshot
     */
    Repository getLastRepositorySnapshot();

    /**
     * @return - pending state repository
     */
    Repository getPendingState();

//  2.   // is blockchain still loading - if buffer is not empty

    Repository getSnapshotTo(byte[] root);

    AdminInfo getAdminInfo();

    ChannelManager getChannelManager();

    /**
     * @return - currently pending transactions received from the net
     */
    List<Transaction> getWireTransactions();

    /**
     * @return - currently pending transactions sent to the net
     */
    List<Transaction> getPendingStateTransactions();

    BlockLoader getBlockLoader();

    /**
     * @return Whisper implementation if the protocol is available
     */
    Whisper getWhisper();

    /**
     *  Gets the Miner component
     */
    BlockMiner getBlockMiner();

    /**
     * Initiates blockchain syncing process
     */
    void initSyncing();

    /**
     * @deprecated
     * Calculates a 'reasonable' Gas price based on statistics of the latest transaction's Gas prices
     * Normally the price returned should be sufficient to execute a transaction since ~25% of the latest
     * transactions were executed at this or lower price.
     * If the transaction is wanted to be executed promptly with higher chances the returned price might
     * be increased at some ratio (e.g. * 1.2)
     *
     * <b>UPDATED</b>: Old version of gas tracking greatly fluctuates in networks with big number of transactions
     * like one2oneeum MainNet. But it's light and simple and still could be used for test networks. If you
     * want to get accurate recommended gas price use {@link org.one2oneeum.listener.RecommendedGasPriceTracker}
     * instead by adding it to listener and polling data.
     * Updated tracker is not enabled by default because it needs noticeable resources
     * and is excessive for most users.
     */
    @Deprecated
    long getGasPrice();

    /**
     * Chain id for next block.
     * Introduced in EIP-155
     * @return chain id or null
     */
    Integer getChainIdForNextBlock();

    /**
     * Manual switch to Short Sync mode
     * Maybe useful in small private and detached networks when automatic detection fails
     * @return Future, which completes when syncDone is turned to True in {@link org.one2oneeum.sync.SyncManager}
     */
    CompletableFuture<Void> switchToShortSync();

    void exitOn(long number);
}
