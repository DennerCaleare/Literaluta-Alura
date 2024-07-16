package com.alura.challengejavalivros.menu;

import com.alura.challengejavalivros.DTO.DadosAutor;
import com.alura.challengejavalivros.DTO.DadosLivro;
import com.alura.challengejavalivros.DTO.DadosResult;
import com.alura.challengejavalivros.model.*;
import com.alura.challengejavalivros.repository.AutorRepository;
import com.alura.challengejavalivros.repository.LivroRepositoy;
import com.alura.challengejavalivros.services.ConsultaApi;
import com.alura.challengejavalivros.services.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private final String URL_API = "https://gutendex.com/books/?search=";
    private ConsultaApi consulta = new ConsultaApi();
    private Scanner scanner = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();

    private final LivroRepositoy repositorio;
    private final AutorRepository autorRepository;

    private boolean mostrarMenu = true;
    public Menu(LivroRepositoy repositorio, AutorRepository autorRepository) {
        this.repositorio = repositorio;
        this.autorRepository = autorRepository;
    }

    public void menu() {

        var menu = """
                \nSELECIONE UM NUMERO DE SUA OPÇÃO:
                
                1) Buscar livro pelo seu título
                2) Listar livros registrados
                3) Listar autores registrados
                4) Listar autores vivos em um determinado ano
                5) Listar livro em um determinado idioma
                0) Sair
                """;

        while (mostrarMenu) {
            System.out.println(menu);
            var opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.println("""
                            Opção selecionada => 1) Buscar livro pelo seu título
                            """);
                    this.buscarLivroPeloTitulo();
                    break;

                case "2":
                    System.out.println("""
                            Opção selecionada => 2) Listar livros registrados
                            """);
                    this.listarLivrosCadastrados();
                    break;

                case "3":
                    System.out.println("""
                             Opção selecionada => 3) Listar autores registrados
                            """);
                    this.listarAutoresRegistrados();
                    break;

                case "4":
                    System.out.println("""
                            Opção selecionada => 4) Listar autores vivos em um determinado ano
                            """);
                    this.autoresVivos();
                    break;

                case "5":
                    System.out.println("""
                            Opção selecionada => 5) Listar livro em um determinado idioma
                            """);
                    this.listarLivroIdioma();
                    break;

                case "0":
                    this.mostrarMenu = false;
                    break;

                default:
                    System.out.println("Opção invalida. Selecione uma opção valida, ou '0'(ZERO) para sair");
                    break;

            }
        }

    }

    public void buscarLivroPeloTitulo() {

        System.out.println("Informe um título: ");
        var titulo = scanner.nextLine().toLowerCase().replace(" ", "%20");

        boolean livroCadastrado =  this.verificaLivroCadastrado(titulo.replace("%20", " "));

        if(!livroCadastrado) {
            String json = consulta.resultadoApi(URL_API + titulo);
            var dadosLivro = conversor.obterDados(json, DadosResult.class);

            if(dadosLivro.livros().isEmpty()) {
                System.out.println("Livro não encontrado");
            } else {
                this.verificaAutorCadastrado(dadosLivro.livros().getFirst());
            }


        }
    }
    public Boolean verificaLivroCadastrado(String titulo) {
        var tituloJaCadastrado = repositorio.existsByTitulo(titulo);
//
        if(tituloJaCadastrado) {
            var livroDb = repositorio.findByTitulo(titulo);
            System.out.println("LIVRO JÁ CADASTRADO");
            System.out.println(livroDb);
            return true;
        }

        return false;

    }

    public void verificaAutorCadastrado(DadosLivro dadosLivro) {
        System.out.println(dadosLivro.autores().getFirst().nome());
        Autor autor = autorRepository.findByNome(dadosLivro.autores().getFirst().nome().toLowerCase());


        if(autor == null) {
            System.out.println("O autor não existe: " + dadosLivro.autores().getFirst().nome());

            Autor autor3 = new Autor(dadosLivro.autores().getFirst());

            Livro livro2 = new Livro(dadosLivro);
            livro2.setAutor(autor3);
            repositorio.save(livro2);

            System.out.println("Livro encontrado e salvo no banco de dados!");
            System.out.println(livro2);
        } else {
            Livro livro = new Livro(dadosLivro);
            livro.setAutor(autor);
            List<Livro> listaLivros = new ArrayList<>();
            listaLivros.add(livro);

            System.out.println("O autor já existe: " + autor.getNome());
            Autor autor1 = new Autor(autor.getNome(), autor.getAnoNascimento(), autor.getAnoMorte(), autor.getId());
            autor1.setLivro(listaLivros);
            autorRepository.save(autor1);

            System.out.println("Livro encontrado e salvo no banco de dados!");
            System.out.println(livro);
        }
    }

    private void listarLivrosCadastrados() {
        List<Livro> livros = repositorio.findAll();

        if(livros.isEmpty()) {
            System.out.println("Não há livros cadastrados no banco de dados");
        } else {
            System.out.println("""
                    ------------------------------------------------
                    LIVROS CADASTRADOS
                    ------------------------------------------------
                    """);
            livros.forEach(System.out::println);
        }


    }

    public void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if(autores.isEmpty()) {
            System.out.println("Não há autores cadastrados no banco de dados");
        } else {
            System.out.println("""
                    ------------------------------------------------
                    AUTORES CADASTRADOS
                    ------------------------------------------------
                    """);
            autores.forEach(System.out::println);
        }
    }

    public void autoresVivos() {
        System.out.println("Digite o ano: ");
        var anoInput = scanner.nextLine();

        try {
            Integer ano = Integer.valueOf(anoInput);
            List<Autor> autores = autorRepository.findAutoresVivo(ano);
            if(autores.isEmpty()) {
                System.out.println("Autor não encontrado.");
            } else {
                System.out.println("\nAutores");
                autores.forEach(System.out::println);
            }
        } catch (NumberFormatException n) {
            System.out.println("O valor informado é inválido => [" + n.getMessage() + "]");
        }

    }

    public void listarLivroIdioma() {
        System.out.println("""
                Insira o idioma para reaizar a busca:
                es - espanhou
                en - inglês
                fr - francês
                pt - portugês
                """);
        var idioma = scanner.nextLine();

        List<Livro> livros = repositorio.findIdiomaLivros(idioma);

        if(livros.isEmpty()) {
            System.out.println("Nenhum Livro encontrado para o idioma informado");
        } else {
            livros.forEach(System.out::println);
        }
    }

}
