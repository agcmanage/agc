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

import org.one2oneeum.config.DefaultConfig;
import org.one2oneeum.config.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;


/**
 * @author Roman Mandeleil
 * @since 13.11.2014
 */
@Component
public class one2oneeumFactory {

    private static final Logger logger = LoggerFactory.getLogger("general");

    public static one2oneeum createone2oneeum() {
        return createone2oneeum((Class) null);
    }

    public static one2oneeum createone2oneeum(Class userSpringConfig) {
        return userSpringConfig == null ? createone2oneeum(new Class[] {DefaultConfig.class}) :
                createone2oneeum(DefaultConfig.class, userSpringConfig);
    }

    /**
     * @deprecated The config parameter is not used anymore. The configuration is passed
     * via 'systemProperties' bean either from the DefaultConfig or from supplied userSpringConfig
     * @param config  Not used
     * @param userSpringConfig   User Spring configuration class
     * @return  Fully initialized one2oneeum instance
     */
    public static one2oneeum createone2oneeum(SystemProperties config, Class userSpringConfig) {

        return userSpringConfig == null ? createone2oneeum(new Class[] {DefaultConfig.class}) :
                createone2oneeum(DefaultConfig.class, userSpringConfig);
    }

    public static one2oneeum createone2oneeum(Class ... springConfigs) {
        logger.info("Starting one2oneeumJ...");
        ApplicationContext context = new AnnotationConfigApplicationContext(springConfigs);

        return context.getBean(one2oneeum.class);
    }
}
