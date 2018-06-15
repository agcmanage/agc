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
package org.one2oneeum.samples;

import com.typesafe.config.ConfigFactory;
import org.one2oneeum.config.SystemProperties;
import org.one2oneeum.crypto.ECKey;
import org.one2oneeum.crypto.HashUtil;
import org.one2oneeum.facade.one2oneeum;
import org.one2oneeum.facade.one2oneeumFactory;
import org.springframework.context.annotation.Bean;

/**
 * This class just extends the BasicSample with the config which connect the peer to the Morden network
 * This class can be used as a base for free transactions testing
 * Everyone may use that 'cow' sender (which is effectively address aacc23ff079d96a5502b31fefcda87a6b3fbdcfb)
 * If you need more coins on this account just go to https://morden.one2one.camp/
 * and push 'Get Free one2one' button.
 *
 * Created by Anton Nashatyrev on 10.02.2016.
 */
public class MordenSample extends BasicSample {
    /**
     * Use that sender key to sign transactions
     */
    protected final byte[] senderPrivateKey = HashUtil.sha3("cow".getBytes());
    // sender address is derived from the private key aacc23ff079d96a5502b31fefcda87a6b3fbdcfb
    protected final byte[] senderAddress = ECKey.fromPrivate(senderPrivateKey).getAddress();

    protected abstract static class MordenSampleConfig {
        private final String config =
                "peer.discovery = {" +
                "    enabled = true \n" +
                "    ip.list = [" +
                "        '94.242.229.4:40404'," +
                "        '94.242.229.203:30303'" +
                "    ]" +
                "} \n" +
                "peer.p2p.eip8 = true \n" +
                "peer.networkId = 2 \n" +
                "sync.enabled = true \n" +
                "genesis = frontier-morden.json \n" +
                "blockchain.config.name = 'morden' \n" +
                "database.dir = mordenSampleDb";

        public abstract MordenSample sampleBean();

        @Bean
        public SystemProperties systemProperties() {
            SystemProperties props = new SystemProperties();
            props.overrideParams(ConfigFactory.parseString(config.replaceAll("'", "\"")));
            return props;
        }
    }

    public static void main(String[] args) throws Exception {
        sLogger.info("Starting one2oneeumJ!");

        class SampleConfig extends MordenSampleConfig {
            @Bean
            public MordenSample sampleBean() {
                return new MordenSample();
            }
        }

        one2oneeum one2oneeum = one2oneeumFactory.createone2oneeum(SampleConfig.class);
    }
}
