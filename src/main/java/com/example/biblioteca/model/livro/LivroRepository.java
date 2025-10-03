package com.example.biblioteca.model.livro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAnoPublicacaoBetween(Integer anoInicio, Integer anoFim);


    @Query("SELECT l FROM Livro l WHERE l.genero.id = :idGenero ORDER BY l.anoPublicacao ASC")
    List<Livro> buscaLivrosPorGenero(@Param("idGenero") Long idGenero);

    @Query("SELECT l FROM Livro l JOIN l.autores a WHERE a.id = :idAutor")
    List<Livro> findByAutorId(@Param("idAutor") Long idAutor);

}