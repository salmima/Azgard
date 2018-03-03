package fc.aloesmanager;

import java.sql.*;

public class ResultatImagerie extends Resultat {
    
     protected PH ph; //ph qui a ajouté le résultat
    
    /**
     * Constructeur
     */
    public ResultatImagerie(String CR, Date date, String obs, String id, PH ph){
        this.compteRendu = CR;
        this.date = date;
        this.observations = obs;      
        this.id = id;
        this.ph = ph;
    }

}
