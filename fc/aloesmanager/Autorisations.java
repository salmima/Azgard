/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Thao
 */
public class Autorisations {
    //On rappelle qu'on a les paramètres 
    //secteur
    //identifiant
    //dans la classe Connexion

    //1 - DMA transmis au service clinique qui le prend en charge c'est-à-dire que le service clinique ayant accès à son DMA 
    //est le service clinique possède le PH respo de la venue du patient
    //dans l'interface, on lance cette fonction quand on veut cliquer sur un DMA à partir d'un DM clinique
    //On considère que le DM clinique est tout le temps ouvrable par le service clinique
    //(sinon je trouve que c'est hyper restrictif, enfin comme vous voulez
    //si vous voulez spécifier qu'un DM n'est ouvrable que si le patient est admis dans l'hôpital
    //il faut appeler cette fonction avant de créer ou rechercher un IPP, après avoir cliqué sur le bouton "créer" ou "rechercher"
    //Il faut appeler cette fonction en mettant les paramètres de la classe Connexion et l'IPP/id_urgence rentré dans le champ de recherche
    public static boolean autorisationDMADM(String identifiant, String nom_secteur, String id) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheOK = null;
        ResultSet resultats_bd = null;
        boolean ok = false;

        //----------- Requête
        try {
            rechercheOK = con.prepareStatement("SELECT service FROM CorrespondancePH_Venue natural join personnelMedical natural join Venue WHERE id = ? and date_sortie is NULL");
            rechercheOK.setString(1, id); //on cherche le service du PH respo de la dernière venue du patient
        } catch (Exception ee) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheOK.executeQuery();
        } catch (SQLException e) {
            do {
                System.out.println("Requête refusée");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //-----------parcours des données retournées
        //---Variables temporaires 
        String r_service = "";

        try {
            while (resultats_bd.next()) {
                try {
                    r_service = resultats_bd.getString("service");
                } catch (Exception e) {
                    System.out.println("");
                }

                //on teste si le service de la personne connectée est le même que celui du PH respo du patient
                if (r_service.equals(nom_secteur)) {
                    ok = true;
                }
            }
            //Fermeture des résultats des requêtes
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
        return ok;
    }

    //2 - DMA transmis au service médico-technique qui le prend en charge
    // c'est-à-dire que le service médico-technique a accès à son DMA
    //si un service demandeur a fait une demande de prestation
    //et que ce service demandeur possède le PH respo
    //utiliser cette méthode avant d'afficher un DMA à partir d'un consultation de DMT
    //la personne connectée doit donc nécessairement être du service médico-technique radiologie 
    //pour pouvoir accéder (peut-être) à cette fonctionnalité
    public static boolean autorisationDMADMT(String id) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheOK = null;
        ResultSet resultats_bd = null;
        boolean ok = false;

        //----------- Requête
        try {
            rechercheOK = con.prepareStatement("select * from Prescription_examen natural join personnelMedical where done = 0 and examen = 'radiologie' and id = ? and service in\n"
                    + "(SELECT service FROM CorrespondancePH_Venue natural join personnelMedical natural join Venue WHERE id = ? and date_sortie is NULL)");
            rechercheOK.setString(1, id);
            rechercheOK.setString(2, id);
        } catch (Exception ee) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheOK.executeQuery();
        } catch (SQLException e) {
            do {
                System.out.println("Requête refusée");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //-----------parcours des données retournées
        //---Variables temporaires 
        String n_rpps = "";

        try {
            while (resultats_bd.next()) {
                try {
                    n_rpps = resultats_bd.getString("n_rpps");
                } catch (Exception e) {
                    System.out.println("");
                }

                //on teste si la requête a retourné un résultat
                if (n_rpps != null) {
                    ok = true;
                }
            }
            //Fermeture des résultats des requêtes
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

        return ok;
    }

    //3 - Un DM ou DMT n'est visible que si la personne connectée fait partie
    //du service concerné
    
    
    
    //4- Un PH anesthésiste et un PH radiologue doit pouvoir voir le DM clinique du service qui lui a demandé une prestation pour un patient
    //cette méthode renvoie le service dont le dossier médical clinique peut-être consulté par le radiologue ou l'anesthesiste
    public static String autorisationDM(String secteur, String id) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheOK = null;
        ResultSet resultats_bd = null;
        String examen = "";
        String r_service = "";

        if (secteur.equals("imagerie")) {
            examen = "radiologie";
        }
        else if (secteur.equals("anesthesie")) {
            examen = "anesthesie";
        }

        //----------- Requête
        try {
            rechercheOK = con.prepareStatement("select service from Prescription_examen natural join personnelMedical where done = 0 and examen = ? and id = ?");
            rechercheOK.setString(1, examen);
            rechercheOK.setString(2, id);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheOK.executeQuery();
        } catch (SQLException e) {
            do {
                System.out.println("Requête refusée");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //-----------parcours des données retournées
        try {
            while (resultats_bd.next()) {
                try {
                    r_service = resultats_bd.getString("service");
                } catch (Exception e) {
                    System.out.println("");
                }

            }
            //Fermeture des résultats des requêtes
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

        return r_service;
    }

    //5- Le PH du service des urgences doit pouvoir voir tous les DM et DMT 
    // du patient admis: il faut lui lister l'ensemble des DMs existants
    //ne mettre cette méthode que si la personne fait partie des urgences (nom_secteur = urgences)

    //La liste des DM et DMT sont dans le DossierTemporaire (attribut dmt et dm: contiennent
    //tous les DM cliniques existants)
    
    
    
    //6 - ajout d'une lettre de sortie dans un DM
    //setVisible(true) le bouton d'ajout de lettre de sortie si boolean renvoyé  true
    public static boolean autorisationLettre(String nom_secteur, String IPP) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheOK = null;
        ResultSet resultats_bd = null;
        boolean ok = false;

        //----------- Requête: cherche le service responsable du patient
        try {
            rechercheOK = con.prepareStatement("SELECT service FROM CorrespondancePH_Venue natural join personnelMedical natural join Venue WHERE id = ? and date_sortie is NULL");
            rechercheOK.setString(1, IPP);
        } catch (Exception ee) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheOK.executeQuery();
        } catch (SQLException e) {
            do {
                System.out.println("Requête refusée");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //-----------parcours des données retournées
        //---Variables temporaires 
        String r_service = "";

        try {
            while (resultats_bd.next()) {
                try {
                    r_service = resultats_bd.getString("service");
                } catch (Exception e) {
                    System.out.println("");
                }

                //on teste si le service de la personne connectée est le même que celui du PH respo du patient
                if (r_service.equals(nom_secteur)) {
                    ok = true;
                }
            }
            //Fermeture des résultats des requêtes
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

        return ok;
    }
    
    public static void main(String[] args) {

        //Test autorisation 1
        System.out.println(Autorisations.autorisationDMADM("GREGH", "secteur_cardiaque", "180000111")); //doit envoyer true -- OK
        System.out.println(Autorisations.autorisationDMADM("ELPLO", "imagerie", "180000111")); //doit envoyer false -- OK

        //Test autorisation 2
        System.out.println(Autorisations.autorisationDMADMT("180000111")); //doit envoyer true -- OK
        System.out.println(Autorisations.autorisationDMADMT("182678481")); //doit envoyer false -- OK
        
         //Test autorisation 4
        System.out.println(Autorisations.autorisationDM("imagerie","180000111")); //doit envoyer secteur cardiaque -- OK
        System.out.println(Autorisations.autorisationDM("anesthesie","182678481")); //doit envoyer urgences -- OK
    
        //Test autorisation 6
        System.out.println(Autorisations.autorisationLettre("secteur_cardiaque", "180000111")); //doit envoyer true -- OK
        System.out.println(Autorisations.autorisationLettre("secteur_cardiaque", "185398140")); //doit envoyer true - OK
        System.out.println(Autorisations.autorisationLettre("imagerie", "185398140")); //doit envoyer false - OK
    }

}

