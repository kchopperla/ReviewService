package com.intuit.reviewengine;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(paths())
				.build()
				.directModelSubstitute(XMLGregorianCalendar.class, Date.class)
				.apiInfo(apiInfo())
				.pathProvider(new CustomPathProvider());
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Review Engine")
				.description("Review Engine services")
				.version("1.0.0")
				.contact(new Contact(" ABC Department", "", "test@email.com"))
				.build();
	}

	private Predicate<String> paths() {
		return Predicates.or(
				PathSelectors.regex("/v1.*")
				);
	}

	private class CustomPathProvider extends AbstractPathProvider {

		@Override
		protected String applicationPath() {
			return "";
		}

		@Override
		protected String getDocumentationPath() {
			return "/";
		}

	}  

}