package com.nesscomputing.tracking;


import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.nesscomputing.logging.Log;
import com.nesscomputing.scopes.threaddelegate.servlet.ThreadDelegatingScopeFilter;
import com.nesscomputing.tracking.adapters.ServletApiAdapter;

/**
 * Tracking filter ensures that the tracking token springs into existence as early as possible (but it must be after the
 * ThreadDelegatingScopeFilter is run!). Also sets the MDC and the response header correctly.
 */
@Singleton
public class TrackingFilter implements Filter
{
    /** Name of the Tracking Header. The value is legacy. */
    public static final String X_NESS_TRACK = "X-Trumpet-Track";


    private static final Log LOG = Log.findLog();

    private final TrackingUUIDProvider trackingTokenProvider;
    private final Provider<TrackingToken> scopedProvider;

    @Inject
    TrackingFilter(final TrackingUUIDProvider trackingUUIDProvider,
                   final Provider<TrackingToken> scopedProvider)
    {
        this.trackingTokenProvider = trackingUUIDProvider;
        this.scopedProvider = scopedProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        LOG.info("Request tracking enabled.");
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        try {
            if (request.getAttribute(ThreadDelegatingScopeFilter.THREAD_DELEGATING_SCOPE_ACTIVE) == null) {
                LOG.warn("Called the tracking filter before the ThreadDelegatingScopeFilter. Tracking will not work. You need to reshuffle your guice modules to bind the filters in the right order!");
            }
            else {
                final TrackingToken trackingToken = scopedProvider.get();
                final UUID trackingUUID = trackingTokenProvider.get(new ServletApiAdapter(req));
                trackingToken.setValue(trackingUUID);

                if (trackingUUID != null) {
                    if (request != null) {
                        request.setAttribute(X_NESS_TRACK, trackingUUID);
                    }
                    if (res != null) {
                        res.setHeader(X_NESS_TRACK, trackingUUID.toString());
                    }
                }
            }
        }
        finally {
            chain.doFilter(request, response);
        }
    }

}


