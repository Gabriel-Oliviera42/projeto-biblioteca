package com.example.biblioteca.model.autor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT a FROM Autor a WHERE a.livros IS EMPTY")
    List<Autor> findAutoresSemLivros();
}