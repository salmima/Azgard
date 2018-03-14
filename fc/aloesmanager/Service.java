/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.sql.*;

/**
 *
 * @author Thao
 */
public enum Service { //à remplir
 secteur_cardiaque("secteur_cardiaque", "clinique"),
    chirurgie_main_brules("chirurgie_main_brules", "clinique"),
    imagerie("imagerie", "medico_technique"),
    biologie("biologie", "medico_technique"),
    anatomopathologie("anatomopathologie", "medico_technique"),
    anesthesie("anesthesie","medico_technique"),
    hematologie("hematologie","medico_technique");
    
    
     
    private String libelle;
     private String type;


    /**
     * Constructeur
     */
   private Service(String libelle, String type) {
        this.libelle = libelle ;
        this.type = type;
    }
    
    /**
     * Retourne le libellé
     */
    public String getLibelle(){
        return libelle;
    }
    
        /**
     * Retourne le type de service
     */
    public String getType(){
        return type;
    }
}
