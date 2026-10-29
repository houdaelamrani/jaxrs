package ma.ws.jaxrs.entities;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entité JPA représentant un compte bancaire.
 * Annotée avec @XmlRootElement pour la sérialisation XML avec JAXB.
 */
@Entity
@Table(name = "comptes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "compte")
@XmlAccessorType(XmlAccessType.FIELD)
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double solde;

    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Enumerated(EnumType.STRING)
    private TypeCompte type;

    // Attributs de personnalisation
    private String titulaire;
    private String devise;

    // Constructeur personnalisé compatible avec le code initial du TP
    public Compte(Long id, double solde, Date dateCreation, TypeCompte type) {
        this.id = id;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.type = type;
        this.titulaire = "Anonyme";
        this.devise = "DH";
    }

    public Compte(Long id, double solde, Date dateCreation, TypeCompte type, String titulaire) {
        this.id = id;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.type = type;
        this.titulaire = titulaire;
        this.devise = "DH";
    }
}

