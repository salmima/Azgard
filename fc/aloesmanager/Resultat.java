package fc.aloesmanager;

import java.sql.*;

public abstract class Resultat {

    protected String compteRendu;
    protected Date date;
    protected String observations;
    protected String id;
    
    /**
     * Retourne le compte-rendu
     */
    public String getCompteRendu(){
        return this.compteRendu;
    }
    
    /**
     * Retourne les dates
     */
    public Date getDate(){
        return this.date;
    }
    
    /**
     * Retourne les observations
     */
    public String getObservations(){
        return this.observations;
    }
    
    //pour obtenir le type de résultat, tester avec instanceOf ResultatBiologie, ResultatAnapathologie, ResultatImagerie
    
     /**
     * Retourne l'ID du patient
     */
    public String getId(){
        return this.observations;
    }
    
    /**
     * Retourne le numéro RPPS du personnel MT qui a publié le résultat
     */
    public PH getPH(){
        return this.ph;
    }
    
}
