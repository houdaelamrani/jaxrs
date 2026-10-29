package ma.ws.jaxrs.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.ws.jaxrs.entities.Compte;
import ma.ws.jaxrs.entities.TypeCompte;
import ma.ws.jaxrs.repositories.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Web RESTful JAX-RS avec Jersey.
 * Expose les opérations CRUD et les méthodes métiers personnalisées.
 * Supporte les formats JSON et XML.
 */
@Component
@Path("/banque")
public class CompteRestJaxRSAPI {

    @Autowired
    private CompteRepository compteRepository;

    // =========================================================================
    // 1. READ: Récupérer tous les comptes (JSON et XML)
    // =========================================================================
    @Path("/comptes")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Compte> getComptes() {
        return compteRepository.findAll();
    }

    // =========================================================================
    // 2. READ: Récupérer un compte par son identifiant (JSON et XML)
    // =========================================================================
    @Path("/comptes/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte getCompte(@PathParam("id") Long id) {
        return compteRepository.findById(id).orElse(null);
    }

    // =========================================================================
    // 3. CREATE: Ajouter un nouveau compte (JSON et XML)
    // =========================================================================
    @Path("/comptes")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte addCompte(Compte compte) {
        if (compte.getDateCreation() == null) {
            compte.setDateCreation(new Date());
        }
        if (compte.getDevise() == null) {
            compte.setDevise("DH");
        }
        return compteRepository.save(compte);
    }

    // =========================================================================
    // 4. UPDATE: Mettre à jour un compte existant (JSON et XML)
    // =========================================================================
    @Path("/comptes/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte updateCompte(@PathParam("id") Long id, Compte compte) {
        Compte existingCompte = compteRepository.findById(id).orElse(null);
        if (existingCompte != null) {
            existingCompte.setSolde(compte.getSolde());
            if (compte.getDateCreation() != null) {
                existingCompte.setDateCreation(compte.getDateCreation());
            }
            if (compte.getType() != null) {
                existingCompte.setType(compte.getType());
            }
            if (compte.getTitulaire() != null) {
                existingCompte.setTitulaire(compte.getTitulaire());
            }
            if (compte.getDevise() != null) {
                existingCompte.setDevise(compte.getDevise());
            }
            return compteRepository.save(existingCompte);
        }
        return null;
    }

    // =========================================================================
    // 5. DELETE: Supprimer un compte (JSON et XML)
    // =========================================================================
    @Path("/comptes/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void deleteCompte(@PathParam("id") Long id) {
        compteRepository.deleteById(id);
    }

    // =========================================================================
    // FONCTIONNALITÉS DE PERSONNALISATION (BONUS TP)
    // =========================================================================

    /**
     * Personnalisation 1 : Filtrer les comptes par type (COURANT ou EPARGNE)
     */
    @Path("/comptes/type/{type}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Compte> getComptesByType(@PathParam("type") TypeCompte type) {
        return compteRepository.findByType(type);
    }

    /**
     * Personnalisation 2 : Effectuer un versement sur un compte
     */
    @Path("/comptes/{id}/verser")
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte verser(@PathParam("id") Long id, @QueryParam("montant") double montant) {
        Compte compte = compteRepository.findById(id).orElse(null);
        if (compte != null && montant > 0) {
            compte.setSolde(compte.getSolde() + montant);
            return compteRepository.save(compte);
        }
        return compte;
    }

    /**
     * Personnalisation 3 : Effectuer un retrait sur un compte
     */
    @Path("/comptes/{id}/retirer")
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte retirer(@PathParam("id") Long id, @QueryParam("montant") double montant) {
        Compte compte = compteRepository.findById(id).orElse(null);
        if (compte != null && montant > 0 && compte.getSolde() >= montant) {
            compte.setSolde(compte.getSolde() - montant);
            return compteRepository.save(compte);
        }
        return compte;
    }

    /**
     * Personnalisation 4 : Statistiques globales (Solde total de la banque)
     */
    @Path("/comptes/stats/solde-total")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Map<String, Object> getStatistiques() {
        List<Compte> comptes = compteRepository.findAll();
        double totalSolde = comptes.stream().mapToDouble(Compte::getSolde).sum();
        long nbComptesCourant = comptes.stream().filter(c -> c.getType() == TypeCompte.COURANT).count();
        long nbComptesEpargne = comptes.stream().filter(c -> c.getType() == TypeCompte.EPARGNE).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("nombreTotalComptes", comptes.size());
        stats.put("soldeTotalBanque", totalSolde);
        stats.put("comptesCourant", nbComptesCourant);
        stats.put("comptesEpargne", nbComptesEpargne);
        return stats;
    }
}

