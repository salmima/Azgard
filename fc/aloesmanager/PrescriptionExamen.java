package fc.aloesmanager;

import java.util.ArrayList;
import java.util.Date;
import java.sql.*;

public class PrescriptionExamen {

    private Date date;
    Examen examen;
    private PH ph; //PH demandeur
    private String exigences_examen;
    private String id;
    private boolean done;
    //je sais pas si on remet la signature

     /**
     * Constructeur initialisant
     */
    public PrescriptionExamen() {
        this.date = null;
        this.examen =  null;
        this.ph =  null;
        this.exigences_examen =  null;
        this.id =  null;
        this.done = false;
    }
    
    /**
     * Constructeur
     */
    public PrescriptionExamen(Date date, Examen examen, PH ph, String exigences, String id, boolean done){
        this.date = date;
        this.examen = examen;
        this.ph = ph;
        this.exigences_examen = exigences;
        this.id = id;
        this.done= done;
    }
    
     /**
     * Retourne le PH
     */
    public PH getPH(){
        return this.ph;
    }
    
    /**
     * Retourne la date de prescription
     */
    public String getID() {
        return this.id;
    }
    
    
    /**
     * Retourne le libellé de l'examen
     */
    public String getLibelleExamen() {
        return this.examen.getLibelle();
    }
    
    /**
     * Retourne la date de prescription de l'examen
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Retourne les exigences de l'examen
     */
    public String getExigences() {
        return this.exigences_examen;
    }
    
     /**
     * Retourne le boolean
     */
    public boolean getDone() {
        return this.done;
    }
    
    /**
     * Ajout d'une prescription d'examen
     */
    public void creerUnePrescriptionExamen(Examen examen, String id, String n_rpps, String exigences_examen) { //id du patient, identifiant de la personne connectée
        //L'interface envoie des informations
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement creerPresExam = null;
        ResultSet resultats_bd = null;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        String n_rpps = PH.returnNRPPS(identifiant);
        
        try {
            String requete = "INSERT INTO Prescription_examen VALUES(? , ? , ?, ?, ?, NULL)";
            creerPresExam = con.prepareStatement(requete);
            creerPresExam.setDate(1, sqlDate);
            creerPresExam.setString(2, examen.getLibelle());
            creerPresExam.setString(3, n_rpps);
            creerPresExam.setString(4, id);
            creerPresExam.setString(5, exigences_examen);
            int nbMaj = creerPresExam.executeUpdate();
            System.out.println("nb mise a jour = " + nbMaj); //affiche le nombre de mises à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<PrescriptionExamen> retourneListePrescriptionExamen(String IPP, String secteur) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement recherchePres = null;
        ResultSet resultats_bd = null;
        ArrayList<PrescriptionExamen> listePres = new ArrayList<PrescriptionExamen>(); //on retourne une liste d'examens

        //----------- Requête 1: recherche du DMT Biologie
        try {
            recherchePres = con.prepareStatement("select * from (Prescription_examen natural join personnelMedical) where id = ? and lower(service)=? ORDER BY date_pres desc");
            recherchePres.setString(1, IPP);
            recherchePres.setString(2, secteur.toLowerCase());
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------parcours des données retournées
        //---Variables temporaires
        Date r_date = null;
        Examen r_examen = null;
        String r_exam;
        String r_exigences_examen = null;
        String r_nrpps = null;
        PH r_ph = new PH();

        try {
            while (resultats_bd.next()) {
                r_exam = resultats_bd.getString("examen");
                r_exigences_examen = resultats_bd.getString("exigences_examen");
                r_nrpps = resultats_bd.getString("n_rpps");

                try {
                    //Conversion du service type String en type Service
                    r_examen = Examen.valueOf(r_exam);

                    //On cherche le PH
                    r_ph = r_ph.rechercherUnMedecinRPPS(r_nrpps);

                    if (r_examen != null && r_ph != null) {
                        listePres.add(new PrescriptionExamen(r_date, r_examen, r_ph, r_exigences_examen, IPP, false));
                    }
                } catch (Exception e) {
                    System.out.println("");
                }
            }
            resultats_bd.close();
        } catch (SQLException e) {
            do {
                System.out.println("Accès aux résultats refusé 2");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        return listePres;
    }
    
    
    /**
     * Méthode renvoie la liste des prescriptions Cherche les prescriptions pour
     * un patient dans un service donné Méthode à utiliser pour automatiser le
     * remplissage de la lettre de sortie secteur est le secteur de la personne
     * connectée
     */
    public static ArrayList<PrescriptionExamen> retourneListePrescriptionExamen(String IPP, String secteur) {
        Connection con = ConnexionBDD.obtenirConnection();
        PreparedStatement recherchePres = null;
        ResultSet resultats_bd = null;
        ArrayList<PrescriptionExamen> listePres = new ArrayList<PrescriptionExamen>(); //on retourne une liste d'examens

        //----------- Requête 1: recherche du DMT Biologie
        try {
            recherchePres = con.prepareStatement("select * from (Prescription_examen natural join personnelMedical) where id = ? and lower(service)=? ORDER BY date_pres desc");
            recherchePres.setString(1, IPP);
            recherchePres.setString(2, secteur.toLowerCase());
        } catch (Exception e) {
            System.out.println("Erreur de requête 1");
        }

        //-----------parcours des données retournées
        //---Variables temporaires
        Date r_date = null;
        Examen r_examen = null;
        String r_exam;
        String r_exigences_examen = null;
        String r_nrpps = null;
        PH r_ph = new PH();

        try {
            while (resultats_bd.next()) {
                r_exam = resultats_bd.getString("examen");
                r_exigences_examen = resultats_bd.getString("exigences_examen");
                r_nrpps = resultats_bd.getString("n_rpps");

                try {
                    //Conversion du service type String en type Service
                    r_examen = Examen.valueOf(r_exam);

                    //On cherche le PH
                    r_ph = r_ph.rechercherUnMedecinRPPS(r_nrpps);

                    if (r_examen != null && r_ph != null) {
                        listePres.add(new PrescriptionExamen(r_date, r_examen, r_ph, r_exigences_examen, IPP, false));
                    }
                } catch (Exception e) {
                    System.out.println("");
                }
            }
            resultats_bd.close();
        } catch (SQLException e) {
            do {
                System.out.println("Accès aux résultats refusé 2");
                System.out.println("SQLState : " + e.getSQLState());
                System.out.println("Description : " + e.getMessage());
                System.out.println("code erreur : " + e.getErrorCode());
                System.out.println("");
                e = e.getNextException();
            } while (e != null);
        }

        return listePres;
    }
    
    public static void main(String[] args) {
        PrescriptionExamen pres = new PrescriptionExamen();
//        pres.creerUnePrescriptionExamen(Examen.valueOf("radiologie"), "180000111", "GREGH", "test Prescription"); //ça marche
    }
}
