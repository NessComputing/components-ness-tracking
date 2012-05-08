package io.trumpet.tracking.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.net.URI;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Assert;
import org.junit.Test;

import com.nesscomputing.config.Config;

/**
 * Tests that for a totally passive (i.e. not creating any token) configuration, no token is created.
 */
public class TestFilterENG1892 extends AbstractTestTrackingFilter
{

    @Override
    protected Config getConfig()
    {
        return Config.getConfig(URI.create("classpath:/test-config"), "passive");
    }

    @Test
    public void testBehaviour() throws Exception
    {
        mockSupport.replayAll();
        Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));

        checkServlet.setCallback(new TestCallback() {
            @Override
            public void callback(final ServletRequest filterReq, final ServletResponse filterRes) {
                Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));
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
