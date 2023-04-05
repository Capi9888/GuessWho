package com.guesswho.guesswho;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.guesswho.guesswho.Model.Game;


@SpringBootApplication
public class GuesswhoApplication {

	public static void main(String[] args) {
	//SpringApplication.run(GuesswhoApplication.class, args);
	Game g = new Game();
	System.out.println(g.guessPersonEnemy(11));
	}
}
