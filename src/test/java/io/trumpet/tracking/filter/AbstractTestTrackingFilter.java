package io.trumpet.tracking.filter;

import io.trumpet.tracking.AbstractTrackingTestCase;
import io.trumpet.tracking.MockedHttpServletRequest;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import com.nesscomputing.config.Config;


public abstract class AbstractTestTrackingFilter extends AbstractTrackingTestCase
{
    protected MockedHttpServletRequest req = null;
    protected HttpServletResponse res = null;

    protected UUID value = null;

    @Inject
    protected CheckServlet checkServlet = null;

    @Before
    public void setup()
    {
        req = new MockedHttpServletRequest();
        value = UUID.randomUUID();

        res = mockSupport.createMock(HttpServletResponse.class);
    }

    @After
    public void teardown()
    {
        req = null;
        res = null;
        value = null;
    }

    @Override
    public Module getTestModule()
    {
        return new ServletModule() {
            @Override
            public void configureServlets() {
                bind(CheckServlet.class).toInstance(new CheckServlet());
                serve("/*").with(CheckServlet.class);
            };
        };
    }

    @Override
    protected abstract Config getConfig();

    public static interface TestCallback
    {
        void callback(ServletRequest filterReq, ServletResponse filterRes);
    }

    public class CheckServlet extends HttpServlet
    {
        private static final long serialVersionUID = 1L;

        private TestCallback callback;

        public void setCallback(TestCallback callback)
        {
            this.callback = callback;
        }

        @Override
        protected void service(HttpServletRequest filterReq, HttpServletResponse filterRes) throws ServletException, IOException
        {
            callback.callback(filterReq, filterRes);
        }
    }
}
