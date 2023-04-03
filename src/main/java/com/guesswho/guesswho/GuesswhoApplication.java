package com.guesswho.guesswho;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.guesswho.guesswho.DAO.PersonDB;

@SpringBootApplication
public class GuesswhoApplication {

	public static void main(String[] args) {
	SpringApplication.run(GuesswhoApplication.class, args);
	}
}
