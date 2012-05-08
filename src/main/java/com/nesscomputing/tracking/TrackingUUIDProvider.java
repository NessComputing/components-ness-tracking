package com.nesscomputing.tracking;


import java.util.UUID;

import com.google.inject.Inject;
import com.nesscomputing.tracking.adapters.TrackingAdapter;
import com.nesscomputing.tracking.config.TrackingConfig;

/**
 * The provider for the tracking token.
 */
public class TrackingUUIDProvider
{
    private final TrackingConfig trackingConfig;

    @Inject
    public TrackingUUIDProvider(final TrackingConfig trackingConfig)
    {
        this.trackingConfig = trackingConfig;
    }

    public UUID get(final TrackingAdapter trackingAdapter)
    {
        UUID trackingUUID = null;

        //
        // If a new token is forced, generated one.
        if (trackingConfig.isForceNewToken()) {
            trackingUUID = UUID.randomUUID();
        }
        else {
            // Try to retrieve the token from the tracking adapter (i.e. the
            // http request. This assumes that this method only ever gets called
            // in http request scope.
            if (trackingAdapter != null) {
                trackingUUID = trackingAdapter.getTrackingUUID();
            }

            // We found no tracking token. Should we force a new one?
            if (trackingUUID == null && trackingConfig.isCreateNewToken()) {
                trackingUUID = UUID.randomUUID();
            }
        }
        return trackingUUID;
    }
}
