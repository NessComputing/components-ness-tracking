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
package com.nesscomputing.tracking.filter;


import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;

import com.nesscomputing.config.Config;
import com.nesscomputing.tracking.AbstractTrackingTestCase;
import com.nesscomputing.tracking.MockedHttpServletRequest;

import org.junit.After;
import org.junit.Before;


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
