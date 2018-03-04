/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Thao
 */
public class RegularisationDossierTemporaire {

    /**
     * Régularisation du dossier des urgences (temporaire)
     */
    public static boolean regulariserDT(String id_urgence) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement regulariser = null;
        PreparedStatement regulariserDM = null;
        ResultSet resultats_bd = null;
        boolean existe = false;

        //Récupération du dossier des urgences
        try {
            String requete = "select * from dossier_urgence where id_urgence = ?";
            regulariser = con.prepareStatement(requete);
            regulariser.setString(1, id_urgence);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = regulariser.executeQuery();
        } catch (SQLException e) {
            do {
                System.out.println("Requête refusée 1");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //-----------parcours des données retournées
        //---Variables temporaires
        String r_nom = "";
        String r_prenom = "";
        String r_sexe = "";
        Date r_dateN = null;
        String r_nom_proche = "";
        String r_tel_proche = "";
        Date r_dateArrivee = null;

        try {
            while (resultats_bd.next()) {
                r_nom = resultats_bd.getString("nom");
                r_prenom = resultats_bd.getString("prenom");
                r_sexe = resultats_bd.getString("sexe");
                r_nom_proche = resultats_bd.getString("nom_proche");
                r_tel_proche = resultats_bd.getString("tel_proche");
                r_dateN = resultats_bd.getDate("dateNaissance");
                r_dateArrivee = resultats_bd.getDate("dateArrivee");
            }
            //Fermeture des résultats des requête
            resultats_bd.close();
        } catch (SQLException e) {
            do {
                System.out.println("Accès aux résultats refusé");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //Test de l'existence du DMA
        DossierMedicoAdministratif dma = new DossierMedicoAdministratif();
        if (r_nom != null && r_prenom != null & r_dateN != null) {
            try {
                dma.rechercherUnDMA(r_nom, r_prenom, r_dateN);
            } catch (Exception e) {
                System.out.println("erreur");
            }

            //Si le DMA n'existe pas (le nom, prénom et date de naissance retourné par l'instance ne correspondent PAS à ceux qui sont rentrés dans le dossier des urgences
            //on renvoie toujours false: au niveau de l'interface, cela signifie qu'on doit afficher le DMA créé
            //mais attention! il est partiel! Il manque certaines informations non renseignées dans le dossier des urgences
            //cela évite néanmoins la ressaisie des informations déjà comprise dans le dossier des urgences
            try {
                if (dma.getNom() == null && dma.getPrenom() == null && dma.getDateN() == null) {
                    String IPP_provisoire = dma.genererUnIPP();
                    while (dma.testerExistenceIPP(IPP_provisoire) == true) {
                        IPP_provisoire = dma.genererUnIPP();
                    }

                    DossierMedicoAdministratif.creerUnDMA(IPP_provisoire, r_nom, r_prenom, r_sexe, r_dateN, r_nom_proche, r_tel_proche, "", "", "", null);
                    //afficher en refaisant rechercherUnDMA avec le nom, prénom, dateNaissance? Je ne sais pas comment tu comptes t'organiser dans l'interface

                    //Mise à jour de tous les dossiers
                    miseAJourDossiers(id_urgence, IPP_provisoire);
                } 

                //Si le DMA existe (le nom, prénom et date de naissance retourné par l'instance correspondent à ceux qui sont rentrés dans le dossier urgence
                //on renvoie true: au niveau de l'interface, true signifie qu'on peut afficher l'écran "Consulter un DMA" directement
                else {
                    String IPP = dma.getIPP();

                    //Mise à jour de tous les dossiers
                    miseAJourDossiers(id_urgence, IPP);
                }
                //On supprime le dossier des urgences
                RegularisationDossierTemporaire.supprimerDossierTemporaire(id_urgence);
                existe = true;
                //afficher en refaisant rechercherUnDMA avec le nom, prénom, dateNaissance? Je ne sais pas comment tu comptes t'organiser dans l'interface

            } catch (Exception e) {
                System.out.println("Le DMA n'existait pas");
            }
        }
        return existe;

    }

    public static void supprimerDossierTemporaire(String id_urgence) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement regulariserDM = null;

        //--1-- On supprime le dossier des urgences
        try {
            String requete = "DELETE from dossier_urgence WHERE id_urgence = ?";
            regulariserDM = con.prepareStatement(requete);
            regulariserDM.setString(1, id_urgence);
            int nbMaj = regulariserDM.executeUpdate();
            System.out.println("nb mise a jour dossier_urgence = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void miseAJourDossiers(String id_urgence, String nouveau_id) { //IPP retrouvé ou généré
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement regulariserDM = null;

        //-- 2 -- On met à jour l'id de tous les dossiers retrouvés
        //On commence par les DMclinique
        DossierMedical dm = new DossierMedical();
        ArrayList<DMclinique> listeDM;
        dm.creationDMclinique(id_urgence, "ADMISSION"); //on cherche les dossiers créés en urgence
        listeDM = dm.returnListe();

        //Pour chaque DM clinique existant pour ce patient
        for (int i = 0; i < listeDM.size(); i++) {
            String obs = listeDM.get(i).getObs();
            
            try {
                String requete = "UPDATE DMclinique SET observations = CONCAT(observations,?) WHERE nom_secteur = ? and id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, obs);
                regulariserDM.setString(2, listeDM.get(i).getService().getLibelle()); //on récupère le nom du secteur
                regulariserDM.setString(3, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                //On supprime le dossier existant pour l'id_urgence
                requete = "DELETE from DMclinique WHERE nom_secteur = ? and id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, listeDM.get(i).getService().getLibelle()); //on récupère le nom du secteur
                regulariserDM.setString(2, id_urgence);

                int nbMaj2 = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour dm clinique = " + nbMaj + nbMaj2); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //On continue avec les DMT
            DossierMedicoTechnique dmt = new DossierMedicoTechnique();
            dmt.creationDMT(id_urgence, "ADMISSION"); //on cherche tous les dmt créés avec l'id_urgence

            //DMT Biologie
            DMT_Biologie dmb = dmt.getDMTBiologie();
            obs = dmb.getObs();
            try {
                String requete = "UPDATE DMT_Biologie SET observations = CONCAT(observations,?) WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, obs);
                regulariserDM.setString(2, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                //On supprime le dossier existant pour l'id_urgence
                String requete2 = "DELETE from DMT_Biologie WHERE id = ?";
                regulariserDM = con.prepareStatement(requete2);
                regulariserDM.setString(1, id_urgence);

                int nbMaj2 = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour dm biologie= " + nbMaj + nbMaj2); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //DMT Anapath
            DMT_Anapath dm_an = dmt.getDMTAnapath();
            obs = dm_an.getObs();
            try {
                String requete = "UPDATE DMT_Anapath SET observations = CONCAT(observations,?) WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, obs);
                regulariserDM.setString(2, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                //On supprime le dossier existant pour l'id_urgence
                requete = "DELETE from DMT_Anapath WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, id_urgence);

                int nbMaj2 = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour dm anapath = " + nbMaj + nbMaj2); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //DMT Hematologie
            DMT_Hematologie dmh = dmt.getDMTHematologie();
            obs = dmh.getObs();
            try {
                String requete = "UPDATE DMT_Hematologie SET observations = CONCAT(observations,?) WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, obs);
                regulariserDM.setString(2, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                //On supprime le dossier existant pour l'id_urgence
                requete = "DELETE from DMT_Hematologie WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, id_urgence);

                int nbMaj2 = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour dm hematologie = " + nbMaj + nbMaj2); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //DMT Radio
            DMT_Radio dmr = dmt.getDMTRadio();
            obs = dmr.getObs();
            try {
                String requete = "UPDATE DMT_Radio SET observations = CONCAT(observations,?) WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, obs);
                regulariserDM.setString(2, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                //On supprime le dossier existant pour l'id_urgence
                requete = "DELETE from DMT_Radio WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(1, id_urgence);

                int nbMaj2 = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour radio = " + nbMaj + nbMaj2); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //On met à jour les résultats existants avec l'id_urgence
            try {
                String requete = "UPDATE Resultat SET id = ? WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(2, id_urgence);
                regulariserDM.setString(1, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour resultats = " + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //On met à jour les presciptions existantes avec l'id_urgence
            try {
                String requete = "UPDATE Prescription_examen SET id = ? WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(2, id_urgence);
                regulariserDM.setString(1, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour pres examen = " + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //On met à jour les presciptions existantes avec l'id_urgence
            try {
                String requete = "UPDATE Prescription_medic SET id = ? WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(2, id_urgence);
                regulariserDM.setString(1, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour pres medic= " + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //-- 3 -- On met à jour l'id dans CorrespondancePH_Venue
            try {
                String requete = "UPDATE CorrespondancePH_Venue SET id = ? WHERE id = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(2, id_urgence);
                regulariserDM.setString(1, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();

                System.out.println("nb mise a jour corresPH_Venue = " + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // -- 4 -- On met à jour la traçabilité
            PreparedStatement ajouterTrace = null;
            java.util.Date utilDate2 = new java.util.Date();
            java.sql.Date date_co = new java.sql.Date(utilDate2.getTime());

            try {
                String requete = "UPDATE Tracabilite SET IPP = ? WHERE IPP = ?";
                regulariserDM = con.prepareStatement(requete);
                regulariserDM.setString(2, id_urgence);
                regulariserDM.setString(1, nouveau_id);

                int nbMaj = regulariserDM.executeUpdate();
                System.out.println("nb mise a jour = Tracabilite" + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    //TEST
    public static void main(String[] args) {
        System.out.println(RegularisationDossierTemporaire.regulariserDT("111222444"));
    }
}
