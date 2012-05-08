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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nesscomputing.httpclient.HttpClient;
import com.nesscomputing.httpclient.HttpClientRequest;
import com.nesscomputing.httpclient.HttpClientResponseHandler;
import com.nesscomputing.httpclient.response.ContentResponseHandler;
import com.nesscomputing.httpserver.testing.LocalHttpService;
import com.nesscomputing.logging.Log;
import com.nesscomputing.testing.lessio.AllowNetworkAccess;


@AllowNetworkAccess(endpoints={"127.0.0.1:*"})
public abstract class AbstractTestHttpClient
{
    private static final Log log = Log.findLog();

    protected final HttpClientResponseHandler<String> responseHandler = new ContentResponseHandler<String>(new ResponseCodeCheckingConverter());

    protected GenericTrackingTestHandler testHandler = null;
    protected LocalHttpService localHttpService = null;
    protected HttpClient httpClient = null;

    protected final DummyTokenProvider tokenProvider = new DummyTokenProvider(null);

    @Before
    public void setupHandler()
    {
        testHandler = new GenericTrackingTestHandler();
        tokenProvider.setValue(null);
    }

    @After
    public void teardown()
    {
        Assert.assertNotNull(localHttpService);
        Assert.assertNotNull(testHandler);
        Assert.assertNotNull(httpClient);

        localHttpService.stop();
        localHttpService = null;
        testHandler = null;

        httpClient.close();
        httpClient = null;
    }

    protected abstract HttpClientRequest<String> getRequest();

    @Test
    public void testSimple() throws IOException
    {
        final String testString = "Ich bin zwei Oeltanks";
        testHandler.setContent(testString);
        testHandler.setContentType("text/plain");

        final HttpClientRequest<String> httpRequest = getRequest();
        final String response = httpRequest.perform();

        Assert.assertThat(response, is(testString));
        Assert.assertThat(testHandler.getToken(), is(nullValue()));
    }

    @Test
    public void testReuse() throws IOException
    {
        final String testString = "Ich bin zwei Oeltanks";
        testHandler.setContent(testString);
        testHandler.setContentType("text/plain");

        final HttpClientRequest<String> httpRequest = getRequest();

        final String response = httpRequest.perform();
        Assert.assertThat(testHandler.getToken(), is(nullValue()));

        Assert.assertThat(response, is(testString));

        final String response2 = httpRequest.perform();
        Assert.assertThat(response2, is(testString));
        Assert.assertThat(testHandler.getToken(), is(nullValue()));
    }

    @Test
    public void testTracking() throws IOException
    {
        final UUID trackingUUID = UUID.randomUUID();
        tokenProvider.setValue(trackingUUID);

        try {
            log.info("Sending Tracking Token: '%s'", trackingUUID);

            final String testString = "Ich bin zwei Oeltanks";
            testHandler.setContent(testString);
            testHandler.setContentType("text/plain");

            final HttpClientRequest<String> httpRequest = getRequest();
            final String response = httpRequest.perform();

            Assert.assertThat(response, is(testString));
            Assert.assertThat(testHandler.getToken(), is(trackingUUID));
        }
        finally {
            // trackingManager.clearTrackingToken(null);
        }

    }
}

