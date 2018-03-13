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
    private DossierMedical dm;
    private Venue venue;

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
        this.venue = null;
    }

    /**
     * Constructeur complet
     */
    public DossierTemporaire(String id_urgence, String nom, String prenom, String sexe, Date dateNaissance, String moyenArrivee, Date dateArrivee, String n_tel_proche, String nom_proche, DossierMedicoTechnique dmt, Venue venue) {
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
        this.venue = venue;
    }

    /**
     * Ajout d'une venue
     */
    public void ajouterUneVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * Recherche d'un DM Temporaire à partir d'un identifiant de patient des
     * urgences
     */
    //ATTENTION: vérifier que la personne connectée est un PH et est urgentiste (services "urgences")
    //La vérification peut se faire dans l'interface, par le fait quu'après sa connexion, on lui affiche cette option
    public void rechercherUnDMTemporaire(String id_urgence, String identifiant) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheID = null;
        PreparedStatement rechercheVenue = null;
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
                    try {
                        this.id_urgence = resultats_bd.getString("id_urgence");
                    } catch (Exception e) {
                        System.out.println("");
                    }

                    if (this.id_urgence != null) {
                        //Informations du patient
                        this.nom = resultats_bd.getString("nom");
                        this.prenom = resultats_bd.getString("prenom");
                        this.sexe = resultats_bd.getString("sexe");
                        this.dateNaissance = resultats_bd.getDate("dateNaissance");
                        this.nom_proche = resultats_bd.getString("nom_proche");
                        this.n_tel_proche = resultats_bd.getString("tel_proche");
                    }

                    //Fermeture des résultats des requêtes
                    resultats_bd.close();
                }
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

            if (this.id_urgence != null) {
                //Requête 2: recherche du DMT du patient (contenant tous les DMT)
                DossierMedicoTechnique dmt = new DossierMedicoTechnique();
                dmt.creationDMT(id_urgence, identifiant); //tous les DMT du patient passé aux urgences sont dans le DossierMedicoTechnique

                //Requête 3: rechercher tous les DM clinique
                DossierMedical dm = new DossierMedical();
                dm.creationDMclinique(id_urgence, identifiant); //identifiant de la personne connectée

                //Requête 4: recherche de la venue
                try {
                    rechercheVenue = con.prepareStatement("SELECT * FROM (Venue natural join CorrespondancePH_Venue natural join LocalisationPatient) where id =  ?");
                    rechercheVenue.setString(1, id_urgence);
                } catch (Exception e) {
                    System.out.println("Erreur de requête 4");
                }

                //-----------Accès à la base de données
                try {
                    resultats_bd2 = rechercheVenue.executeQuery();
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
                String r_numSejour = "";
                Date r_date_entree = null;
                String nrpps;
                PH r_PHrespo;
                String r_num_lit;;
                try {
                    while (resultats_bd2.next()) {
                        r_numSejour = resultats_bd2.getString("num_sejour");
                        r_date_entree = resultats_bd2.getDate("date_entree");
                        nrpps = resultats_bd2.getString("n_rpps");
                        r_num_lit = resultats_bd2.getString("num_lit");

                        //On cherche le PH
                        r_PHrespo = new PH();
                        r_PHrespo.rechercherUnMedecinRPPS(nrpps);

                        //On crée le lit 
                        Lit lit = new Lit(true, r_num_lit);

                        //On crée une venue
                        if (r_num_lit != null) {
                            Hospitalisation h1 = new Hospitalisation(r_numSejour, r_date_entree, null, null, r_PHrespo, lit);
                            this.ajouterUneVenue(h1);
                        } else {
                            Consultation c1 = new Consultation(r_numSejour, r_date_entree, null, null, r_PHrespo);
                            this.ajouterUneVenue(c1);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erreur");
                }
            }
            else{
                System.out.println("Le dossier temporaire des urgences n'existe pas");
        }
    }
    }

    /**
     * Création d'un DM des urgences
     */
    //Je propose que la méthode pour générer l'id_urgence soit le même que pour les IPP
    //C'est à l'interface de génerer un IPP 
    public void creerUnDMTemporaire(String id_urgence, String nom, String prenom, String n_tel, String sexe, Date dateNaissance, String moyen_arrivee, String nom_proche, String tel_proche, String identifiant) { //identifiant de la personne connectée
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement creerDMTemporaire;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        String numSejour = Venue.creerUnNumSejour();
        String nrpps = PH.returnNRPPS(identifiant);

        //Requête 1: Ajout d'un DM des urgences
        //La vérification de non-existence du DM se fait dans la base de données
        try {
            String requete = "INSERT INTO dossier_urgence VALUES(? ,?,?,?,?,?,?,?,?)";
            creerDMTemporaire = con.prepareStatement(requete);
            creerDMTemporaire.setString(1, id_urgence);
            creerDMTemporaire.setString(2, nom);
            creerDMTemporaire.setString(3, prenom);
            creerDMTemporaire.setString(4, sexe);
            creerDMTemporaire.setDate(5, dateNaissance);
            creerDMTemporaire.setDate(6, sqlDate);
            creerDMTemporaire.setString(7, moyen_arrivee);
            creerDMTemporaire.setString(8, nom_proche);
            creerDMTemporaire.setString(9, tel_proche);

            int nbMaj = creerDMTemporaire.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Ajout de la traçabilité 
        PreparedStatement ajouterTrace = null;
        java.util.Date utilDate2 = new java.util.Date();
        java.sql.Date date_co = new java.sql.Date(utilDate2.getTime());

        try {
            String requete = "INSERT INTO Tracabilite VALUES(? , ? , ?)";
            ajouterTrace = con.prepareStatement(requete);
            ajouterTrace.setDate(3, date_co);
            ajouterTrace.setString(2, identifiant);
            ajouterTrace.setString(1, id_urgence);

            int nbMaj = ajouterTrace.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Requête 2: Création d'une seule venue!
        try {
            String requete = "INSERT INTO Venue VALUES(? ,?, NULl, NULL)";
            creerDMTemporaire = con.prepareStatement(requete);
            creerDMTemporaire.setString(1, numSejour);
            creerDMTemporaire.setDate(2, sqlDate);

            int nbMaj = creerDMTemporaire.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Requête 3: on ajoute la correspondance PH_Venue
        try {
            String requete = "INSERT INTO CorrespondancePH_Venue VALUES(? ,?,?)";
            creerDMTemporaire = con.prepareStatement(requete);
            creerDMTemporaire.setString(3, numSejour);
            creerDMTemporaire.setString(1, id_urgence);
            creerDMTemporaire.setString(2, nrpps);

            int nbMaj = creerDMTemporaire.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        DossierTemporaire dt = new DossierTemporaire();
        Date date = new Date(00, 9, 28);
        dt.creerUnDMTemporaire("111222444", "test", "urgence th", "0101202020", "M", date, "SAMU", "", "", "AIMD");
    }

}
