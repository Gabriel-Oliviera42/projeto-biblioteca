package com.example.biblioteca.model.autor;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    public Autor() {}

    public Autor(DadosCadastroAutor dados) {
        this.nome = dados.nome();
    }

    public void atualizaDados(DadosAlteracaoAutor dados) {
        this.nome = dados.nome();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @ManyToMany(mappedBy = "autores")
    private java.util.Set<com.example.biblioteca.model.livro.Livro> livros = new java.util.HashSet<>();

    public java.util.Set<com.example.biblioteca.model.livro.Livro> getLivros() {
        return livros;
    }
}