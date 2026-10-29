package ma.ws.jaxrs.repositories;

import ma.ws.jaxrs.entities.Compte;
import ma.ws.jaxrs.entities.TypeCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface Repository JPA pour la gestion des comptes bancaires.
 */
@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    // Méthodes de recherche personnalisées
    List<Compte> findByType(TypeCompte type);

    List<Compte> findByTitulaireContainingIgnoreCase(String titulaire);
}

