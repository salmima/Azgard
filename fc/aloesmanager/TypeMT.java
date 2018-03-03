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
public enum TypeMT {
        biologie("biologie"),
    hematologie("hematologie"),
    anatomopathologie("anatomopathologie");
    
     // attributs de l'énum
    private String libelle;

    /**
     * Constructeur
     */
    private TypeMT(String libelle) {
        this.libelle = libelle;
    }
    
    /**
     * Retourne le libellé
     */
    public String getLibelle(){
        return libelle;
    }
}
