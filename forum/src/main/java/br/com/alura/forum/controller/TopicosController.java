package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	//anotação para guardar o cache
	@Cacheable(value = "listaDeTopicos")
//	@ResponseBody //navega pelo browser sem precisar criar uma página.html
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,  @PageableDefault(sort ="id", page=0, size=10) Pageable paginacao) { //cria a lista com paginação
		/*
		 * definir atributos de paginação, default caso não vir nenhum tipo de parametro
		 * para a paginação
		 * @PageableDefault(sort ="id", direction= Direction.DESC, page=0, size=10)
		 */
		
		if(nomeCurso == null) { //caso o nome for null irá trazer todos
			Page<Topico> topicos = topicoRepository.findAll(paginacao); //irá trazer todo conteúdo e informando quantas páginas tem
			return TopicoDto.converter(topicos);
			
		} else { //caso especificar um nome, irá trazer a lista que tem aquele nome
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true) 
	/*
	 * 	@CacheEvictinforma que é para limpar um determinado cache e atualizar as informações se 
	 * necessário do cache, mas usado quando a tabela não é usada tanto ou atualizada com frequencia.
	 */
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
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) { //verifica se o id está presente no banco de dados
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}	
		return ResponseEntity.notFound().build(); //irá retornar erro 404 caso não encontre nenhum id
	}
	
	@DeleteMapping("{id}") //deletar um topico especifico
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id){ //<?> generico sem especificar um
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) { 
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build(); //apenas irá retornar o retorno ok após o topico ser deletado
		}
		return ResponseEntity.notFound().build();
	}
}
