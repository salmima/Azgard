package fc.aloesmanager;

import java.sql.*;

public class Hospitalisation extends Venue {

    private Lit localisation;

    /**
     * Constructeur initialisant
     */
    public Hospitalisation() {
        this.numSejour = null;
        this.dateEntree = null;
        this.dateSortie = null;
        this.lettreSortie = null;
        this.PHrespo = null;
        this.localisation = null;
    }

    /**
     * Constructeur complet
     */
    public Hospitalisation(String num, Date dE, Date dS, String lettre, PH phRespo, Lit lit) {
        this.numSejour = num;
        this.dateEntree = dE;
        this.dateSortie = dS;
        this.lettreSortie = lettre;
        this.PHrespo = phRespo;
        this.localisation = lit;
    }

    /**
     * Retourne la localisation du patient
     */
    public Lit getLocalisation() {
        return this.localisation;
    }

    /**
     * Retourne la date d'entrée
     */
    public java.sql.Date getDateEntree() {
        return this.dateEntree;
    }

     /**
     * Retourne la date de sortie
     */
    public java.sql.Date getDateSortie() {
        return this.dateSortie;
    }

     /**
     * Retourne le numéro de séjour
     */
    public String getNumSejour() {
        return this.numSejour;
    }

    /**
     * Met à jour la localisation d'un patient (instance java)
     */
    public void setLocalisation(Lit lit) { //le lit peut être retourné par l'interface en utilisant le constructeur dans Lit
        this.localisation = lit;
    }
    
    /**
     * Met à jour la localisation d'un patient (BDD)
     */
    public void mettreAJourLocalisationPatient(String id, String num_sejour, String num_lit) {
        //L'interface envoie des informations
        //La localisation du patient est mise à jour par le service clinique, une fois qu'ils on reçu le DMA, si c'est une hospitalisation

        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement majLit = null;

        try {
            String requete = "INSERT INTO LocalisationPatient VALUES(? , ? , ?)";
            majLit = con.prepareStatement(requete);
            majLit.setString(1, id);
            majLit.setString(2, num_sejour);
            majLit.setString(3, num_lit);

            int nbMaj = majLit.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    

}
