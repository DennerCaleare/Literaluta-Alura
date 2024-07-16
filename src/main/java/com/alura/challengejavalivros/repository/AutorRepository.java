package com.alura.challengejavalivros.repository;

import com.alura.challengejavalivros.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNome(String nome);

    @Query("""
            SELECT a 
            FROM Autor a
            WHERE 
                (a.anoNascimento <= :ano) AND
                (a.anoMorte >= :ano)
            """)
    List<Autor> findAutoresVivo(Integer ano);
}
