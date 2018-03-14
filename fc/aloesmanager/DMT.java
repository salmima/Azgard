/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.util.ArrayList;

/**
 *
 * @author Thao
 */
public abstract class DMT {

    protected ArrayList<Resultat> liste_resultats;
    protected ArrayList<PrescriptionExamen> liste_examens;
    protected String observations;
    protected String id;
    
    /**
     * Ajout d'un résultat à la liste d'examens
     */
    public void ajouterPrescription(PrescriptionExamen pres) {
        this.liste_examens.add(pres);
    }

    /**
     * Retourne les observations
     */
    public String getObs() {
        return this.observations;
    }

    /**
     * Retourne l'id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Mise à jour deS observations
     */
    public void setObs(String obs) {
        this.observations = obs;
    }

    /**
     * Ajout d'un résultat à la liste de résultats
     */
    public void ajouterResultat(Resultat resultat) {
        this.liste_resultats.add(resultat);
    }
    
    
    /**
     * Récupération de la liste de résultats
     */
    public ArrayList<Resultat> getListeResultat() {
        return this.liste_resultats;
    }

}
