package br.com.alura.forum.config.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.model.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

//autenticação via token
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UsuarioRepository usuarioRepository;
	
	//esse construtor é para poder injetar o autowired na classe securityConfiguration
	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// ira recuperar o token e verificar se o mesmo é valido para liberar acesso
		String token = recuperarToken(request);
//		System.out.println(token);
		boolean valido = tokenService.isTokenValido(token);
		if(valido) {
			autenticarCliente(token);
		}
		filterChain.doFilter(request, response);
		
	}

	private void autenticarCliente(String token) {
		Long idUsuario = tokenService.getIdUsuario(token);
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication); //irá considerar se está autenticado
	}

	private String recuperarToken(HttpServletRequest request) {
		//ira pegar o token
		String token = request.getHeader("Authorization"); //irá pegar o cabeçalho
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
		
	}
	
	

}
