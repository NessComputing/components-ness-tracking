package com.nesscomputing.tracking.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.net.URI;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.nesscomputing.config.Config;
import com.nesscomputing.tracking.TrackingFilter;

public class TestFilterForceNewToken extends AbstractTestTrackingFilter
{
    @Override
    protected Config getConfig()
    {
        return Config.getConfig(URI.create("classpath:/test-config"), "force-token");
    }

    @Test
    public void testBehaviour() throws Exception
    {
        req.addHeader(TrackingFilter.X_NESS_TRACK, value.toString());

        res.setHeader(EasyMock.eq(TrackingFilter.X_NESS_TRACK), EasyMock.anyObject(String.class));
        EasyMock.expectLastCall().once();
        mockSupport.replayAll();

        Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));

        checkServlet.setCallback(new TestCallback() {
            @Override
            public void callback(final ServletRequest filterReq, final ServletResponse filterRes) {
                Assert.assertThat(trackingTokenProvider.get(), is(notNullValue()));
                Assert.assertThat(trackingTokenProvider.get().getValue(), is(not(value)));
            }
        });

        guiceFilter.doFilter(req, res, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            }
        });

        Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));
    }


}
