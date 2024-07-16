package com.alura.challengejavalivros;

import com.alura.challengejavalivros.menu.Menu;
import com.alura.challengejavalivros.repository.AutorRepository;
import com.alura.challengejavalivros.repository.LivroRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private LivroRepositoy repositorio;

	@Autowired
	private AutorRepository autorRepository;


    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Menu menu = new Menu(this.repositorio, this.autorRepository);
		menu.menu();
	}
}
