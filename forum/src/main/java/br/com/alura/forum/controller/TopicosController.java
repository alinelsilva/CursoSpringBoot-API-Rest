package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController //indica que sempre terá uma respota na página sem precisar criar um arquivo para navegar.
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
//	@ResponseBody //navega pelo browser sem precisar criar uma página.html
	public List<TopicoDto> lista(String nomeCurso) {
//		System.out.println(nomeCurso);
		if(nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
			
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
		//retorna uma lista em json.
		// return Arrays.asList(topico, topico);
	}
	
	@PostMapping
	//ResponseEntity. Esse generic é o tipo de objeto que vou devolver no corpo da resposta, 
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){ //pega a requisição do corpo
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();  //toUri() converte e transforma na url completa
		//retorno será um 201 informando que deu certo a requisição
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
}
