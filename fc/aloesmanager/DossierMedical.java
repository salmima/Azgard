package fc.aloesmanager;

import java.util.ArrayList;
import java.sql.*;

public class DossierMedical {

    private ArrayList<DMclinique> listeDMclinique;

    /**
     * Constructeur initialisant
     */
    public DossierMedical() {
        this.listeDMclinique = new ArrayList<DMclinique>();
    }

    /**
     * Ajout d'un DM clinique
     */
    public void ajouterDMclinique(DMclinique dm) {
        this.listeDMclinique.add(dm);
    }
    
     /**
     * Retourne la liste de DM clinique
     */
    public ArrayList<DMclinique> returnListe() {
        return this.listeDMclinique;
    }

    /**
     * Ajout de tous les DM clinique d'un patient passé aux urgencss
     */
    public void creationDMclinique(String id, String identifiant) { //identifiant de l'urgentiste
        //Récupération de la liste des services de l'hôpital
        ArrayList<String> listeService = new ArrayList<String>();
        Service[] listeEnum = Service.class.getEnumConstants();
         for (int i = 0; i < listeEnum.length ; i++){
                         if (!listeEnum[i].getLibelle().equals("imagerie") && !listeEnum[i].getLibelle().equals("anesthesie") && !listeEnum[i].getLibelle().equals("hematologie") && !listeEnum[i].getLibelle().equals("anatomopathologie") && !listeEnum[i].getLibelle().equals("biologie")){
                 listeService.add(listeEnum[i].getLibelle());
             }
         }
        
        //Ajout de chaque DM clinique 
        for (int i = 0; i < listeService.size(); i++) {
            DMclinique dm = new DMclinique();
            try {
                dm.rechercherUnDMClinique(id, identifiant, listeService.get(i));
            } catch (Exception e) {
                System.out.println("");
            }
            if (dm.getId() != null) {
                this.ajouterDMclinique(dm);
            }

        }
    }
    
    
     //TEST
     public static void main(String[] args) {

     }
}
