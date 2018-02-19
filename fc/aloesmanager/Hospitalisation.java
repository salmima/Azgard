package fc.aloesmanager;

import java.sql.*;

public class Hospitalisation extends Venue {

    private Lit localisation;

    public Hospitalisation() {
        this.numSejour = null;
        this.dateEntree = null;
        this.dateSortie = null;
        this.lettreSortie = null;
        this.PHrespo = null;
        this.localisation = null;
    }

    public Hospitalisation(String num, Date dE, Date dS, String lettre, PH phRespo, Lit lit) {
        this.numSejour = num;
        this.dateEntree = dE;
        this.dateSortie = dS;
        this.lettreSortie = lettre;
        this.PHrespo = phRespo;
        this.localisation = lit;
    }

    public Lit getLocalisation() {
        return this.localisation;
    }

    public java.sql.Date getDateEntree() {
        return this.dateEntree;
    }

    public java.sql.Date getDateSortie() {
        return this.dateSortie;
    }

    public String getNumSejour() {
        return this.numSejour;
    }

    public void setLocalisation(Lit lit) { //le lit peut être retourné par l'interface en utilisant le constructeur dans Lit
        this.localisation = lit;
    }
    

}
