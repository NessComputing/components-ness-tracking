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


import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nesscomputing.httpclient.HttpClientObserver;
import com.nesscomputing.httpclient.HttpClientRequest;
import com.nesscomputing.httpclient.HttpClientRequest.Builder;
import com.nesscomputing.logging.Log;
import com.nesscomputing.tracking.TrackingFilter;
import com.nesscomputing.tracking.TrackingToken;

public class HttpClientTrackingObserver extends HttpClientObserver
{
    public static final String TRACK_PARAMETER_NAME = "_ness.track";

    private static final Log LOG = Log.findLog();

    private final Provider<TrackingToken> trackingTokenProvider;

    @Inject
    HttpClientTrackingObserver(final Provider<TrackingToken> trackingTokenProvider)
    {
        this.trackingTokenProvider = trackingTokenProvider;
    }

    @Override
    public <RequestType> HttpClientRequest<RequestType> onRequestSubmitted(final HttpClientRequest<RequestType> request)
    {
        final UUID trackingToken = trackingTokenProvider.get().getValue();

        if (trackingToken != null) {
            LOG.trace("Add Tracking Token '%s' to request", trackingToken);

            final Builder<RequestType> builder = HttpClientRequest.Builder.fromRequest(request);

            builder.replaceHeader(TrackingFilter.X_NESS_TRACK, trackingToken.toString());
            builder.setParameter(TRACK_PARAMETER_NAME, trackingToken);

            return builder.request();
        }
        else {
            LOG.trace("No tracking token found.");
            return request;
        }
    }
}
