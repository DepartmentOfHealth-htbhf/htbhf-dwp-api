package uk.gov.dhsc.htbhf.dwp;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.gov.dhsc.htbhf.CommonRestConfiguration;
import uk.gov.dhsc.htbhf.database.CloudDBConfiguration;
import uk.gov.dhsc.htbhf.dwp.controller.DwpEligibilityRequestResolver;
import uk.gov.dhsc.htbhf.dwp.converter.RequestHeaderToDWPEligibilityRequestConverter;
import uk.gov.dhsc.htbhf.dwp.http.GetRequestBuilder;

import java.util.List;

/**
 * The starting point for spring boot, this class enables SpringFox for documenting the api using swagger
 * and defines a number of beans.
 * See also: {@link ApiDocumentation}.
 */
@AllArgsConstructor
@SpringBootApplication
@EnableSwagger2
@Import({CommonRestConfiguration.class, CloudDBConfiguration.class})
public class DWPApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(DWPApplication.class, args);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        RequestHeaderToDWPEligibilityRequestConverter converter = new RequestHeaderToDWPEligibilityRequestConverter();
        argumentResolvers.add(new DwpEligibilityRequestResolver(converter));
    }

    @Bean
    public GetRequestBuilder getRequestBuilder() {
        return new GetRequestBuilder();
    }

}
