package io.trumpet.tracking.adapters;

import com.nesscomputing.logging.Log;
import io.trumpet.tracking.TrackingFilter;

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
            trackingUUID = (UUID) req.getAttribute(TrackingFilter.X_TRUMPET_TRACK);

            if (trackingUUID == null) {
                // Or a tracking header?
                final String trackingHeader = req.getHeader(TrackingFilter.X_TRUMPET_TRACK);

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
