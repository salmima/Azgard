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
    pneumologie("pneumologie"),
    urgences("urgences"),
    obstrétrique("obstrétique"),
    psychiatrie("psychiatrie"),
    dermatologie("dermatologie"),
    cardiologie("cardiologie"),
    urologie("urologie"),    
    radiologie("radiologie");
    
     // attributs de l'énum
    private String libelle;

    // constructeur
    private Service(String libelle) {
        this.libelle = libelle;
    }
    
    //retourne le libellé
    public String getLibelle(){
        return libelle;
    }
}
