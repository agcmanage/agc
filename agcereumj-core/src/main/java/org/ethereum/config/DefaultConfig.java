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
package org.one2oneeum.config;

import org.one2oneeum.datasource.*;
import org.one2oneeum.db.BlockStore;
import org.one2oneeum.db.IndexedBlockStore;
import org.one2oneeum.db.PruneManager;
import org.one2oneeum.db.TransactionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Roman Mandeleil
 * Created on: 27/01/2015 01:05
 */
@Configuration
@Import(CommonConfig.class)
public class DefaultConfig {
    private static Logger logger = LoggerFactory.getLogger("general");

    @Autowired
    ApplicationContext appCtx;

    @Autowired
    CommonConfig commonConfig;

    @Autowired
    SystemProperties config;

    public DefaultConfig() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> logger.error("Uncaught exception", e));
    }

    @Bean
    public BlockStore blockStore(){
        commonConfig.fastSyncCleanUp();
        IndexedBlockStore indexedBlockStore = new IndexedBlockStore();
        Source<byte[], byte[]> block = commonConfig.cachedDbSource("block");
        Source<byte[], byte[]> index = commonConfig.cachedDbSource("index");
        indexedBlockStore.init(index, block);

        return indexedBlockStore;
    }

    @Bean
    public TransactionStore transactionStore() {
        commonConfig.fastSyncCleanUp();
        return new TransactionStore(commonConfig.cachedDbSource("transactions"));
    }

    @Bean
    public PruneManager pruneManager() {
        if (config.databasePruneDepth() >= 0) {
            return new PruneManager((IndexedBlockStore) blockStore(), commonConfig.stateSource().getJournalSource(),
                    commonConfig.stateSource().getNoJournalSource(), config.databasePruneDepth());
        } else {
            return new PruneManager(null, null, null, -1); // dummy
        }
    }
}
