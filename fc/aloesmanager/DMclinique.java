package fc.aloesmanager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DMclinique {

    private String id;
    private Service service;
    private String observations;
    private ArrayList<PrescriptionMedicamenteuse> listePrescriptionMedic;
    private ArrayList<Resultat> listeResultats;
    public ArrayList<PrescriptionExamen> listePrescriptionExamen;

    /**
     * Constructeur initialisant
     */
    public DMclinique() {
        this.id = null;
        this.service = null;
        this.observations = "";
        this.listePrescriptionMedic = new ArrayList();
        this.listeResultats = new ArrayList();
        this.listePrescriptionExamen = new ArrayList();
    }

    /**
     * Constructeur complet
     */
    public DMclinique(String id, String obs, Service service) {
        this.id = id;
        this.service = service;
        this.observations = obs;
        this.listePrescriptionMedic = new ArrayList();
        this.listeResultats = new ArrayList();
        this.listePrescriptionExamen = new ArrayList();
    }

    /**
     * Mise à jour de l'identifiant du patient
     */
    public void setId(String id) {
        this.id = id;
    }

     /**
     * Mise à jour deS observations
     */
    public void setObs(String obs) {
        this.observations = obs;
    }

     /**
     * Mise à jour du service
     */
    public void setService(Service service) {
        this.service = service;
    }

     /**
     * Ajout d'une prescription d'examen
     */
    public void ajouterPrescriptionExam(PrescriptionExamen pres) {
        this.listePrescriptionExamen.add(pres);
    }

    /**
     * Ajout d'une prescription de médicaments
     */
    public void ajouterPrescriptionMedic(PrescriptionMedicamenteuse pres) {
        this.listePrescriptionMedic.add(pres);
    }
    
     /**
     * Ajout d'un résultat à la liste de résultats
     */
    public void ajouterResultat(Resultat resultat) {
        this.listeResultats.add(resultat);
    }
    
    /**
     * Récupération de la liste de prescription médicamenteuse
     */
    public ArrayList<PrescriptionMedicamenteuse> getListePresMedic(){
        return this.listePrescriptionMedic;
    }
    
    
    /**
     * Récupération de la liste de prescription d'examen
     */
    public ArrayList<PrescriptionExamen> getListePresExam(){
        return this.listePrescriptionExamen;
    }

    /**
     * Récupération de la liste de résultats
     */
    public ArrayList<Resultat> getListeResultat(){
        return this.listeResultats;
    }
    
    
    /**
     * Recherche d'un DM clinique d'un patient
     */
    public void rechercherUnDMClinique(String IPP, String identifiant) { //identifiant de la personne connectée
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement rechercheDMClinique = null;
        PreparedStatement recherchePrescriptionExamen = null;
        PreparedStatement recherchePrescriptionMedic = null;
        ResultSet resultats_bd = null; //ensemble des résultats retournés par la requête
        ResultSet resultats_bd2 = null;
        ResultSet resultats_bd3 = null;

        //----------- Requête 1
        try {
            rechercheDMClinique = con.prepareStatement("select * from DMclinique where id = ?");
            rechercheDMClinique.setString(1, IPP);
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------Accès à la base de données
        try {
            resultats_bd = rechercheDMClinique.executeQuery();
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
        String r_service = null;
        String r_observations = null;
        String r_id = null;
        Service serv = null;

        try {
            while (resultats_bd.next()) {
                r_observations = resultats_bd.getString("observations");
                r_service = resultats_bd.getString("nom_secteur");
                r_id = resultats_bd.getString("id");

                //Conversion du service type String en type Service
                try {
                    serv = Service.valueOf(r_service);
                } catch (Exception e) {
                    System.out.println("");
                }

                //Création partielle d'un DM clinique
                if (r_id != null && r_service != null) {
                    this.id = r_id;
                    this.observations = r_observations;
                    this.service = serv;
                }
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

        //Une fois que j'ai le nom du secteur, on récupère les autres documents éventuels (prescription, résultat...)
        if (this.service != null) {

            //----------- Requête 2
            try {
                recherchePrescriptionExamen = con.prepareStatement("select * from (Prescription_examen natural join personnelMedical) where id = ? and lower(service) = ?");
                recherchePrescriptionExamen.setString(1, IPP);
                recherchePrescriptionExamen.setString(2, service.getLibelle().toLowerCase());
            } catch (Exception e) {
                System.out.println("Erreur de requête 2");
            }

            //-----------Accès à la base de données
            try {
                resultats_bd2 = recherchePrescriptionExamen.executeQuery();
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

            //------------------ Ajout des prescriptions d'examen ------------------
            //-----------parcours des données retournées
            //---Variables temporaires
            Date r_date;
            Date r_date_sql;
            Examen r_examen;
            String r_exam;
            String r_exigences_examen = null;
            String r_nrpps = null;
            PH r_ph = new PH();

            try {
                while (resultats_bd2.next()) {
                    r_exam = resultats_bd2.getString("examen");
                    r_exigences_examen = resultats_bd2.getString("exigences_examen");
                    r_nrpps = resultats_bd2.getString("n_rpps");
                    r_date = resultats_bd2.getDate("date_pres");

                    //Conversion du service type String en type Service
                    r_examen = Examen.valueOf(r_exam);

                    //On cherche le PH
                    r_ph = r_ph.rechercherUnMedecinRPPS(r_nrpps);

                    try {
                        this.ajouterPrescriptionExam(new PrescriptionExamen(r_date, r_examen, r_ph, r_exigences_examen, IPP));
                    } catch (Exception e) {
                        System.out.println("");
                    }
                }

                resultats_bd2.close();
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

            //----------- Requête 3
            try {
                recherchePrescriptionMedic = con.prepareStatement("select * from (Prescription_medic natural join personnelMedical) where id = ? and lower(service) = ?");
                recherchePrescriptionMedic.setString(1, IPP);
                recherchePrescriptionMedic.setString(2, service.getLibelle().toLowerCase());
            } catch (Exception e) {
                System.out.println("Erreur de requête 3");
            }

            //-----------Accès à la base de données
            try {
                resultats_bd3 = recherchePrescriptionMedic.executeQuery();
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

            //------------------ Ajout des prescriptions de médicaments ------------------
            //-----------parcours des données retournées
            //---Variables temporaires
            Date r_date2;
            String r_nrpps2 = null;
            String r_listeMedic = null;
            PH r_ph2 = new PH();

            try {
                while (resultats_bd3.next()) {
                    r_date2 = resultats_bd3.getString("date_pres");
                    r_nrpps2 = resultats_bd3.getString("n_rpps");
                    r_listeMedic = resultats_bd3.getString("liste_medic");

                    //On cherche le PH
                    r_ph2 = r_ph2.rechercherUnMedecinRPPS(r_nrpps2);

                    try {
                        this.ajouterPrescriptionMedic(new PrescriptionMedicamenteuse(r_date2, r_ph2, r_listeMedic, IPP));
                    } catch (Exception e) {
                        System.out.println("");
                    }
                }

                resultats_bd3.close();
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

            //Ajout de la traçabilité 
            PreparedStatement ajouterTrace = null;
            java.util.Date utilDate2 = new java.util.Date();
            java.sql.Date date_co = new java.sql.Date(utilDate2.getTime());

            try {
                String requete = "INSERT INTO Tracabilite VALUES(? , ? , ?)";
                ajouterTrace = con.prepareStatement(requete);
                ajouterTrace.setDate(3, date_co);
                ajouterTrace.setString(2, identifiant);
                ajouterTrace.setString(1, IPP);

                int nbMaj = ajouterTrace.executeUpdate();
                System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
              //------------------ Ajout des résultats  ------------------
        }
    }

   
    /**
     * Création d'un DM clinique
     */
    public static void creerUnDM(String IPP, String nom_secteur) { //le nom du secteur est récupéré par les attributs de connexion
        //L'interface envoie des informations
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement creerDM = null;
        
        //Requête 1: Ajout d'un DM clinique
        //La non-vérification de l'existence du DM se fait dans la base de données
        try {
            String requete = "INSERT INTO DMclinique VALUES(? ,?, NULL )";
            creerDM = con.prepareStatement(requete);
            creerDM.setString(1, IPP);
            creerDM.setString(2, nom_secteur);
            int nbMaj = creerDM.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        DMclinique dm = new DMclinique();
        String listeMedic = "Liste de médicaments contre l'allergie";
        Examen exam = Examen.valueOf("radiologie");

//        dm.rechercherUnDMClinique("180000111", "GREGH"); //marche + ajouter traçabilite
//        dm.creerUnePrescriptionMedicamenteuse(listeMedic, "180000111","18945686235");
//        dm.creerUnePrescriptionExamen(exam, "180000111","18945686235","radio du thorax");
//        dm.creerUnDM("180000222","chirurgie_main_brules"); //marche
    }
}

