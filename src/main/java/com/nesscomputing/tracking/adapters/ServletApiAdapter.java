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
package com.nesscomputing.tracking.adapters;

import com.nesscomputing.logging.Log;
import com.nesscomputing.tracking.TrackingFilter;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Singleton;

/**
 * Implements {@link TrackingAdapter} for the Servlet API.
 *
 * This adapter looks for a request attribute (which could be set by an upstream filter) and if
 * this is not found, for a header on the incoming request.
 *
 * Uses Guice magic to get at the Request scoped HttpServletRequest.
 */
@Singleton
public class ServletApiAdapter implements TrackingAdapter
{
    private static final Log LOG = Log.findLog();

    private final HttpServletRequest req;

    public ServletApiAdapter(final HttpServletRequest req)
    {
        this.req = req;
    }

    @Override
    public UUID getTrackingUUID()
    {
        UUID trackingUUID = null;

        if (req != null) {
            // Do we have a request attribute?
            trackingUUID = (UUID) req.getAttribute(TrackingFilter.X_NESS_TRACK);

            if (trackingUUID == null) {
                // Or a tracking header?
                final String trackingHeader = req.getHeader(TrackingFilter.X_NESS_TRACK);

                if (trackingHeader != null) {
                    try {
                        trackingUUID = UUID.fromString(trackingHeader.trim());
                    }
                    catch (IllegalArgumentException iae) {
                        LOG.trace("Could not decode Tracking header '%s'", trackingHeader);
                        trackingUUID = null;
                    }
                }
            }
        }
        return trackingUUID;
    }
}
