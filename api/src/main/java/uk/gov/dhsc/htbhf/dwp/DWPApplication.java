package uk.gov.dhsc.htbhf.dwp;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.dhsc.htbhf.dwp.requestcontext.HeaderInterceptor;
import uk.gov.dhsc.htbhf.dwp.requestcontext.RequestContext;

@AllArgsConstructor
@SpringBootApplication
public class DWPApplication {

    private HeaderInterceptor headerInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(DWPApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        var restTemplate = new RestTemplate();
        var interceptors = restTemplate.getInterceptors();
        interceptors.add(headerInterceptor);
        return restTemplate;
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RequestContext requestContext() {
        return new RequestContext();
    }
}
