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
