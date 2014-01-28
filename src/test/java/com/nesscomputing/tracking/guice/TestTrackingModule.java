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
package com.nesscomputing.tracking.guice;


import com.nesscomputing.tracking.AbstractTrackingTestCase;
import com.nesscomputing.tracking.TrackingToken;

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
