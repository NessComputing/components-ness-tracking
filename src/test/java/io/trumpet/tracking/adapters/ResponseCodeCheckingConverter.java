package io.trumpet.tracking.adapters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.nesscomputing.httpclient.HttpClientResponse;
import com.nesscomputing.httpclient.response.ContentConverter;


@Immutable
class ResponseCodeCheckingConverter implements ContentConverter<String>
{
    private final int responseCode;


    ResponseCodeCheckingConverter()
    {
        this(HttpServletResponse.SC_OK);
    }

    ResponseCodeCheckingConverter(final int responseCode)
    {
        this.responseCode = responseCode;
    }

    @Override
    public String convert(final HttpClientResponse response, final InputStream inputStream)
        throws IOException
    {
        Assert.assertThat(response.getStatusCode(), is(equalTo(responseCode)));
        return IOUtils.toString(inputStream);
    }

    @Override
    public String handleError(HttpClientResponse response, IOException ex) throws IOException
    {
        throw new IllegalStateException(ex);
    }
}
