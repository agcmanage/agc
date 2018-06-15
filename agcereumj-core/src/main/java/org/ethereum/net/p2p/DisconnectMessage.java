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

import org.one2oneeum.net.message.ReasonCode;
import org.one2oneeum.util.RLP;
import org.one2oneeum.util.RLPList;

import static org.one2oneeum.net.message.ReasonCode.REQUESTED;
import static org.one2oneeum.net.message.ReasonCode.UNKNOWN;
import static org.one2oneeum.net.p2p.P2pMessageCodes.DISCONNECT;

/**
 * Wrapper around an one2oneeum Disconnect message on the network
 *
 * @see org.one2oneeum.net.p2p.P2pMessageCodes#DISCONNECT
 */
public class DisconnectMessage extends P2pMessage {

    private ReasonCode reason;

    public DisconnectMessage(byte[] encoded) {
        super(encoded);
    }

    public DisconnectMessage(ReasonCode reason) {
        this.reason = reason;
        parsed = true;
    }

    private void parse() {
        RLPList paramsList = (RLPList) RLP.decode2(encoded).get(0);

        if (paramsList.size() > 0) {
            byte[] reasonBytes = paramsList.get(0).getRLPData();
            if (reasonBytes == null)
                this.reason = UNKNOWN;
            else
                this.reason = ReasonCode.fromInt(reasonBytes[0]);
        } else {
            this.reason = UNKNOWN;
        }

        parsed = true;
    }

    private void encode() {
        byte[] encodedReason = RLP.encodeByte(this.reason.asByte());
        this.encoded = RLP.encodeList(encodedReason);
    }

    @Override
    public byte[] getEncoded() {
        if (encoded == null) encode();
        return encoded;
    }

    @Override
    public P2pMessageCodes getCommand() {
        return P2pMessageCodes.DISCONNECT;
    }

    @Override
    public Class<?> getAnswerMessage() {
        return null;
    }

    public ReasonCode getReason() {
        if (!parsed) parse();
        return reason;
    }

    public String toString() {
        if (!parsed) parse();
        return "[" + this.getCommand().name() + " reason=" + reason + "]";
    }
}