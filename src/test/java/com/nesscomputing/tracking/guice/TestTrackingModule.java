package com.nesscomputing.tracking.guice;


import org.junit.Assert;
import org.junit.Test;

import com.nesscomputing.tracking.AbstractTrackingTestCase;
import com.nesscomputing.tracking.TrackingToken;

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
