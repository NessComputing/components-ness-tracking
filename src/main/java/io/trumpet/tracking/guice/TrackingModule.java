package io.trumpet.tracking.guice;

import io.trumpet.tracking.TrackingFilter;
import io.trumpet.tracking.TrackingToken;
import io.trumpet.tracking.TrackingUUIDProvider;
import io.trumpet.tracking.adapters.HttpClientTrackingObserver;
import io.trumpet.tracking.config.TrackingConfig;
import io.trumpet.tracking.http.HttpRequestTrackingManager;
import io.trumpet.tracking.http.RequestTrackingData;

import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.ServletModule;
import com.nesscomputing.config.ConfigProvider;
import com.nesscomputing.httpclient.guice.HttpClientModule;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScope;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScopeModule;
import com.nesscomputing.scopes.threaddelegate.servlet.ThreadDelegatingScopeFilter;
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
