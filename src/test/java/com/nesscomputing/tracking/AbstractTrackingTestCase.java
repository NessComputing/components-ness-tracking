package com.nesscomputing.tracking;


import java.net.URI;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.nesscomputing.config.Config;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScopeModule;
import com.nesscomputing.tracking.TrackingToken;
import com.nesscomputing.tracking.guice.TrackingModule;

public abstract class AbstractTrackingTestCase
{
    protected EasyMockSupport mockSupport;

    protected static final Key<TrackingToken> tokenKey = Key.get(TrackingToken.class);

    @Inject
    protected GuiceFilter guiceFilter;

    @Inject
    protected Provider<TrackingToken> trackingTokenProvider;

    @Inject
    protected Injector injector;

    @Before
    public void setUpTracking() throws Exception
    {
        mockSupport = new EasyMockSupport();

        final Config config = getConfig();
        final Injector injector = Guice.createInjector(Stage.PRODUCTION,
                                                       new ThreadDelegatedScopeModule(),
                                                       new TrackingModule(),
                                                       getTestModule(),
                                                       new AbstractModule() {
                                                           @Override
                                                           public void configure() {
                                                               binder().requireExplicitBindings();
                                                               binder().disableCircularProxies();

                                                               bind (Config.class).toInstance(config);
                                                               bind(GuiceFilter.class).in(Scopes.SINGLETON);
                                                           }
                                                       });

        injector.injectMembers(this);
    }

    @After
    public void tearDownTracking()
    {
        Assert.assertNotNull(guiceFilter);
        guiceFilter.destroy();

        Assert.assertNotNull(injector);
        injector = null;

        mockSupport.verifyAll();
    }

    protected Module getTestModule()
    {
        return new Module() {

            @Override
            public void configure(Binder binder) {
            }

        };
    }

    protected Config getConfig()
    {
        return Config.getConfig(URI.create("classpath:/test-config"), "tracking");
    }
}
