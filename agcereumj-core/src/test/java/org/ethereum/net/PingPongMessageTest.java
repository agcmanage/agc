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
package org.one2oneeum.net;

import org.one2oneeum.net.p2p.P2pMessageCodes;
import org.one2oneeum.net.p2p.PingMessage;
import org.one2oneeum.net.p2p.PongMessage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PingPongMessageTest {

    /* PING_MESSAGE & PONG_MESSAGE */

    @Test /* PingMessage */
    public void testPing() {

        PingMessage pingMessage = new PingMessage();
        System.out.println(pingMessage);

        assertEquals(PongMessage.class, pingMessage.getAnswerMessage());

        assertEquals(P2pMessageCodes.PING, pingMessage.getCommand());
    }

    @Test /* PongMessage */
    public void testPong() {

        PongMessage pongMessage = new PongMessage();
        System.out.println(pongMessage);

        assertEquals(P2pMessageCodes.PONG, pongMessage.getCommand());
        assertEquals(null, pongMessage.getAnswerMessage());
    }
}

