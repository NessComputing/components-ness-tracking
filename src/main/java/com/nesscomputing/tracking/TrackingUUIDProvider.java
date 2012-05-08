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
