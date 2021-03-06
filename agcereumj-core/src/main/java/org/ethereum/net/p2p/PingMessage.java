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
package org.one2oneeum.net.p2p;

import org.spongycastle.util.encoders.Hex;

/**
 * Wrapper around an one2oneeum Ping message on the network
 *
 * @see org.one2oneeum.net.p2p.P2pMessageCodes#PING
 */
public class PingMessage extends P2pMessage {

    /**
     * Ping message is always a the same single command payload
     */
    private final static byte[] FIXED_PAYLOAD = Hex.decode("C0");

    public byte[] getEncoded() {
        return FIXED_PAYLOAD;
    }

    @Override
    public Class<PongMessage> getAnswerMessage() {
        return PongMessage.class;
    }

    @Override
    public P2pMessageCodes getCommand() {
        return P2pMessageCodes.PING;
    }

    @Override
    public String toString() {
        return "[" + getCommand().name() + "]";
    }
}