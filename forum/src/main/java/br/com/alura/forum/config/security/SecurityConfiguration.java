package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * Habilitação de segurança
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	//configurar a parte de autenticação, controle de acesso
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	//	auth.userDetailsService() //qual classe que a autenticação
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
		//BCryptPasswordEncoder vai gerar um hash da senha
	}
	
	//configuração de autorização, quem pode acessar o que e qual url
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/topicos").permitAll()
		.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		.anyRequest().authenticated() //qualquer outra requisição precisar autenticar para acessar.
//		.and().formLogin(); //gera um formulário para fazer login padrão do spring
		.and().csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //não irá criar sessão
	}
	
	//configuração de recursos estáticos(requisição para arquivo de js,css, imagem etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	
//	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123456"));
//	}

}
