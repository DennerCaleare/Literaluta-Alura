package com.alura.challengejavalivros.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosResult(
        @JsonAlias("results") List<DadosLivro> livros
) {
}
