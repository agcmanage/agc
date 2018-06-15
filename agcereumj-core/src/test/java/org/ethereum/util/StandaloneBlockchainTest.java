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

import org.one2oneeum.config.SystemProperties;
import org.one2oneeum.crypto.ECKey;
import org.one2oneeum.util.blockchain.SolidityContract;
import org.one2oneeum.util.blockchain.StandaloneBlockchain;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;

import static org.one2oneeum.util.blockchain.one2oneUtil.Unit.one2one;
import static org.one2oneeum.util.blockchain.one2oneUtil.convert;

/**
 * Created by Anton Nashatyrev on 06.07.2016.
 */
public class StandaloneBlockchainTest {

    @AfterClass
    public static void cleanup() {
        SystemProperties.resetToDefault();
    }

    @Test
    public void constructorTest() {
        StandaloneBlockchain sb = new StandaloneBlockchain().withAutoblock(true);
        SolidityContract a = sb.submitNewContract(
                "contract A {" +
                        "  uint public a;" +
                        "  uint public b;" +
                        "  function A(uint a_, uint b_) {a = a_; b = b_; }" +
                        "}",
                "A", 555, 777
        );
        Assert.assertEquals(BigInteger.valueOf(555), a.callConstFunction("a")[0]);
        Assert.assertEquals(BigInteger.valueOf(777), a.callConstFunction("b")[0]);

        SolidityContract b = sb.submitNewContract(
                "contract A {" +
                        "  string public a;" +
                        "  uint public b;" +
                        "  function A(string a_, uint b_) {a = a_; b = b_; }" +
                        "}",
                "A", "This string is longer than 32 bytes...", 777
        );
        Assert.assertEquals("This string is longer than 32 bytes...", b.callConstFunction("a")[0]);
        Assert.assertEquals(BigInteger.valueOf(777), b.callConstFunction("b")[0]);
    }

    @Test
    public void fixedSizeArrayTest() {
        StandaloneBlockchain sb = new StandaloneBlockchain().withAutoblock(true);
        {
            SolidityContract a = sb.submitNewContract(
                    "contract A {" +
                            "  uint public a;" +
                            "  uint public b;" +
                            "  address public c;" +
                            "  address public d;" +
                            "  function f(uint[2] arr, address[2] arr2) {a = arr[0]; b = arr[1]; c = arr2[0]; d = arr2[1];}" +
                            "}");
            ECKey addr1 = new ECKey();
            ECKey addr2 = new ECKey();
            a.callFunction("f", new Integer[]{111, 222}, new byte[][] {addr1.getAddress(), addr2.getAddress()});
            Assert.assertEquals(BigInteger.valueOf(111), a.callConstFunction("a")[0]);
            Assert.assertEquals(BigInteger.valueOf(222), a.callConstFunction("b")[0]);
            Assert.assertArrayEquals(addr1.getAddress(), (byte[])a.callConstFunction("c")[0]);
            Assert.assertArrayEquals(addr2.getAddress(), (byte[])a.callConstFunction("d")[0]);
        }

        {
            ECKey addr1 = new ECKey();
            ECKey addr2 = new ECKey();
            SolidityContract a = sb.submitNewContract(
                    "contract A {" +
                            "  uint public a;" +
                            "  uint public b;" +
                            "  address public c;" +
                            "  address public d;" +
                            "  function A(uint[2] arr, address a1, address a2) {a = arr[0]; b = arr[1]; c = a1; d = a2;}" +
                            "}", "A",
                    new Integer[]{111, 222}, addr1.getAddress(), addr2.getAddress());
            Assert.assertEquals(BigInteger.valueOf(111), a.callConstFunction("a")[0]);
            Assert.assertEquals(BigInteger.valueOf(222), a.callConstFunction("b")[0]);
            Assert.assertArrayEquals(addr1.getAddress(), (byte[]) a.callConstFunction("c")[0]);
            Assert.assertArrayEquals(addr2.getAddress(), (byte[]) a.callConstFunction("d")[0]);

            String a1 = "0x1111111111111111111111111111111111111111";
            String a2 = "0x2222222222222222222222222222222222222222";
        }
    }

    @Test
    public void encodeTest1() {
        StandaloneBlockchain sb = new StandaloneBlockchain().withAutoblock(true);
        SolidityContract a = sb.submitNewContract(
                "contract A {" +
                        "  uint public a;" +
                        "  function f(uint a_) {a = a_;}" +
                        "}");
        a.callFunction("f", "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        BigInteger r = (BigInteger) a.callConstFunction("a")[0];
        System.out.println(r.toString(16));
        Assert.assertEquals(new BigInteger(Hex.decode("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")), r);
    }

    @Test
    public void invalidTxTest() {
        // check that invalid tx doesn't break implementation
        StandaloneBlockchain sb = new StandaloneBlockchain();
        ECKey alice = sb.getSender();
        ECKey bob = new ECKey();
        sb.sendone2one(bob.getAddress(), BigInteger.valueOf(1000));
        sb.setSender(bob);
        sb.sendone2one(alice.getAddress(), BigInteger.ONE);
        sb.setSender(alice);
        sb.sendone2one(bob.getAddress(), BigInteger.valueOf(2000));

        sb.createBlock();
    }

    @Test
    public void initBalanceTest() {
        // check StandaloneBlockchain.withAccountBalance method
        StandaloneBlockchain sb = new StandaloneBlockchain();
        ECKey alice = sb.getSender();
        ECKey bob = new ECKey();
        sb.withAccountBalance(bob.getAddress(), convert(123, one2one));

        BigInteger aliceInitBal = sb.getBlockchain().getRepository().getBalance(alice.getAddress());
        BigInteger bobInitBal = sb.getBlockchain().getRepository().getBalance(bob.getAddress());
        assert convert(123, one2one).equals(bobInitBal);

        sb.setSender(bob);
        sb.sendone2one(alice.getAddress(), BigInteger.ONE);

        sb.createBlock();

        assert convert(123, one2one).compareTo(sb.getBlockchain().getRepository().getBalance(bob.getAddress())) > 0;
        assert aliceInitBal.add(BigInteger.ONE).equals(sb.getBlockchain().getRepository().getBalance(alice.getAddress()));
    }

}
