package fc.aloesmanager;

import java.sql.*;

public class ResultatImagerie extends Resultat {
    
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
