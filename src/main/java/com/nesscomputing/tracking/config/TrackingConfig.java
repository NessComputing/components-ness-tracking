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

import org.skife.config.Config;
import org.skife.config.Default;

/**
 * Configuration for Ness tracking.
 */
public class TrackingConfig
{
    public static final TrackingConfig PASSIVE = new TrackingConfig(false, false);
    public static final TrackingConfig FORCE = new TrackingConfig(true, false);

    private final boolean forceNewToken;
    private final boolean createNewToken;

    /**
     * C'tor for config magic.
     */
    public TrackingConfig()
    {
        this(false, true);
    }

    public TrackingConfig(boolean forceNewToken, boolean createNewToken)
    {
        this.forceNewToken = forceNewToken;
        this.createNewToken = createNewToken;
    }

    @Config("ness.tracking.forceNewToken")
    @Default("false")
    public boolean isForceNewToken()
    {
        return forceNewToken;
    }

    @Config("ness.tracking.createNewToken")
    @Default("true")
    public boolean isCreateNewToken()
    {
        return createNewToken;
    }

    @Config("ness.tracking.http-tracking-debug.enabled")
    @Default("true")
    public boolean isTrackingEnabled()
    {
        return true;
    }

    @Config("ness.tracking.maxRequestBranchesTracked")
    @Default("100")
    public int maxRequestBranchesTracked()
    {
    	return 100;
    }
}