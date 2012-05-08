package com.nesscomputing.tracking.adapters;

import org.junit.Assert;
import org.junit.Before;

import com.google.common.collect.ImmutableSet;
import com.nesscomputing.httpclient.HttpClient;
import com.nesscomputing.httpclient.HttpClientDefaults;
import com.nesscomputing.httpclient.HttpClientRequest;
import com.nesscomputing.httpserver.testing.LocalHttpService;
import com.nesscomputing.testing.lessio.AllowNetworkAccess;
import com.nesscomputing.testing.lessio.AllowNetworkListen;
import com.nesscomputing.tracking.adapters.HttpClientTrackingObserver;

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

