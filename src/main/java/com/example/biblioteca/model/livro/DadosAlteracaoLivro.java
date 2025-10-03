package com.example.biblioteca.model.livro;

import java.util.List;

public record DadosAlteracaoLivro(Long id, String titulo, Integer anoPublicacao, Long generoId, List<Long> autorIds) {
}