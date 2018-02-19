package fc.aloesmanager;

import java.sql.*;

public class MedecinTraitant extends PersonnelMedical {

    private String adresse;
    private String nom;
    private String prenom;
    private String ntel;
    private int codePostal;

    //Constructeur complet
    public MedecinTraitant(String adresse, String nom, String prenom, String ntel, int codeP) {
        this.nom = nom;
        this.prenom = prenom;
        this.ntel = ntel;
        this.adresse = adresse;
        this.codePostal = codeP;
    }

    /**
     * @return the adresse
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * @param adresse the adresse to set
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the ntel
     */
    public String getNtel() {
        return ntel;
    }

    /**
     * @param ntel the ntel to set
     */
    public void setNtel(String ntel) {
        this.ntel = ntel;
    }

    /**
     * @return the codePostal
     */
    public int getCodePostal() {
        return codePostal;
    }

    /**
     * @param codePostal the codePostal to set
     */
    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

}
