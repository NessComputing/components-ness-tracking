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