package io.trumpet.tracking.adapters;

import io.trumpet.tracking.TrackingFilter;
import io.trumpet.tracking.TrackingToken;

import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nesscomputing.httpclient.HttpClientObserver;
import com.nesscomputing.httpclient.HttpClientRequest;
import com.nesscomputing.httpclient.HttpClientRequest.Builder;
import com.nesscomputing.logging.Log;

public class HttpClientTrackingObserver extends HttpClientObserver
{
    public static final String TRACK_PARAMETER_NAME = "_trumpet.track";

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

            builder.replaceHeader(TrackingFilter.X_TRUMPET_TRACK, trackingToken.toString());
            builder.setParameter(TRACK_PARAMETER_NAME, trackingToken);

            return builder.request();
        }
        else {
            LOG.trace("No tracking token found.");
            return request;
        }
    }
}
