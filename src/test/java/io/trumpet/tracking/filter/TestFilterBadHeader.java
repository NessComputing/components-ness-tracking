package io.trumpet.tracking.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import io.trumpet.tracking.TrackingFilter;

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

public class TestFilterBadHeader extends AbstractTestTrackingFilter
{
    @Override
    protected Config getConfig()
    {
        return Config.getConfig(URI.create("classpath:/test-config"), "new-token");
    }

    @Test
    public void testBehaviour() throws Exception
    {
        req.addHeader(TrackingFilter.X_TRUMPET_TRACK, "NOT A VALID UUID!");
        res.setHeader(EasyMock.eq(TrackingFilter.X_TRUMPET_TRACK), EasyMock.anyObject(String.class));
        EasyMock.expectLastCall().once();
        mockSupport.replayAll();
        Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));

        checkServlet.setCallback(new TestCallback() {
            @Override
            public void callback(final ServletRequest filterReq, final ServletResponse filterRes) {
                Assert.assertThat(trackingTokenProvider.get(), is(notNullValue()));
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
