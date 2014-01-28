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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;

import com.nesscomputing.log4j.testing.RecordingAppender;
import com.nesscomputing.logging.Log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestLogging extends AbstractTrackingTestCase
{
    private RecordingAppender recordingAppender = null;

    private Log log = null;

    private MockedHttpServletRequest req = null;
    private HttpServletResponse res = null;

    private UUID value = null;

    @Before
    public void setup()
    {
        final URL log4jFile = TestLogging.class.getResource("/log4j.xml");
        DOMConfigurator.configure(log4jFile);
        final Logger root = Logger.getRootLogger();
        recordingAppender = new RecordingAppender("%X{track} - %m%n");
        root.addAppender(recordingAppender);

        log = Log.forName("tracker");

        req = new MockedHttpServletRequest();
        value = UUID.randomUUID();

        res = mockSupport.createMock(HttpServletResponse.class);
        res.setHeader(TrackingFilter.X_NESS_TRACK, value.toString());
        EasyMock.expectLastCall().once();

        mockSupport.replayAll();

    }

    @After
    public void teardown()
    {
        recordingAppender = null;
        log = null;
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
                bind(TrackingServlet.class).toInstance(new TrackingServlet());
                serve("/*").with(TrackingServlet.class);
            };
        };
    }

    @Test
    public void testLoggingFromHeader() throws Exception
    {
        Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));

        req.addHeader(TrackingFilter.X_NESS_TRACK, value.toString());

        guiceFilter.doFilter(req, res, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            }
        });

        Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));
    }

    @Test
    public void testLoggingFromContext() throws Exception
    {
        Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));

        req.setAttribute(TrackingFilter.X_NESS_TRACK, value);

        guiceFilter.doFilter(req, res, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            }
        });

        Assert.assertThat(trackingTokenProvider.get().getValue(), is(nullValue()));
    }

    public class TrackingServlet extends HttpServlet
    {
        private static final long serialVersionUID = 1L;

        @Override
        protected void service(HttpServletRequest filterReq, HttpServletResponse filterRes) throws ServletException, IOException
        {
            Assert.assertThat(trackingTokenProvider.get().getValue(), is(value));

            recordingAppender.clear();
            log.info("Hello, World");

            Assert.assertThat(recordingAppender.getContents(), is(value.toString() + " - Hello, World\n"));
            Assert.assertThat(recordingAppender.getLevel(), is(Level.INFO));
            Assert.assertThat(recordingAppender.getThrowable(), is(nullValue()));
        }
    }
}

