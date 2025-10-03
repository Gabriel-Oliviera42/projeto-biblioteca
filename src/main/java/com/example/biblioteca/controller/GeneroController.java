package com.example.biblioteca.controller;

import com.example.biblioteca.model.genero.DadosAlteracaoGenero;
import com.example.biblioteca.model.genero.DadosCadastroGenero;
import com.example.biblioteca.model.genero.Genero;
import com.example.biblioteca.model.genero.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/generos")
public class GeneroController {

    @Autowired
    private GeneroRepository repository;

    @GetMapping("/listagem")
    public String carregaPaginaListagem(Model model) {
        model.addAttribute("listaDeGeneros", repository.findAll());
        return "generos/listagem";
    }

    // MÉTODO MODIFICADO: Agora também carrega dados para edição
    @GetMapping("/formulario")
    public String carregaPaginaFormulario(Long id, Model model) {
        if (id != null) {
            var genero = repository.getReferenceById(id);
            model.addAttribute("genero", genero);
        }
        return "generos/formulario";
    }

    @PostMapping("/cadastrar")
    @Transactional
    public String cadastraGenero(DadosCadastroGenero dados) {
        var genero = new Genero(dados);
        repository.save(genero);
        return "redirect:/generos/listagem";
    }

    // NOVO METODO: Processa a alteração de um gênero
    @PostMapping("/alterar")
    @Transactional
    public String alteraGenero(DadosAlteracaoGenero dados) {
        var genero = repository.getReferenceById(dados.id());
        genero.atualizaDados(dados);
        return "redirect:/generos/listagem";
    }

    // NOVO METODO: Processa a exclusão de um gênero
    @PostMapping("/deletar")
    @Transactional
    public String removeGenero(Long id) {
        repository.deleteById(id);
        return "redirect:/generos/listagem";
    }
}