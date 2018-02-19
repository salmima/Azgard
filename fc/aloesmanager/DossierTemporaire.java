package fc.aloesmanager;

import java.util.Date;
import java.sql.*;

public class DossierTemporaire {

    private String nom;

    private String prenom;

    private String sexe;

    private Date dateNaissance;

    private int identifiiantUrgence;

    private String moyenArrivee;

    private DossierMedical dossierMedical;

    private DossierMedicoTechnique dossierMT;

    private Date dateArrivee;

    private Consultation venue;

}
