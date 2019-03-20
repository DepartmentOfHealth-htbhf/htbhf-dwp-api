package uk.gov.dhsc.htbhf.dwp;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.gov.dhsc.htbhf.CommonRestConfiguration;

/**
 * The starting point for spring boot, this class enables SpringFox for documenting the api using swagger
 * and defines a number of beans.
 * See also: {@link ApiDocumentation}.
 */
@AllArgsConstructor
@SpringBootApplication
@EnableSwagger2
@Import(CommonRestConfiguration.class)
public class DWPApplication {

    public static void main(String[] args) {
        SpringApplication.run(DWPApplication.class, args);
    }

}
