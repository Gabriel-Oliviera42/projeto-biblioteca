package com.example.biblioteca.model.livro;

import com.example.biblioteca.model.autor.Autor;
import com.example.biblioteca.model.genero.Genero;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer anoPublicacao;


    @ManyToOne
    @JoinColumn(name = "genero_id")
    private Genero genero;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();


    public Livro() {}

    public Livro(DadosCadastroLivro dados, Genero genero, Set<Autor> autores) {
        this.titulo = dados.titulo();
        this.anoPublicacao = dados.anoPublicacao();
        this.genero = genero;
        this.autores = autores;
    }

    public void atualizaDados(DadosAlteracaoLivro dados, Genero genero, Set<Autor> autores) {
        this.titulo = dados.titulo();
        this.anoPublicacao = dados.anoPublicacao();
        this.genero = genero;
        this.autores = autores;
    }

    public String getTitulo() { return titulo; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public Genero getGenero() { return genero; }
    public Long getId() { return id; }

    public Set<Autor> getAutores() { return autores; }
}