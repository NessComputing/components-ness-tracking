package com.nesscomputing.tracking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.input.NullInputStream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;

public class MockedHttpServletRequest implements HttpServletRequest
{
    private Map<String, Object> attributes = Maps.newHashMap();
    private Map<String, List<String>> parameters = Maps.newHashMap();
    private Map<String, List<String>> headers = Maps.newHashMap();

    public void addHeader(final String name, final String value)
    {
        List<String> values = headers.get(name);
        if (values == null) {
            values = Lists.newArrayList();
            headers.put(name, values);
        }
        values.add(value);
    }

    @Override
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames()
    {
        return Iterators.asEnumeration(attributes.keySet().iterator());
    }

    @Override
    public String getCharacterEncoding()
    {
        return "UTF-8";
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException
    {
    }

    @Override
    public int getContentLength()
    {
        return 0;
    }

    @Override
    public String getContentType()
    {
        return "text/plain";
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        final InputStream stream = new NullInputStream(getContentLength());
        return new ServletInputStream() {

            @Override
            public int read() throws IOException
            {
                return stream.read();
            }

        };
    }

    @Override
    public String getParameter(String name)
    {
        final List<String> value = parameters.get(name);
        return value == null ? null : value.get(0);
    }

    @Override
    public Enumeration<String> getParameterNames()
    {
        return Iterators.asEnumeration(parameters.keySet().iterator());
    }

    @Override
    public String[] getParameterValues(String name)
    {
        final List<String> values = parameters.get(name);
        return values == null ? null : values.toArray(new String[values.size()]);
    }

    @Override
    public Map<String, String[]> getParameterMap()
    {
        return Maps.transformEntries(parameters, new EntryTransformer<String, List<String>, String []>() {

            @Override
            public String[] transformEntry(String key, List<String> values)
            {
                return values == null ? null : values.toArray(new String[values.size()]);
            }
        });
    }

    @Override
    public String getProtocol()
    {
        return "HTTP/1.1";
    }

    @Override
    public String getScheme()
    {
        return "http";
    }

    @Override
    public String getServerName()
    {
        return "localhost";
    }

    @Override
    public int getServerPort()
    {
        return 80;
    }

    @Override
    public BufferedReader getReader() throws IOException
    {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public String getRemoteAddr()
    {
        return "127.0.0.1";
    }

    @Override
    public String getRemoteHost()
    {
        return "localhost";
    }

    @Override
    public void setAttribute(String name, Object o)
    {
        attributes.put(name, o);
    }

    @Override
    public void removeAttribute(String name)
    {
        attributes.remove(name);
    }

    @Override
    public Locale getLocale()
    {
        return Locale.ENGLISH;
    }

    @Override
    public Enumeration<Locale> getLocales()
    {
        return Iterators.asEnumeration(ImmutableSet.of(getLocale()).iterator());
    }

    @Override
    public boolean isSecure()
    {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRealPath(String path)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRemotePort()
    {
        return 12345;
    }

    @Override
    public String getLocalName()
    {
        return "localhost";
    }

    @Override
    public String getLocalAddr()
    {
        return "127.0.0.1";
    }

    @Override
    public int getLocalPort()
    {
        return 80;
    }

    @Override
    public String getAuthType()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie[] getCookies()
    {
        return new Cookie [0];
    }

    @Override
    public long getDateHeader(String name)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHeader(String name)
    {
        final List<String> headerValues = headers.get(name);
        return headerValues == null ? null : headerValues.get(0);
    }

    @Override
    public Enumeration<String> getHeaders(String name)
    {
        final List<String> headerValues = headers.get(name);
        return headerValues == null ? null : Iterators.asEnumeration(headerValues.iterator());
    }

    @Override
    public Enumeration<String> getHeaderNames()
    {
        return Iterators.asEnumeration(headers.keySet().iterator());
    }

    @Override
    public int getIntHeader(String name)
    {
        final String header = getHeader(name);
        return header == null ? null : Integer.parseInt(header);
    }

    @Override
    public String getMethod()
    {
        return "GET";
    }

    @Override
    public String getPathInfo()
    {
        return "";
    }

    @Override
    public String getPathTranslated()
    {
        return "/foo";
    }

    @Override
    public String getContextPath()
    {
        return "";
    }

    @Override
    public String getQueryString()
    {
        return "";
    }

    @Override
    public String getRemoteUser()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUserInRole(String role)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Principal getUserPrincipal()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestedSessionId()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestURI()
    {
        return "/foo";
    }

    @Override
    public StringBuffer getRequestURL()
    {
        return new StringBuffer(getRequestURI());
    }

    @Override
    public String getServletPath()
    {
        return "/foo";
    }

    @Override
    public HttpSession getSession(boolean create)
    {
        if (create) {
            throw new UnsupportedOperationException();
        }
        else {
            return null;
        }
    }

    @Override
    public HttpSession getSession()
    {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    @Override
    public ServletContext getServletContext()
    {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException
    {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException
    {
        return null;
    }

    @Override
    public boolean isAsyncStarted()
    {
        return false;
    }

    @Override
    public boolean isAsyncSupported()
    {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext()
    {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType()
    {
        return null;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException
    {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException
    {
    }

    @Override
    public void logout() throws ServletException
    {
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException
    {
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException
    {
        return null;
    }
}
