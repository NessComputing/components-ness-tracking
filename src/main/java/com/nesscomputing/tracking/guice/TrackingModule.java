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
package com.nesscomputing.tracking.guice;


import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

import com.nesscomputing.config.ConfigProvider;
import com.nesscomputing.httpclient.HttpClientObserverGroup;
import com.nesscomputing.httpclient.guice.HttpClientModule;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScope;
import com.nesscomputing.tracking.TrackingFilter;
import com.nesscomputing.tracking.TrackingToken;
import com.nesscomputing.tracking.TrackingUUIDProvider;
import com.nesscomputing.tracking.adapters.HttpClientTrackingObserver;
import com.nesscomputing.tracking.config.TrackingConfig;

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
        HttpClientModule.bindNewObserver(binder(), HttpClientObserverGroup.PLATFORM_INTERNAL).to(HttpClientTrackingObserver.class);

        bind(TrackingFilter.class).in(Scopes.SINGLETON);
        filter(pattern).through(TrackingFilter.class);
    }
}
