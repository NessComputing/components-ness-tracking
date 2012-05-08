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
