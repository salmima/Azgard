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
    secteur_cardiaque("secteur_cardiaque"),
    chirurgie_main_brules("chirurgie_main_brules"),
    imagerie("imagerie"),
    biologie("biologie"),
    anesthesie("anesthesie"),
    anatomopathologie("anatomopathologie"),
    hematologie("hematologie");
    
    
     
    private String libelle;

    /**
     * Constructeur
     */
    private Service(String libelle) {
        this.libelle = libelle;
    }
    
    /**
     * Retourne le libellé
     */
    public String getLibelle(){
        return libelle;
    }
}
