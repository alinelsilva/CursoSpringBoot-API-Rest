package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.h2.security.auth.AuthConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDTO;
import br.com.alura.forum.controller.form.LoginForm;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	//será solicitado pelo usuário quando for  logar
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDTO> autenticar(@RequestBody @Valid LoginForm form){
//		System.out.println(form.getEmail());
//		System.out.println(form.getSenha());
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		
		try {
			/*
			 * Quando o Spring chamar essa linha, do authManager.authenticate, o Spring vai olhar as 
			 * configurações que fizemos e ele sabe que é para chamar a classe authentication services, 
			 * que chama o usuário repository para consultar os dados do banco de dados
			 */
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authentication);
//			System.out.println(token);
			return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
			
		} catch (AuthConfigException e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
