package uk.gov.dhsc.htbhf.dwp.requestcontext;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static uk.gov.dhsc.htbhf.dwp.requestcontext.RequestIdFilter.REQUEST_ID_HEADER;
import static uk.gov.dhsc.htbhf.dwp.requestcontext.RequestIdFilter.SESSION_ID_HEADER;

@Component
@AllArgsConstructor
public class HeaderInterceptor implements ClientHttpRequestInterceptor {

    private final RequestContext requestContext;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var headers = request.getHeaders();
        headers.add(REQUEST_ID_HEADER, requestContext.getRequestId());
        headers.add(SESSION_ID_HEADER, requestContext.getSessionId());
        return execution.execute(request, body);
    }
}
