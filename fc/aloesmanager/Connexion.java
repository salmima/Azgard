/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 //cette classe sera à mettre dans l'UI!

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Thao
 */
public class Connexion {

    protected String identifiant;
    protected String secteur;
    protected String nom;
    protected String prenom;

    public Connexion() {
        this.identifiant = null;
        this.secteur = null;
        this.nom = null;
        this.prenom = null;
    }
    
    //pour se connecter: utiliser testerIdentifiants
    //puis ouvrir une connexion avec ouvrirUneConnexion
    //Enchainement des méthodes
    //ouvrirConnexion(testerIdentifiants("id","mdp"), "id")
    //si testerIdentifiants renvoie false: mettre des messages d'erreur
    public boolean testerIdentifiants(String identifiant, String mdp) { //l'identifiant et le mdp correspondent à un getText() de l'interface
        boolean connexionOK = false;
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheOK = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //----------- Requêtes
        try {
            rechercheOK = con.prepareStatement("SELECT identifiant_connexion FROM acces_serveur where mdp = SHA1(?)");
            rechercheOK.setString(1, mdp);
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
        //---Variables temporaires
        String r_identifiant = null;

        try {
            while (resultats_bd.next()) {
                r_identifiant = resultats_bd.getString("identifiant_connexion"); //ne marche pas
                this.identifiant = r_identifiant;
            }
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
        try {
            if (r_identifiant.compareTo(identifiant) == 0) {
                connexionOK = true;
            }
        } catch (Exception e) {
            System.out.println("");
        }

        return connexionOK;
    }

    public void ouvrirUneConnexion(boolean connexionOK, String identifiant) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheType = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //----------- Requêtes
        try {
            rechercheType = con.prepareStatement("select nom, prenom,service from correspondanceId natural join personnelMedical where identifiant_connexion = ?");
            rechercheType.setString(1, identifiant);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheType.executeQuery();
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
        String r_nom= "";
        String r_prenom ="";
        String r_service = null;

        try {
            while (resultats_bd.next()) {
                r_nom = resultats_bd.getString("nom");
                r_prenom = resultats_bd.getString("prenom");
                r_service = resultats_bd.getString("service");

                this.secteur = r_service;
                this.nom = r_nom;
                this.prenom = r_prenom;
            }
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

    }
    
    
    public static void main(String[] args) {
        Connexion con = new Connexion();
//        System.out.println(testerIdentifiants("POLOB", "mdp3")); //ça marche

        //Enchainement des méthodes
        con.ouvrirUneConnexion(con.testerIdentifiants("POLOB", "mdp3"), "POLOB");
    }

}
