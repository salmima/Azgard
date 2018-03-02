/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

/**
 *
 * @author Thao
 */
public enum Examen {
    hematologie("hematologie"),
    biologie("biologie"),
    anatomopathologie("anatomopathologie"),
    radiologie("radiologie"),
    anesthesie("anesthesie");

    // attributs de l'énum
    private String libelle;

    /**
     * Constructeur
     */
    private Examen(String libelle) {
        this.libelle = libelle;
    }
    
    /**
     * Retourne le libellé
     */
    public String getLibelle(){
        return libelle;
    }
}

