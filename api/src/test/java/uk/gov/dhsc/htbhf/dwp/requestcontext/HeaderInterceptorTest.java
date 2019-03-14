package uk.gov.dhsc.htbhf.dwp.requestcontext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.dhsc.htbhf.dwp.requestcontext.RequestIdFilter.REQUEST_ID_HEADER;
import static uk.gov.dhsc.htbhf.dwp.requestcontext.RequestIdFilter.SESSION_ID_HEADER;

@ExtendWith(MockitoExtension.class)
public class HeaderInterceptorTest {

    private static final String SESSION_ID = UUID.randomUUID().toString();
    private static final String REQUEST_ID = UUID.randomUUID().toString();
    private final RequestContext requestContext =  new RequestContext();

    private HeaderInterceptor headerInterceptor = new HeaderInterceptor(requestContext);

    @BeforeEach
    public void setUp() {
        requestContext.setRequestId(REQUEST_ID);
        requestContext.setSessionId(SESSION_ID);
    }

    @Test
    public void shouldAddSessionAndRequestId() throws IOException {
        var httpRequest = mock(HttpRequest.class);
        var headers = mock(HttpHeaders.class);
        given(httpRequest.getHeaders()).willReturn(headers);
        var body = new byte[0];
        var execution = mock(ClientHttpRequestExecution.class);

        headerInterceptor.intercept(httpRequest, body, execution);

        verify(execution).execute(httpRequest, body);
        verify(headers).add(SESSION_ID_HEADER, SESSION_ID);
        verify(headers).add(REQUEST_ID_HEADER, REQUEST_ID);
    }
}
