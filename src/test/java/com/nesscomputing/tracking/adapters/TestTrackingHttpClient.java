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

import com.google.common.collect.ImmutableSet;

import com.nesscomputing.httpclient.HttpClient;
import com.nesscomputing.httpclient.HttpClientDefaults;
import com.nesscomputing.httpclient.HttpClientRequest;
import com.nesscomputing.httpserver.testing.LocalHttpService;

import org.junit.Assert;
import org.junit.Before;
import org.kitei.testing.lessio.AllowNetworkAccess;
import org.kitei.testing.lessio.AllowNetworkListen;

@AllowNetworkListen(ports={0})
@AllowNetworkAccess(endpoints={"127.0.0.1:*"})
public class TestTrackingHttpClient extends AbstractTestHttpClient
{
    @Before
    public void setup()
    {
        Assert.assertNull(localHttpService);
        Assert.assertNull(httpClient);

        localHttpService =  LocalHttpService.forHandler(testHandler);
        localHttpService.start();

        // Create a http client with the tracker installed.
        httpClient = new HttpClient(new HttpClientDefaults(), ImmutableSet.of(new HttpClientTrackingObserver(tokenProvider))).start();
    }

    @Override
    protected HttpClientRequest<String> getRequest()
    {
        final String uri = "http://" + localHttpService.getHost() + ":" + localHttpService.getPort() + "/data";
        return httpClient.get(uri, responseHandler).request();
    }
}

