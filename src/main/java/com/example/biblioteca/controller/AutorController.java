package com.example.biblioteca.controller;

import com.example.biblioteca.model.autor.Autor;
import com.example.biblioteca.model.autor.AutorRepository;
import com.example.biblioteca.model.autor.DadosAlteracaoAutor;
import com.example.biblioteca.model.autor.DadosCadastroAutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorRepository repository;

    @GetMapping("/listagem")
    public String carregaPaginaListagem(Model model, String nome) { // Recebe um parâmetro 'nome' opcional da URL
        if (nome != null) {
            model.addAttribute("listaDeAutores", repository.findByNomeContainingIgnoreCase(nome));
        } else {
            model.addAttribute("listaDeAutores", repository.findAll());
        }
        return "autores/listagem";
    }

    @GetMapping("/formulario")
    public String carregaPaginaFormulario(Long id, Model model) {
        if (id != null) {
            var autor = repository.getReferenceById(id);
            model.addAttribute("autor", autor);
        }
        return "autores/formulario";
    }

    @PostMapping("/cadastrar")
    @Transactional
    public String cadastraAutor(DadosCadastroAutor dados) {
        var autor = new Autor(dados);
        repository.save(autor);
        return "redirect:/autores/listagem";
    }

    @PostMapping("/alterar")
    @Transactional
    public String alteraAutor(DadosAlteracaoAutor dados) {
        var autor = repository.getReferenceById(dados.id());
        autor.atualizaDados(dados);
        return "redirect:/autores/listagem";
    }

    @PostMapping("/deletar")
    @Transactional
    public String removeAutor(Long id) {
        repository.deleteById(id);
        return "redirect:/autores/listagem";
    }

    @GetMapping("/sem-livros")
    public String carregaPaginaAutoresSemLivros(Model model) {
        model.addAttribute("listaDeAutores", repository.findAutoresSemLivros());
        return "autores/autores-sem-livros"; // Aponta para a nova página
    }
}