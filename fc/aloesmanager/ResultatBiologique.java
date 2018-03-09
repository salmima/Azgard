package fc.aloesmanager;

import java.sql.*;

public class ResultatBiologique extends Resultat {

    /**
     * Constructeur
     */
    public ResultatBiologique(String CR, Date date, String obs, String id, PH ph, String service_demandeur){
        this.compteRendu = CR;
        this.date = date;
        this.observations = obs;      
        this.id = id;
        this.ph = ph;
        this.service_demandeur = service_demandeur;
    }

}
