package ma.ws.jaxrs;

import ma.ws.jaxrs.entities.Compte;
import ma.ws.jaxrs.entities.TypeCompte;
import ma.ws.jaxrs.repositories.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

/**
 * Classe principale de l'application Spring Boot.
 * Initialise des comptes de démonstration au démarrage.
 */
@SpringBootApplication
public class JaxrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaxrsApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CompteRepository compteRepository) {
        return args -> {
            System.out.println("==========================================================");
            System.out.println(">>> Initialisation des données dans la base H2...");
            System.out.println("==========================================================");

            // Création de comptes avec des données réalistes et personnalisées
            compteRepository.save(new Compte(null, 7500.50, new Date(), TypeCompte.COURANT, "Amine El Amrani"));
            compteRepository.save(new Compte(null, 15300.00, new Date(), TypeCompte.EPARGNE, "Sarah Bennani"));
            compteRepository.save(new Compte(null, 3200.75, new Date(), TypeCompte.COURANT, "Youssef Alami"));
            compteRepository.save(new Compte(null, 54000.00, new Date(), TypeCompte.EPARGNE, "Khadija Idrissi"));

            // Affichage des comptes dans la console
            compteRepository.findAll().forEach(c -> {
                System.out.println("Compte ID: " + c.getId() +
                        " | Titulaire: " + c.getTitulaire() +
                        " | Solde: " + c.getSolde() + " " + c.getDevise() +
                        " | Type: " + c.getType() +
                        " | Date: " + c.getDateCreation());
            });

            System.out.println("==========================================================");
            System.out.println(">>> Application démarrée avec succès !");
            System.out.println(">>> API REST : http://localhost:8082/banque/comptes");
            System.out.println(">>> Console H2 : http://localhost:8082/h2-console");
            System.out.println("==========================================================");
        };
    }
}

