package br.com.alura.forum.config.swagger;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alura.forum.model.Usuario;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurations {
	
	@Bean
	public Docket forumApi(){
		return new Docket(DocumentationType.SWAGGER_2) //tipo da documentação
				.select() 
				.apis(RequestHandlerSelectors.basePackage("br.com.alura.forum")) //qual pacote ira ler
				.paths(PathSelectors.ant("/**")) //qual endereço que é para fazer uma analise
				.build()
				.ignoredParameterTypes(Usuario.class) //irá ignorar a classe usuario
				.globalOperationParameters(Arrays.asList( //adiciona parametro padrão no swagger nos endpointers recebe uma lista com os parametros
						new ParameterBuilder()
						.name("Authorization")
						.description("Header para Token JWR")
						.modelRef(new ModelRef("string"))
						.parameterType("header")
						.required(false)
						.build()));
		
	}
}
