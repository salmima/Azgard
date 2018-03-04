package fc.aloesmanager;

public class DossierMedicoTechnique {

    private String observations;
    private DMT_Hematologie dh;
    private DMT_Anapath dap;
    private DMT_Biologie db;
    private Anesthesie da;
    private DMT_Radio dr;
    
    /**
     * Constructeur initialisant
     */
    public DossierMedicoTechnique(){
        this.observations = null;
        this.dh = null;
        this.dap = null;
        this.db = null;
        this.da = null;
        this.dr = null;
    }
    
    /**
     * Ajout d'un DMT Hematologie
     */
    public void ajouterDMTHemato(DMT_Hematologie dmt){
        this.dh = dmt;
    }
    
    /**
     * Ajout d'un DMT Anatomopathologie
     */
    public void ajouterDMTAnapath(DMT_Anapath dmt){
        this.dap = dmt;
    }
    
    /**
     * Ajout d'un DMT Biologie
     */
    public void ajouterDMTBiologie(DMT_Biologie dmt){
        this.db = dmt;
    }
    
    /**
     * Ajout d'un DMT Anesthesie
     */
    public void ajouterDMTAnesthesie(DMT_Anesthesie dmt){
        this.da = dmt;
    }
    
    /**
     * Ajout d'un DMT Radio
     */
    public void ajouterDMTRadio(DMT_Radio dmt){
        this.dr = dmt;
    }
    
    /**
     * Retourne d'un DMT Radio
     */
    public DMT_Radio getDMTRadio(){
        return this.dr;
    }
    
    /**
     * Retourne d'un DMT Biologie
     */
    public DMT_Biologie getDMTBiologie(){
        return this.db;
    }
    
    /**
     * Retourne d'un DMT Anesthesie
     */
    public DMT_Anesthesie getDMTAnesthesie(){
        return this.da;
    }
    
    /**
     * Retourne d'un DMT Hematologie
     */
    public DMT_Hematologie getDMTHematologie(){
        return this.dh;
    }
    
    /**
     * Retourne d'un DMT Anapath
     */
    public DMT_Anapath getDMTAnapath(DMT_Anapath dmt){
        return this.dap;
    }
    
    /**
     * Ajout de tous les DMT d'un patient passé aux urgencss
     */
    public void creationDMT(String id, String identifiant){ //identifiant de l'urgentiste
        //Ajout de chaque DMT
        DMT_Hematologie dmt_h = new DMT_Hematologie();
        dmt_h.rechercherUnDMHematologie(id, identifiant);
        this.dh = dmt_h;
        
        DMT_Anapath dmt_ap = new DMT_Anapath();
        dmt_ap.rechercherUnDMAnapath(id, identifiant);
        this.dap = dmt_ap;
        
        DMT_Biologie dmt_b = new DMT_Biologie();
        dmt_b.rechercherUnDMBiologie(id, identifiant);
        this.db = dmt_b;
        
        DMT_Radio dmt_r = new DMT_Radio();
        dmt_r.rechercherUnDMRadio(id, identifiant);
        this.dr = dmt_r;
        
        DMT_Anesthesie dmt_a = new DMT_Anesthesie();
        dmt_a.rechercherUnDMAnesthesie(id, identifiant);
        this.da = dmt_a;             
    }
    
    
}

