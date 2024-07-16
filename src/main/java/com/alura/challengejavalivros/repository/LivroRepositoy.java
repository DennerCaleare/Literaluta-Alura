package com.alura.challengejavalivros.repository;

import com.alura.challengejavalivros.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepositoy extends JpaRepository<Livro, Long> {
    boolean existsByTitulo(String titulo);

    Livro findByTitulo(String titulo);

    @Query("""
            SELECT l
            FROM Livro l
            WHERE l.idioma = :idioma
            """)
    List<Livro> findIdiomaLivros(String idioma);
}
