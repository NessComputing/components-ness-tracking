package io.trumpet.tracking.guice;

import io.trumpet.tracking.AbstractTrackingTestCase;
import io.trumpet.tracking.TrackingToken;

import org.junit.Assert;
import org.junit.Test;

public class TestTrackingModule extends AbstractTrackingTestCase
{
    @Test
    public void testSimple()
    {
        Assert.assertNotNull(trackingTokenProvider);
        final TrackingToken token = trackingTokenProvider.get();
        Assert.assertNotNull(token);
    }
}
