package fc.aloesmanager;

import java.sql.*;

public class ResultatImagerie extends Resultat {
    
    /**
     * Constructeur
     */
    public ResultatImagerie(String CR, Date date, String obs, String id, String nrpps){
        this.compteRendu = CR;
        this.date = date;
        this.observations = obs;      
        this.id = id;
        this.nrpps = nrpps;
    }

}
