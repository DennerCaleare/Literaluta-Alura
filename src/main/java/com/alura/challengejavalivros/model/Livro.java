package com.alura.challengejavalivros.model;

import com.alura.challengejavalivros.DTO.DadosAutor;
import com.alura.challengejavalivros.DTO.DadosLivro;
import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private String idioma;

    private Integer downloads;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Livro(){}

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo().toLowerCase();
        this.idioma = dadosLivro.idiomas().getFirst();
        this.downloads = dadosLivro.downloads();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public void setAutor(Autor dadosAutor) {
        this.autor = dadosAutor;
    }

    @Override
    public String toString() {
        return
                "------------------------------------------------" +
                "\nTítulo: " + this.titulo +
                "\nAutor: " + this.autor.getNome() +
                "\nIdioma: " + this.idioma +
                "\nDownloads: " + this.downloads + "\n";
    }

}
