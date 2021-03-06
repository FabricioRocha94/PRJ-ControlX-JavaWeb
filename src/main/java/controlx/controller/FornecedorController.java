package controlx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import controlx.model.Fornecedor;
import controlx.model.Produto;
import controlx.repository.CategoriaRepository;
import controlx.repository.FornecedorRepository;
import controlx.repository.ProdutoRepository;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

	@Autowired
	private FornecedorRepository fornecedorRepository;

	@GetMapping("/cadastrar")
	public ModelAndView cadastrar() {
		ModelAndView modelAndView = new ModelAndView("formFornecedor");
		modelAndView.addObject("fornecedorObj", new Fornecedor());
		return modelAndView;
	}

	@GetMapping("/editar/{idfornecedor}")
	public ModelAndView editar(@PathVariable("idfornecedor") Long idfornecedor) {
		Optional<Fornecedor> fornecedor = fornecedorRepository.findById(idfornecedor);
		ModelAndView modelAndView = new ModelAndView("/formFornecedor");
		modelAndView.addObject("fornecedorObj", fornecedor.get());
		modelAndView.addObject("edit", "");
		modelAndView.addObject("id", idfornecedor);
		return modelAndView;
	}

	@GetMapping("/remover/{idfornecedor}")
	public ModelAndView excluir(@PathVariable("idfornecedor") Long idfornecedor) {
		Optional<Fornecedor> f = fornecedorRepository.findById(idfornecedor);
		f.get().setDeleted(true);
		fornecedorRepository.save(f.get());
		ModelAndView modelAndView = new ModelAndView("redirect:/fornecedores");
		return modelAndView;
	}

	@PostMapping("**/salvar")
	public ModelAndView salvar(@Valid Fornecedor fornecedor, BindingResult bindingResult) {
		//Verifica erros no formulário
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("/formFornecedor");
			modelAndView.addObject("fornecedorObj", fornecedor);
			
			List<String> msg = new ArrayList<>();
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}
			
			modelAndView.addObject("msg", msg);
			
			return modelAndView;
		}
		
		//Se não tiver erros...
		fornecedorRepository.save(fornecedor);
		ModelAndView modelAndView = new ModelAndView("redirect:/fornecedores");
		return modelAndView;
	}

	@GetMapping("**/")
	public ModelAndView listarTodos() {
		ModelAndView andView = new ModelAndView("fornecedores");
		Iterable<Fornecedor> fornecedoresIt = fornecedorRepository.findAll();
		andView.addObject("fornecedores", fornecedoresIt);
		return andView;
	}

	@PostMapping("**/pesquisar")
	public ModelAndView listarByName(@RequestParam("pesquisa") String pesquisa,
			@RequestParam("tipoPesquisa") String tipoPesquisa) {
		ModelAndView modelAndView = new ModelAndView("/fornecedores");
		if (tipoPesquisa.equals("nome")) {
			modelAndView.addObject("fornecedores", fornecedorRepository.findFornecedorByName(pesquisa));
			return modelAndView;
		} else if (tipoPesquisa.equals("id")) {
			modelAndView.addObject("fornecedores", fornecedorRepository.findFornecedorById(Long.parseLong(pesquisa)));
			return modelAndView;
		}

		return modelAndView;
	}
}
