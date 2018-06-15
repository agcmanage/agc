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
package org.one2oneeum.net.eth.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.one2oneeum.db.BlockStore;
import org.one2oneeum.listener.one2oneeumListener;
import org.one2oneeum.config.SystemProperties;
import org.one2oneeum.core.*;
import org.one2oneeum.listener.Compositeone2oneeumListener;
import org.one2oneeum.listener.one2oneeumListenerAdapter;
import org.one2oneeum.net.MessageQueue;
import org.one2oneeum.net.eth.EthVersion;
import org.one2oneeum.net.eth.message.*;
import org.one2oneeum.net.message.ReasonCode;
import org.one2oneeum.net.server.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Process the messages between peers with 'eth' capability on the network<br>
 * Contains common logic to all supported versions
 * delegating version specific stuff to its descendants
 *
 */
public abstract class EthHandler extends SimpleChannelInboundHandler<EthMessage> implements Eth {

    private final static Logger logger = LoggerFactory.getLogger("net");

    protected Blockchain blockchain;

    protected SystemProperties config;

    protected Compositeone2oneeumListener one2oneeumListener;

    protected Channel channel;

    private MessageQueue msgQueue = null;

    protected EthVersion version;

    protected boolean peerDiscoveryMode = false;

    protected Block bestBlock;
    protected one2oneeumListener listener = new one2oneeumListenerAdapter() {
        @Override
        public void onBlock(Block block, List<TransactionReceipt> receipts) {
            bestBlock = block;
        }
    };

    protected boolean processTransactions = false;

    protected EthHandler(EthVersion version) {
        this.version = version;
    }

    protected EthHandler(final EthVersion version, final SystemProperties config,
                         final Blockchain blockchain, final BlockStore blockStore,
                         final Compositeone2oneeumListener one2oneeumListener) {
        this.version = version;
        this.config = config;
        this.one2oneeumListener = one2oneeumListener;
        this.blockchain = blockchain;
        bestBlock = blockStore.getBestBlock();
        this.one2oneeumListener.addListener(listener);
        // when sync enabled we delay transactions processing until sync is complete
        processTransactions = !config.isSyncEnabled();
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, EthMessage msg) throws InterruptedException {

        if (EthMessageCodes.inRange(msg.getCommand().asByte(), version))
            logger.trace("EthHandler invoke: [{}]", msg.getCommand());

        one2oneeumListener.trace(String.format("EthHandler invoke: [%s]", msg.getCommand()));

        channel.getNodeStatistics().ethInbound.add();

        msgQueue.receivedMessage(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Eth handling failed", cause);
        ctx.close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.debug("handlerRemoved: kill timers in EthHandler");
        one2oneeumListener.removeListener(listener);
        onShutdown();
    }

    public void activate() {
        logger.debug("ETH protocol activated");
        one2oneeumListener.trace("ETH protocol activated");
        sendStatus();
    }

    protected void disconnect(ReasonCode reason) {
        msgQueue.disconnect(reason);
        channel.getNodeStatistics().nodeDisconnectedLocal(reason);
    }

    protected void sendMessage(EthMessage message) {
        msgQueue.sendMessage(message);
        channel.getNodeStatistics().ethOutbound.add();
    }

    public StatusMessage getHandshakeStatusMessage() {
        return channel.getNodeStatistics().getEthLastInboundStatusMsg();
    }

    public void setMsgQueue(MessageQueue msgQueue) {
        this.msgQueue = msgQueue;
    }

    public void setPeerDiscoveryMode(boolean peerDiscoveryMode) {
        this.peerDiscoveryMode = peerDiscoveryMode;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public EthVersion getVersion() {
        return version;
    }

}