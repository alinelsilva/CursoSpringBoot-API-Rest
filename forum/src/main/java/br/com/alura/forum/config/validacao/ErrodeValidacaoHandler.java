package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //tratamento de erro
public class ErrodeValidacaoHandler {
	
	//ajuda a pegar a mensagem de erro
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) //retornará um status para o usuário
	//será chamado nos métodos automaticamente para fazer o tratamento de erro
	@ExceptionHandler(MethodArgumentNotValidException.class)
	//retornará uma lista de erro do formulário
	public List<ErroDeFormDto> handle(MethodArgumentNotValidException exception) {
		List<ErroDeFormDto> dto = new ArrayList<>();
		List<FieldError> fieldError = exception.getBindingResult().getFieldErrors();
		
		fieldError.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale()); //LocalContectHolder pega a mensagem conforme o idioma
			ErroDeFormDto erro = new ErroDeFormDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
		
	}

}
