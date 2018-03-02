/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager; //cette classe sera à mettre dans l'UI!

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
    protected String typePersonnel;
    protected String secteur;

    public Connexion() {
        this.identifiant = null;
        this.typePersonnel = null;
        this.secteur = null;
    }
    
    //pour se connecter: utiliser testerIdentifiants
    //puis ouvrir une connexion avec ouvrirUneConnexion
    //Enchainement des méthodes
    //ouvrirConnexion(testerIdentifiants("id","mdp"), "id")
    //si testerIdentifiants renvoie false: mettre des messages d'erreur
    public boolean testerIdentifiants(String identifiant, String mdp) { //l'identifiant et le mdp correspondent à un getText() de l'interface
        boolean connexionOK = false;
        Connection con = null;
        PreparedStatement rechercheOK = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //-----------Connexion
        //Chargement du pilote
        try {
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        }

        //-----------Etablissement de la connexion
        try {
            //il faut instancier un objet de la classe Connexion en précisant l'URL de la base
            String base1 = "azgardengineering_sih";
            String DBurl1 = "jdbc:mysql://mysql-azgardengineering.alwaysdata.net/" + base1 + "?verifyServerCertificate=false&useSSL=true";
            con = DriverManager.getConnection(DBurl1, "154118", "choco"); //remplacer le mot de passe
        } catch (java.sql.SQLException e) {
            do {
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

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
        Connection con = null;
        PreparedStatement rechercheType = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête

        //-----------Connexion
        //Chargement du pilote
        try {
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        }

        //-----------Etablissement de la connexion
        try {
            //il faut instancier un objet de la classe Connexion en précisant l'URL de la base
            String base1 = "azgardengineering_sih";
            String DBurl1 = "jdbc:mysql://mysql-azgardengineering.alwaysdata.net/" + base1 + "?verifyServerCertificate=false&useSSL=true";
            con = DriverManager.getConnection(DBurl1, "154118", "choco"); //remplacer le mot de passe
        } catch (java.sql.SQLException e) {
            do {
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //----------- Requêtes
        try {
            rechercheType = con.prepareStatement("select typePersonnel,service from correspondanceId natural join personnelMedical where identifiant_connexion = ?");
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
        String r_typePersonnel = null;
        String r_service = null;

        try {
            while (resultats_bd.next()) {
                r_typePersonnel = resultats_bd.getString("typePersonnel");
                r_service = resultats_bd.getString("service");
                this.typePersonnel = r_typePersonnel;
                this.secteur = r_service;
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

        //Mettre une méthode: en fonction de r_typePersonnel, affichage différent
    }

    public static void main(String[] args) {
        Connexion con = new Connexion();
//        System.out.println(testerIdentifiants("POLOB", "mdp3")); //ça marche

        //Enchainement des méthodes
        con.ouvrirUneConnexion(con.testerIdentifiants("POLOB", "mdp3"), "POLOB");
    }

}
