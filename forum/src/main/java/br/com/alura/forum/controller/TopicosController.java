package br.com.alura.forum.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.model.Curso;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.TopicoRepository;

@RestController //indica que sempre terá uma respota na página sem precisar criar um arquivo para navegar.
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@RequestMapping("/topicos")
//	@ResponseBody //navega pelo browser sem precisar criar uma página.html
	public List<TopicoDto> lista() {
		List<Topico> topicos = topicoRepository.findAll();
		
		//retorna uma lista em json.
		return TopicoDto.converter(topicos);
		// return Arrays.asList(topico, topico);
		
	}

}
