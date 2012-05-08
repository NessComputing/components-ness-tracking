package com.nesscomputing.tracking.guice;


import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.ServletModule;
import com.nesscomputing.config.ConfigProvider;
import com.nesscomputing.httpclient.guice.HttpClientModule;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScope;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScopeModule;
import com.nesscomputing.scopes.threaddelegate.servlet.ThreadDelegatingScopeFilter;
import com.nesscomputing.tracking.TrackingFilter;
import com.nesscomputing.tracking.TrackingToken;
import com.nesscomputing.tracking.TrackingUUIDProvider;
import com.nesscomputing.tracking.adapters.HttpClientTrackingObserver;
import com.nesscomputing.tracking.config.TrackingConfig;
import com.nesscomputing.tracking.http.HttpRequestTrackingManager;
import com.nesscomputing.tracking.http.RequestTrackingData;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Add Tracking to a guicified web application. This now requires the {@link ThreadDelegatedScopeModule} to be installed, too. Tracking must be installed *after* that module, so that the
 * {@link ThreadDelegatingScopeFilter} runs before the TrackingFilter.
 */
public class TrackingModule extends ServletModule
{
	private final String pattern;

    /**
     * Track all requests.
     */
    public TrackingModule()
    {
        this("/*");
    }

    /**
     * Specify the pattern of requests to track.
     */
    public TrackingModule(final String pattern)
    {
    	this.pattern = pattern;
    }

    @Override
    public void configureServlets()
    {
        // Tracking token setup
        bind (TrackingConfig.class).toProvider(ConfigProvider.of(TrackingConfig.class)).in(Scopes.SINGLETON);
        bind (TrackingUUIDProvider.class).in(Scopes.SINGLETON);

        // Bind the TrackingToken in the right scope.
        bind(TrackingToken.class).in(ThreadDelegatedScope.SCOPE);

        // Hook up tracking token to the HttpClient.
        HttpClientModule.bindNewObserver(binder()).to(HttpClientTrackingObserver.class);

        // Request tracking
        bind (HttpRequestTrackingManager.class).in(Scopes.SINGLETON);

        bind (RequestTrackingData.class).in(ThreadDelegatedScope.SCOPE);
        binder().requestStaticInjection(RequestTrackingData.class);

        // Hook up request tracking to the HttpClient.
        HttpClientModule.bindNewObserver(binder()).to(HttpRequestTrackingManager.class);

        // Hook up request tracking to Jersey.
        Multibinder.newSetBinder(binder(), ContainerRequestFilter.class).addBinding().to(HttpRequestTrackingManager.class);
        Multibinder.newSetBinder(binder(), ContainerResponseFilter.class).addBinding().to(HttpRequestTrackingManager.class);

        bind(TrackingFilter.class).in(Scopes.SINGLETON);
        filter(pattern).through(TrackingFilter.class);
    }
}
