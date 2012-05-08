package com.nesscomputing.tracking.http;


import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.nesscomputing.httpclient.HttpClientObserver;
import com.nesscomputing.httpclient.HttpClientRequest;
import com.nesscomputing.httpclient.HttpClientResponse;
import com.nesscomputing.logging.Log;
import com.nesscomputing.tracking.config.TrackingConfig;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

@Singleton
public class HttpRequestTrackingManager extends HttpClientObserver implements ContainerRequestFilter, ContainerResponseFilter {
    public static final String DEBUG_HEADER = "X-Ness-Debug";
    private static final Log LOG = Log.findLog();
    private final Provider<RequestTrackingData> trackingProvider;
    private final boolean enabled;

    private ObjectMapper mapper = null;

    @Inject
    HttpRequestTrackingManager(Provider<RequestTrackingData> trackingProvider, TrackingConfig config) {
        this.trackingProvider = trackingProvider;
        this.enabled = config.isTrackingEnabled();
    }

    @Inject(optional = true)
    void injectObjectMapper(final ObjectMapper mapper)
    {
        this.mapper = mapper;
    }

    @Override
    public ContainerRequest filter(ContainerRequest req) {
        if (!enabled) {
            return req;
        }

        RequestTrackingData data = trackingProvider.get();
        data.setRequestStart(System.currentTimeMillis());
        if (req.getHeaderValue(DEBUG_HEADER) == null) {
            return req;
        }
        data.setRequestUri(req.getRequestUri());
        return req;
    }

    @Override
    public <T> HttpClientRequest<T> onRequestSubmitted(HttpClientRequest<T> request) {
        if (!enabled) {
            return request;
        }

        RequestTrackingData data = trackingProvider.get();
        if (data.getRequestStart() == null) {
            LOG.trace(new Throwable(), "RequestTrackingData not bound to an actual servlet request. " +
            		"Most likely cause is that a servlet started work on another thread " +
            		"without forwarding the tracked request scope.  Use a TrackedExecutor.");
            return request;
        }
        // begin() sets this only if X-Ness-Debug is set
        if (data.getRequestUri() != null) {
            HttpClientRequest.Builder<T> newBuilder = HttpClientRequest.Builder.fromRequest(request);
            newBuilder.addHeader(DEBUG_HEADER, "true");
            return newBuilder.request();
        }
        return request;
    }

    @Override
    public HttpClientResponse onResponseReceived(HttpClientResponse response) {
        if (!enabled || mapper == null) {
            return response;
        }

        RequestTrackingData data = trackingProvider.get();
        if (data.getRequestStart() == null) {
            LOG.trace(new Throwable(), "RequestTrackingData not bound to an actual servlet request. " +
            		"Most likely cause is that a servlet started work on another thread " +
            		"without forwarding the tracked request scope.  Use a TrackedExecutor.");
            return response;
        }

        String debugData = response.getHeader(DEBUG_HEADER);
        // begin() sets requestUri only if X-Ness-Debug is set
        if (data.getRequestUri() == null) {
            return response;
        }

        data.setResponseCode(response.getStatusCode());
        try {
            RequestTrackingData remoteData;
            if (debugData != null) {
                remoteData = mapper.readValue(debugData, RequestTrackingData.class);
            } else {
                remoteData = new RequestTrackingData();
                remoteData.setRequestUri(response.getUri());
            }
            data.addRequest(remoteData);
        } catch (IOException e) {
            LOG.error(e, "Could not read debug data \"%s\"", debugData);
        }
        return response;
    }

    @Override
    public ContainerResponse filter(ContainerRequest req, ContainerResponse res) {
        if ( (!enabled) || mapper == null || req.getHeaderValue(DEBUG_HEADER) == null) {
            return res;
        }

        RequestTrackingData data = trackingProvider.get();
        data.setResponseEnd(System.currentTimeMillis());
        if (res.isCommitted()) {
            LOG.error("Tracking requested, but response committed before I could send the data out.  Losing your debug data: %s", data);
        } else {
            try {
                res.getHttpHeaders().add(DEBUG_HEADER, mapper.writeValueAsString(data));
            } catch (IOException e) {
                LOG.error(e, "Failed to write debug response %s", data);
            }
        }
        return res;
    }
}
