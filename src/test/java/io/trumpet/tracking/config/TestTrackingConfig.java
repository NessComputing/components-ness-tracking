package io.trumpet.tracking.config;

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
        props.put("trumpet.tracking.forceNewToken", "true");
        props.put("trumpet.tracking.createNewToken", "false");
        props.put("ness.tracking.http-tracking-debug.enabled", "false");
        final ConfigurationObjectFactory factory = new ConfigurationObjectFactory(new SimplePropertyConfigSource(props));
        final TrackingConfig config = factory.build(TrackingConfig.class);
        Assert.assertTrue(config.isForceNewToken());
        Assert.assertFalse(config.isCreateNewToken());
        Assert.assertFalse(config.isTrackingEnabled());
    }
}



