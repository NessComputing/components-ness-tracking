/**
 * Copyright (C) 2012 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nesscomputing.tracking.config;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.skife.config.ConfigurationObjectFactory;
import org.skife.config.SimplePropertyConfigSource;

public class TestTrackingConfig
{
    @Test
    public void testPassive()
    {
        Assert.assertFalse(TrackingConfig.PASSIVE.isForceNewToken());
        Assert.assertFalse(TrackingConfig.PASSIVE.isCreateNewToken());
    }

    @Test
    public void testForce()
    {
        Assert.assertTrue(TrackingConfig.FORCE.isForceNewToken());
        Assert.assertFalse(TrackingConfig.FORCE.isCreateNewToken());
    }

    @Test
    public void testDefault()
    {
        final TrackingConfig config = new TrackingConfig();
        Assert.assertFalse(config.isForceNewToken());
        Assert.assertTrue(config.isCreateNewToken());
    }

    @Test
    public void testConfigMagicDefault()
    {
        final Properties props = new Properties();
        final ConfigurationObjectFactory factory = new ConfigurationObjectFactory(new SimplePropertyConfigSource(props));
        final TrackingConfig config = factory.build(TrackingConfig.class);
        Assert.assertFalse(config.isForceNewToken());
        Assert.assertTrue(config.isCreateNewToken());
        Assert.assertTrue(config.isTrackingEnabled());
    }

    @Test
    public void testConfigMagicValues()
    {
        final Properties props = new Properties();
        props.put("ness.tracking.forceNewToken", "true");
        props.put("ness.tracking.createNewToken", "false");
        props.put("ness.tracking.http-tracking-debug.enabled", "false");
        final ConfigurationObjectFactory factory = new ConfigurationObjectFactory(new SimplePropertyConfigSource(props));
        final TrackingConfig config = factory.build(TrackingConfig.class);
        Assert.assertTrue(config.isForceNewToken());
        Assert.assertFalse(config.isCreateNewToken());
        Assert.assertFalse(config.isTrackingEnabled());
    }
}



