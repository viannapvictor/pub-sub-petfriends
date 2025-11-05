package br.edu.infnet.petfriends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetFriendsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetFriendsApplication.class, args);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("PET FRIENDS API - Sistema de Agendamento");
        System.out.println("=".repeat(70));
        System.out.println("API REST disponivel em: http://localhost:8080/api/agendamentos");
        System.out.println("Console H2 disponivel em: http://localhost:8080/h2-console");
        System.out.println("=".repeat(70) + "\n");
    }
}