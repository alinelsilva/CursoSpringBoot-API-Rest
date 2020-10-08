package br.com.alura.forum.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.alura.forum.model.Topico;

/*
 * Que não é uma boa prática retornar entidades JPA nos métodos dos controllers, 
 * sendo mais indicado retornar classes que seguem o padrão DTO (Data Transfer Object);
 */

public class TopicoDto {
	
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	
	public TopicoDto (Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.dataCriacao = topico.getDataCriacao();
    }
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public static List<TopicoDto> converter(List<Topico> topicos) {
		// irá converter o dado do topico para topicodto
		/*
		 *  Para cada um, ele vai chamar o construtor, passando o tópico como parâmetro. 
		 *  No final, tenho que transformar isso em uma lista, então vou encadear a chamada para o método collect.
		 */
		return topicos.stream().map(TopicoDto:: new ).collect(Collectors.toList());
	}
	
	
	

}
