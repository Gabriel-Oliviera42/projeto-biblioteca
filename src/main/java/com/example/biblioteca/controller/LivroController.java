package com.example.biblioteca.controller;

import com.example.biblioteca.model.autor.Autor;
import com.example.biblioteca.model.autor.AutorRepository;
import com.example.biblioteca.model.genero.GeneroRepository;
import com.example.biblioteca.model.livro.DadosAlteracaoLivro;
import com.example.biblioteca.model.livro.DadosCadastroLivro;
import com.example.biblioteca.model.livro.Livro;
import com.example.biblioteca.model.livro.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;


/*
samuel, isso daqui é algumas informações que podem ser uteis

para o projeto funcionar na sua máquina, você precisa fazer duas coisas:

arrumar seu mysql:
primeiro, abra seu mysql workbench e crie um novo schema (banco de dados) com o nome exato: biblioteca_db.
depois, abra o arquivo: src/main/resources/application.properties.
encontre estas duas linhas e coloque seu usuário e senha do mysql:

spring.datasource.username=root
spring.datasource.password=root (caso seja diferente no seu é só mudar)

na linha "ddl-auto=update" ela serve para atualizar, na primeira vez que você rodar o projeto,
o spring vai criar as tabelas sozinho. dependendo do que acontecer, talvez você
tenha que mudar para "ddl-auto=create" para recriar tudo do zero, mas acho que não vai precisar.

não estou usando @putmapping e @deletemapping:

nos controllers eu não usei `@putmapping` ou `@deletemapping`.
em vez disso, todas as ações (cadastrar, alterar, deletar) eu uso `@postmapping`,
mas com urls diferentes (ex: `/livros/cadastrar`, `/livros/alterar`, `/livros/deletar`).

por que durante os testes, o "filtro de metodo" do spring (aquele que usa o campo `_method` no html) estava
se comportando de um jeito que não sabia como arrumar (como o de "criar em vez de atualizar").
ai para acelerar e garantir que funcionasse, eu fiz assim. é uma abordagem mais direta.

fiz a relação `@manytomany` entre livro e autor. isso significa que um livro pode ter vários autores e
um autor pode ter vários livros. quando você rodar o projeto, o jpa vai criar automaticamente uma quarta tabela
"ponte" no banco, chamada `livro_autor`, para gerenciar isso.

checklist da atividade para ver se tá tudo certo:

1- fiz o banco de dados mysql com 3 tabelas (na verdade 4): `livros`, `generos`, `autores` e a tabela `livro_autor` que o jpa cria sozinho.
2- estamos usando as frameworks certas: maven, spring e spring data jpa.
3- implementei o crud completo para todas as tabelas: livros, gêneros e autores.
4- implementei 2 consultas com derived query: uma para buscar autor por parte do nome (`findbynamecontainingignorecase`) e outra para buscar livros entre dois anos (`findbyanopublicacaobetween`).
5- implementei a consulta que seria a nativequery usando jpql, porque estava dando menos problema: é a busca de livros por gênero, ordenada por ano (`buscalivrospor genero`).
6- implementei 2 consultas com jpql: uma para achar autores que não têm livros cadastrados (`findautoressemlivros`) e outra para buscar todos os livros de um autor específico (`findbyautorid`).

sua parte do css:

-   o estilo da aplicação está em um único arquivo: `src/main/resources/static/estilo.css`.
-   ele já está linkado no nosso `template.html`, então qualquer mudança que você fizer lá será refletida em todas as páginas.
-   fique à vontade para estilizar as tabelas, os formulários, o menu de navegação, etc.

qualquer dúvida sobre o código, é só me avisar.
*/

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private AutorRepository autorRepository; // Repositório de autores

    @Autowired
    private LivroRepository repository;

    @GetMapping("/formulario")
    public String carregaPaginaFormulario(Long id, Model model) {
        model.addAttribute("generos", generoRepository.findAll());
        model.addAttribute("autores", autorRepository.findAll());
        if (id != null) {
            var livro = repository.getReferenceById(id);
            model.addAttribute("livro", livro);
        }
        return "livros/formulario";
    }

    @GetMapping("/listagem")
    public String carregaPaginaListagem(Model model, Integer anoInicio, Integer anoFim) {
        if (anoInicio != null && anoFim != null) {
            model.addAttribute("listaDeLivros", repository.findByAnoPublicacaoBetween(anoInicio, anoFim));
        } else {
            model.addAttribute("listaDeLivros", repository.findAll());
        }
        return "livros/listagem";
    }

    @PostMapping("/cadastrar")
    @Transactional
    public String cadastraLivro(DadosCadastroLivro dados) {
        var genero = generoRepository.getReferenceById(dados.generoId());
        Set<Autor> autores = new HashSet<>(autorRepository.findAllById(dados.autorIds()));
        var livro = new Livro(dados, genero, autores);
        repository.save(livro);
        return "redirect:/livros/listagem";
    }

    @PostMapping("/alterar")
    @Transactional
    public String alteraLivro(DadosAlteracaoLivro dados) {
        var livro = repository.getReferenceById(dados.id());
        var genero = generoRepository.getReferenceById(dados.generoId());
        Set<Autor> autores = new HashSet<>(autorRepository.findAllById(dados.autorIds()));
        livro.atualizaDados(dados, genero, autores);
        return "redirect:/livros/listagem";
    }

    @PostMapping("/deletar")
    @Transactional
    public String removeLivro(Long id) {
        repository.deleteById(id);
        return "redirect:/livros/listagem";
    }

    @GetMapping("/por-genero")
    public String carregaPaginaBuscaPorGenero(Model model, Long generoId) {
        model.addAttribute("generos", generoRepository.findAll());

        if (generoId != null) {
            model.addAttribute("listaDeLivros", repository.buscaLivrosPorGenero(generoId));
        }

        return "livros/busca-por-genero";
    }

    @GetMapping("/por-autor")
    public String carregaPaginaBuscaPorAutor(Model model, Long autorId) {
        model.addAttribute("autores", autorRepository.findAll());

        if (autorId != null) {
            model.addAttribute("listaDeLivros", repository.findByAutorId(autorId));
        }

        return "livros/busca-por-autor"; // Aponta para a nova página HTML
    }
}