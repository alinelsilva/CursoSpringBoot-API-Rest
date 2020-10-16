package br.com.alura.forum.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
 * Habilitação de segurança
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{
	
	//configurar a parte de autenticação, controle de acesso
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	}
	
	//configuração de autorização, quem pode acessar o que e qual url
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/topicos").permitAll()
		.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
		.anyRequest().authenticated(); //qualquer outra requisição precisar autenticar para acessar.
	}
	////
	//configuração de recursos estáticos(requisição para arquivo de js,css, imagem etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	

}
