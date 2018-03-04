package fc.aloesmanager;

import java.sql.*;
import java.util.ArrayList;

public class DossierTemporaire {

    private String id_urgence;
    private String nom;
    private String prenom;
    private String sexe;
    private Date dateNaissance;
    private String moyenArrivee;
    private Date dateArrivee;
    private String nom_proche;
    private String n_tel_proche;
    private DossierMedicoTechnique dmt;

    /**
     * Constructeur initialisant
     */
    public DossierTemporaire() {
        this.id_urgence = null;
        this.nom = null;
        this.prenom = null;
        this.sexe = null;
        this.dateNaissance = null;
        this.moyenArrivee = null;
        this.dateArrivee = null;
        this.n_tel_proche = null;
        this.nom_proche = null;
        this.dmt = null;
    }

    /**
     * Constructeur complet
     */
    public DossierTemporaire(String id_urgence, String nom, String prenom, String sexe, Date dateNaissance, String moyenArivee, Date dateArrivee, String n_tel_proche, String nom_proche, DossierMedicoTechnique dmt) {
        this.id_urgence = id_urgence;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.n_tel_proche = n_tel_proche;
        this.nom_proche = nom_proche;
        this.moyenArrivee = moyenArrivee;
        this.dateArrivee = dateArrivee;
        this.dmt = dmt;
    }

    
    
    /**
     * Recherche d'un DM Temporaire à partir d'un identifiant de patient des urgences
     */
    //ATTENTION: vérifier que la personne connectée est un PH et est urgentiste (services "urgences"
    //La vérification peut se faire dans l'interface, par le fait quu'après sa connexion, on lui affiche cette option
    public void rechercherUnDMTemporaire(String id_urgence, String identifiant) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheID = null;
        PreparedStatement rechercheExamen = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        ResultSet resultats_bd2 = null;

        //----------- Requêtes
        //Requête 1: informations du DM temporaire
        try {
            rechercheID = con.prepareStatement("SELECT * FROM dossier_urgence WHERE id_urgence =  ?");
            rechercheID.setString(1, id_urgence);
        } catch (Exception ee) {
            System.out.println("Erreur de requête 1");

            //-----------Accès à la base de données
            try {
                resultats_bd = rechercheID.executeQuery();
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
            //---Variables temporaires /**
            try {
                while (resultats_bd.next()) {
                    //Informations du patient
                    this.id_urgence = resultats_bd.getString("id_urgence");
                    this.nom = resultats_bd.getString("nom");
                    this.prenom = resultats_bd.getString("prenom");
                    this.sexe = resultats_bd.getString("sexe");
                    this.dateNaissance = resultats_bd.getDate("dateNaissance");
                    this.nom_proche = resultats_bd.getString("nom_proche");
                    this.n_tel_proche = resultats_bd.getString("tel_proche");
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
            
             //Requête 2: recherche du DMT du patient (contenant tous les DMT)
             DossierMedicoTechnique dmt = new DossierMedicoTechnique();
             dmt.creationDMT(id_urgence, identifiant); //tous les DMT du patient passé aux urgences sont dans le DossierMedicoTechnique

             //Requête 3: rechercher tous les DM clinique
             DossierMedical dm = new DossierMedical();
             dm.creationDMclinique(id_urgence, identifiant); //identifiant de la personne connectée
            
 
            }
        }
    }

