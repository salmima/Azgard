package fc.aloesmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import org.w3c.dom.Document;

public class DossierMedicoAdministratif {

    private String IPP;
    private String nom;
    private String prenom;
    private int idSIR;
    private String sexe;
    private Date dateNaissance;
    private String nom_proche;
    private String n_tel_proche;
    private String n_tel;
    private String adresse;
    private String groupeSanguin;
    private MedecinTraitant medTraitant;
    private ArrayList<Venue> listeVenue;

    //Constructeur par défaut
    public DossierMedicoAdministratif() {
        this.IPP = null;
        this.nom = null;
        this.prenom = null;
        this.n_tel = null;
        this.sexe = null;
        this.dateNaissance = null;
        this.adresse = null;
        this.n_tel_proche = null;
        this.nom_proche = null;
        this.groupeSanguin = null;
        this.listeVenue = new ArrayList();
    }

    //Constructeur complet
    public DossierMedicoAdministratif(String IPP, String nom, String prenom, String n_tel, String sexe, Date dateNaissance, String adresse, String groupeSanguin, MedecinTraitant medTt, String n_tel_proche, String nom_proche) {
        this.IPP = IPP;
        this.nom = nom;
        this.prenom = prenom;
        this.n_tel = n_tel;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.groupeSanguin = groupeSanguin;
        this.n_tel_proche = n_tel_proche;
        this.nom_proche = nom_proche;
        this.medTraitant = medTt;
        this.listeVenue = new ArrayList();
    }

    public void ajouterUneVenue(Venue venue) {
        listeVenue.add(venue);
    }

    public String getIPP() {
        return this.IPP;
    }

    public MedecinTraitant getMedecinTraitant() {
        return this.medTraitant;
    }

    public DossierMedicoAdministratif rechercherUnDMA(String ipp) {
        Connection con = null;
        PreparedStatement rechercheIPP = null;
        PreparedStatement rechercheIDSIR = null;
        PreparedStatement rechercheMedTt = null;
        PreparedStatement rechercheVenue = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        ResultSet resultats_bd2 = null;
        ResultSet resultats_bd3 = null;
        ResultSet resultats_bd4 = null;

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
            String base1 = "SIH";
            String DBurl1 = "jdbc:mysql://localhost:3306/" + base1 + "?verifyServerCertificate=false&useSSL=true";
            con = DriverManager.getConnection(DBurl1, "root", "choco"); //remplacer le mot de passe
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
        //Requête 1: informations du DMA
        try {
            rechercheIPP = con.prepareStatement("SELECT * FROM DMA WHERE deces = 0 and IPP =  ?");
            rechercheIPP.setString(1, ipp);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //Requête 2: récupérer l'idSIR s'il existe
        try {
            rechercheIDSIR = con.prepareStatement("SELECT idSIR FROM correspondanceDMA_SIR where IPP =  ?");
            rechercheIDSIR.setString(1, ipp);
        } catch (Exception e) {
            System.out.println("Erreur de requête 2");
        }

        //Requête 3: récupérer le médecin traitant s'il existe
        try {
            rechercheMedTt = con.prepareStatement("SELECT * FROM correspondanceMedecinExterne natural join MedecinExterne where IPP =  ?"); //à débugger
            rechercheMedTt.setString(1, ipp);
        } catch (Exception e) {
            System.out.println("Erreur de requête 3");
        }

        //Requête 4: récupérer les venues
        try {
            rechercheVenue = con.prepareStatement("SELECT * FROM (CorrespondanceDMA_Hospitalisation natural join Venue) natural join CorrespondancePH_Venue natural join LocalisationPatient where id =  ?"); //à débugger
            rechercheVenue.setString(1, ipp);
        } catch (Exception e) {
            System.out.println("Erreur de requête 4");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheIPP.executeQuery();
            resultats_bd2 = rechercheIDSIR.executeQuery();
            resultats_bd3 = rechercheMedTt.executeQuery();
            resultats_bd4 = rechercheVenue.executeQuery();
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
        String r_IPP = null;
        String r_nom = null;
        String r_prenom = null;
        String r_idSIR = null;
        String r_sexe = null;
        Date r_dateNaissance = null;
        String r_nom_proche = null;
        String r_n_tel_proche = null;
        String r_n_tel = null;
        String r_adresse = null;
        String r_groupeSanguin = null;
        MedecinTraitant r_medTraitant = null;
        DossierMedicoAdministratif dmaTest = null;
        String r_medAdresse = null;
        String r_medNom = null;
        String r_medPrenom = null;
        String r_medNtel = null;
        int r_medCodePostal = 0;
        ArrayList<Venue> r_liste = new ArrayList();

        try {
            while (resultats_bd.next()) {
                //Informations du patient
                r_IPP = resultats_bd.getString("IPP");
                r_nom = resultats_bd.getString("nom");
                r_prenom = resultats_bd.getString("prenom");
                r_sexe = resultats_bd.getString("sexe");
                r_dateNaissance = resultats_bd.getDate("dateNaissance");
                r_nom_proche = resultats_bd.getString("nom_proche");
                r_n_tel_proche = resultats_bd.getString("tel_proche");
                r_n_tel = resultats_bd.getString("telephone_personnel");
                r_adresse = resultats_bd.getString("adresse");
                r_groupeSanguin = resultats_bd.getString("groupe_sanguin");
            }
            while (resultats_bd2.next()) {
                r_idSIR = resultats_bd2.getString("idSIR");
            }
            while (resultats_bd3.next()) {
                //Informations du médecin
                r_medAdresse = resultats_bd3.getString("adresse");
                r_medNom = resultats_bd3.getString("nom");
                r_medPrenom = resultats_bd3.getString("prenom");
                r_medNtel = resultats_bd3.getString("telephone_pro");
                r_medCodePostal = resultats_bd3.getInt("codePostal");
            }

            //Fermeture des résultats des requêtes
            resultats_bd.close();
            resultats_bd2.close();
            resultats_bd3.close();

            //Création de l'instance du médecin traitant
            if (r_medNom != null && r_medPrenom != null && r_medCodePostal != 0 && r_medNtel != null && r_medAdresse != null) {
                r_medTraitant = new MedecinTraitant(r_medAdresse, r_medNom, r_medPrenom, r_medNtel, r_medCodePostal);
            } else {
                r_medTraitant = null;
            }

            //Création du DMA
            dmaTest = new DossierMedicoAdministratif(r_IPP, r_nom, r_prenom, r_n_tel, r_sexe, r_dateNaissance, r_adresse, r_groupeSanguin, r_medTraitant, r_n_tel_proche, r_nom_proche);

            while (resultats_bd4.next()) {
                //Liste des venues
                String r_numSejour;
                Date r_dateEntree;
                Date r_dateSortie;
                String r_lettreSortie;
                String nrpps;
                PH r_PHrespo;
                String r_num_lit;

                //On récupère la première venue
                r_numSejour = resultats_bd4.getString("num_sejour");
                r_dateEntree = resultats_bd4.getDate("date_entree");
                r_dateSortie = resultats_bd4.getDate("date_sortie");
                r_lettreSortie = resultats_bd4.getString("ref_doc_lettre_sortie");
                nrpps = resultats_bd4.getString("n_rpps");
                r_num_lit = resultats_bd4.getString("num_lit");

                //On cherche le PH
                r_PHrespo = new PH();
                r_PHrespo = r_PHrespo.rechercherUnMedecinRPPS(nrpps);

                //On crée le lit 
                Lit lit = new Lit(true, r_num_lit);

                //On crée une venue
                if (r_num_lit != null) {
                    Hospitalisation h1 = new Hospitalisation(r_numSejour, r_dateEntree, r_dateSortie, r_lettreSortie, r_PHrespo, lit);
                    dmaTest.ajouterUneVenue(h1);
                } else {
                    Consultation c1 = new Consultation(r_numSejour, r_dateEntree, r_dateSortie, r_lettreSortie, r_PHrespo);
                    dmaTest.ajouterUneVenue(c1);
                }
            }

            resultats_bd4.close();

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

        return dmaTest;
    }

    public DossierMedicoAdministratif rechercherUnDMA(String nom, String prenom, Date dateN) {
        Connection con = null;
        PreparedStatement rechercheIPP = null;
        PreparedStatement rechercheIDSIR = null;
        PreparedStatement rechercheMedTt = null;
        PreparedStatement rechercheVenue = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        ResultSet resultats_bd2 = null;
        ResultSet resultats_bd3 = null;
        ResultSet resultats_bd4 = null;

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
            String base1 = "SIH";
            String DBurl1 = "jdbc:mysql://localhost:3306/" + base1 + "?verifyServerCertificate=false&useSSL=true";
            con = DriverManager.getConnection(DBurl1, "root", "choco"); //remplacer le mot de passe
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
        //Requête 1: informations du DMA
        try {
            rechercheIPP = con.prepareStatement("SELECT * FROM DMA WHERE deces = 0 and lower(nom) =  ? and lower(prenom) = ? and dateNaissance = ?");
            rechercheIPP.setString(1, nom.toLowerCase());
            rechercheIPP.setString(2, prenom.toLowerCase());
            rechercheIPP.setDate(3, dateN);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données: requête 1
        try {
            resultats_bd = rechercheIPP.executeQuery();
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

        //-----------parcours des données retournées: requête 1
        //---Variables temporaires
        String r_IPP = null;
        String r_sexe = null;
        String r_nom_proche = null;
        String r_n_tel_proche = null;
        String r_n_tel = null;
        String r_adresse = null;
        String r_groupeSanguin = null;
        MedecinTraitant r_medTraitant = null;
        DossierMedicoAdministratif dmaTest = null;

        try {
            while (resultats_bd.next()) {
                //Informations du patient
                r_IPP = resultats_bd.getString("IPP");
                r_sexe = resultats_bd.getString("sexe");
                r_nom_proche = resultats_bd.getString("nom_proche");
                r_n_tel_proche = resultats_bd.getString("tel_proche");
                r_n_tel = resultats_bd.getString("telephone_personnel");
                r_adresse = resultats_bd.getString("adresse");
                r_groupeSanguin = resultats_bd.getString("groupe_sanguin");
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

        //Requête 2: récupérer l'idSIR s'il existe
        try {
            rechercheIDSIR = con.prepareStatement("SELECT idSIR FROM correspondanceDMA_SIR where IPP =  ?");
            rechercheIDSIR.setString(1, r_IPP);
        } catch (Exception e) {
            System.out.println("Erreur de requête 2");
        }

        //Requête 3: récupérer le médecin traitant s'il existe
        try {
            rechercheMedTt = con.prepareStatement("SELECT * FROM correspondanceMedecinExterne natural join MedecinExterne where IPP =  ?"); //à débugger
            rechercheMedTt.setString(1, r_IPP);
        } catch (Exception e) {
            System.out.println("Erreur de requête 3");
        }

        //Requête 4: récupérer les venues
        try {
            rechercheVenue = con.prepareStatement("SELECT * FROM (CorrespondanceDMA_Hospitalisation natural join Venue) natural join CorrespondancePH_Venue natural join LocalisationPatient where id =  ?"); //à débugger
            rechercheVenue.setString(1, r_IPP);
        } catch (Exception e) {
            System.out.println("Erreur de requête 4");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd2 = rechercheIDSIR.executeQuery();
            resultats_bd3 = rechercheMedTt.executeQuery();
            resultats_bd4 = rechercheVenue.executeQuery();
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
        String r_idSIR = null;
        String r_medAdresse = null;
        String r_medNom = null;
        String r_medPrenom = null;
        String r_medNtel = null;
        int r_medCodePostal = 0;
        ArrayList<Venue> r_liste = new ArrayList();

        try {
            while (resultats_bd2.next()) {
                r_idSIR = resultats_bd2.getString("idSIR");
            }
            while (resultats_bd3.next()) {
                //Informations du médecin
                r_medAdresse = resultats_bd3.getString("adresse");
                r_medNom = resultats_bd3.getString("nom");
                r_medPrenom = resultats_bd3.getString("prenom");
                r_medNtel = resultats_bd3.getString("telephone_pro");
                r_medCodePostal = resultats_bd3.getInt("codePostal");
            }

            //Fermeture des résultats des requête
            resultats_bd2.close();
            resultats_bd3.close();

            //Création de l'instance du médecin traitant
            if (r_medNom != null && r_medPrenom != null && r_medCodePostal != 0 && r_medNtel != null && r_medAdresse != null) {
                r_medTraitant = new MedecinTraitant(r_medAdresse, r_medNom, r_medPrenom, r_medNtel, r_medCodePostal);
            } else {
                r_medTraitant = null;
            }

            //Création du DMA
            dmaTest = new DossierMedicoAdministratif(r_IPP, nom, prenom, r_n_tel, r_sexe, dateN, r_adresse, r_groupeSanguin, r_medTraitant, r_n_tel_proche, r_nom_proche);

            while (resultats_bd4.next()) {
                //Liste des venues
                String r_numSejour;
                Date r_dateEntree;
                Date r_dateSortie;
                String r_lettreSortie;
                String nrpps;
                PH r_PHrespo;
                String r_num_lit;

                //On récupère la première venue
                r_numSejour = resultats_bd4.getString("num_sejour");
                r_dateEntree = resultats_bd4.getDate("date_entree");
                r_dateSortie = resultats_bd4.getDate("date_sortie");
                r_lettreSortie = resultats_bd4.getString("ref_doc_lettre_sortie");
                nrpps = resultats_bd4.getString("n_rpps");
                r_num_lit = resultats_bd4.getString("num_lit");

                //On cherche le PH
                r_PHrespo = new PH();
                r_PHrespo = r_PHrespo.rechercherUnMedecinRPPS(nrpps);

                //On crée le lit 
                Lit lit = new Lit(true, r_num_lit);

                //On crée une venue
                if (r_num_lit != null) {
                    Hospitalisation h1 = new Hospitalisation(r_numSejour, r_dateEntree, r_dateSortie, r_lettreSortie, r_PHrespo, lit);
                    dmaTest.ajouterUneVenue(h1);
                } else {
                    Consultation c1 = new Consultation(r_numSejour, r_dateEntree, r_dateSortie, r_lettreSortie, r_PHrespo);
                    dmaTest.ajouterUneVenue(c1);
                }
            }

            //On ferme les résultats
            resultats_bd4.close();

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

        return dmaTest;
    }

    //Cherche le lit du patient. Méthode à combiner avec la méthode InformationsSecteur dans la classe Secteur pour avoir toutes les informations
    public Lit localiserUnPatient(String nom, String prenom, Date dateN) {
        DossierMedicoAdministratif dma = new DossierMedicoAdministratif();
        dma = dma.rechercherUnDMA(nom, prenom, dateN);

        //On récupère toutes les hospitalisations
        ArrayList<Hospitalisation> listeHospi = new ArrayList();
        for (int i = 0; i < dma.listeVenue.size(); i++) {
            if (dma.listeVenue.get(i) instanceof Hospitalisation) {
                Hospitalisation h = (Hospitalisation) dma.listeVenue.get(i);
                listeHospi.add(h);
            }
        }

        //On cherche l'hospitalisation la plus récente, qui est en cours
        Lit lit = null;

        int j = 0;
        while (j < listeHospi.size() && listeHospi.get(j).getDateSortie() != null) {
            j++;
        }

        if (j < listeHospi.size()) {
            lit = listeHospi.get(j).getLocalisation();
        }

        return lit;
    }

    public void creerUnDMA(String IPP, String nom, String prenom, String sexe, Date dateN, String nom_proche, String n_tel_proche, String n_tel, String adresse, String groupeSanguin, MedecinTraitant medTt) {
        //L'interface envoie des informations
        //Création un peu particulière: la création du DMA crée aussi automatiquement la première venue
        //Voir la méthode créerUneVenue, à associer avec celle-ci lors de la création du DMA
        //Création de l'une ou l'autre instance en fonction du type spécifié dans l'interface par l'utilisateur

        Connection con = null;
        PreparedStatement creerDMA = null;
        PreparedStatement creerMTT = null;

        //-----------Connexion
        //Chargement du pilote
        try {
            Class.forName("com.mysql.jdbc.Driver"); //charge le pilote et crée une instance de cette classe
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Erreur: Class Not Found");
        }

        //-----------Etablissement de la connexion
        try {
            String base1 = "SIH";
            String DBurl1 = "jdbc:mysql://localhost:3306/" + base1 + "?verifyServerCertificate=false&useSSL=true";
            con = DriverManager.getConnection(DBurl1, "root", "choco"); //remplacer le mot de passe
        } catch (java.sql.SQLException e) {
            do {
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        //Ajout d'un DMA
        try {
            String requete = "INSERT INTO DMA VALUES(? , ? , ? , ? , ? , ? , ? , ? , ? , 0 , ?)";
            creerDMA = con.prepareStatement(requete);
            creerDMA.setString(1, IPP);
            creerDMA.setString(2, nom);
            creerDMA.setString(3, prenom);
            creerDMA.setString(4, n_tel);
            creerDMA.setString(5, sexe);
            creerDMA.setDate(6, dateN);
            creerDMA.setString(7, nom_proche);
            creerDMA.setString(8, n_tel_proche);
            creerDMA.setString(9, adresse);
            creerDMA.setString(10, groupeSanguin);

            int nbMaj = creerDMA.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //lors de la création d'une table avec cette méthode, la méthode retourne 0 

        //Ajout d'un médecin traitant
        //Requête 3: récupérer le médecin traitant s'il existe
        ResultSet resultats_bd = null;
        MedecinTraitant r_medTraitant = null;

        try {
            creerMTT = con.prepareStatement("SELECT * FROM MedecinExterne");
        } catch (Exception e) {
            System.out.println("Erreur de requête");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = creerMTT.executeQuery();
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
        String r_medNom = null;
        String r_medPrenom = null;
        int r_medCodePostal = 0;

        try {
            while (resultats_bd.next()) {
                //Informations du médecin
                r_medNom = resultats_bd.getString("nom");
                r_medPrenom = resultats_bd.getString("prenom");
                r_medCodePostal = resultats_bd.getInt("codePostal");
            }

            //Fermeture des résultats des requête
            resultats_bd.close();

            //Création de l'instance du médecin traitant
            if (r_medNom != null && r_medPrenom != null && r_medCodePostal != 0) {
                r_medTraitant = new MedecinTraitant(null, r_medNom, r_medPrenom, null, r_medCodePostal);
            } else {
                r_medTraitant = null;
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
        if (r_medTraitant.getNom() != medTt.getNom() && r_medTraitant.getPrenom() != medTt.getPrenom() && r_medTraitant.getCodePostal() != medTt.getCodePostal()) {
            try {
                String requete = "INSERT INTO MedecinExterne VALUES (?,?,?,?,?)";
                creerMTT = con.prepareStatement(requete);
                creerMTT.setString(1, medTt.getNom());
                creerMTT.setString(2, medTt.getPrenom());
                creerMTT.setString(3, medTt.getNtel());
                creerMTT.setString(4, medTt.getAdresse());
                creerMTT.setInt(5, medTt.getCodePostal());

                int nbMaj = creerMTT.executeUpdate();
                System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //Ajout de la correspondance avec le médecin traitant
        try {
            String requete = "INSERT INTO CorrespondanceMedecinExterne VALUES (?,?,?,?)";
            creerMTT = con.prepareStatement(requete);
            creerMTT.setString(1, IPP);
            creerMTT.setString(2, medTt.getNom());
            creerMTT.setString(3, medTt.getPrenom());;
            creerMTT.setInt(4, medTt.getCodePostal());

            int nbMaj = creerMTT.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//TEST
    public static void main(String[] args) {
        DossierMedicoAdministratif dma = new DossierMedicoAdministratif();
        Date date1 = new Date(56, 9, 28);
        Date date = new Date(55, 9, 28);
        Date dateTest = new Date(100, 06, 22);

        dma.rechercherUnDMA("465782439");
        dma.rechercherUnDMA("Gates", "Bill", date1);
        dma.localiserUnPatient("Gates", "Bill", date); //trouve un lit: normal car hospitalisation en cours
        dma.localiserUnPatient("Jobs", "Steve", date1); //ne trouve pas de lit: normal car pas d'hospitalisation en cours
        MedecinTraitant medTest = dma.rechercherUnDMA("465782439").getMedecinTraitant();
        //dma.creerUnDMA("123456789", "Test", "Raoul", "M", dateTest, "Test Christine", "0666996699", "0688991122", "32 adresse de test", "B+", medTest);
        
    }
}
