package fc.aloesmanager;

import java.sql.*;

public class Consultation extends Venue {

    /**
     * Constructeur initialisant
     */
    public Consultation() {
        this.numSejour = null;
        this.dateEntree = null;
        this.dateSortie = null;
        this.lettreSortie = null;
    }

    /**
     * Constructeur complet
     */
    public Consultation(String num, Date dE, Date dS, String lettre, PH phRespo) {
        this.numSejour = num;
        this.dateEntree = dE;
        this.dateSortie = dS;
        this.lettreSortie = lettre;
        this.PHrespo = phRespo;
    }
    
  

    /**
     * Retourne le PH respo
     */
    public PH getPHRespo() {
        return this.PHrespo;
    }
    
    //TEST
    public static void main(String[] args) {
        Consultation c = new Consultation();
        Date date1 = new Date(118,1,19);
        System.out.println(date1);
        PH ph1 = new PH();
        ph1 = ph1.rechercherUnMedecin("House", "Gregory");

        c.creerUneVenue("555458756", "180200003", date1, ph1);
    }
}
