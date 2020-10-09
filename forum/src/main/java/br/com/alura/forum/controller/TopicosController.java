package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
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
		if(nomeCurso == null) { //caso o nome for null irá trazer todos
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
			
		} else { //caso especificar um nome, irá trazer a lista que tem aquele nome
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
	
	/*
	 * detalhar um tópico especifico
	 */
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		//getOne sempre irá considerar que o id existe
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()) { //verifica se o id está presente no banco de dados
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}	
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}") //sobreescrever o recurso (algo que já está escrito)
	@Transactional //informar que é para commitar a nova alteração no banco.
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) { //verifica se o id está presente no banco de dados
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}	
		return ResponseEntity.notFound().build(); //irá retornar erro 404 caso não encontre nenhum id
	}
	
	@DeleteMapping("{id}") //deletar um topico especifico
	public ResponseEntity<?> remover(@PathVariable Long id){ //<?> generico sem especificar um
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) { 
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build(); //apenas irá retornar o retorno ok após o topico ser deletado
		}
		return ResponseEntity.notFound().build();
	}
}
