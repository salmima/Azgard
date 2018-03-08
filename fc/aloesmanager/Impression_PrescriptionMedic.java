/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.io.File;
import java.sql.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Thao
 */
public class Impression_PrescriptionMedic {

    private Document document;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Element racine;

    /* 
    * Constructeur initialisant
    */
    public Impression_PrescriptionMedic(PrescriptionMedicamenteuse pres) {
        this.document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            //Création d'un parseur
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Création d'un Document
        document = builder.newDocument();
        
        //Création de l'élément racine et ajout au document
        racine = document.createElement("prescription_medic");
        racine.setAttribute("xmlns:","http://azgardengineering.com/PrescriptionMedic"); //a finir
        racine.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance"); 
        racine.setAttribute("xsi:schemaLocation","http://azgardengineering.com/PrescriptionMedic PrescriptionMedicamenteuse.xsd"); //a finir
        document.appendChild(racine);
        
        //Création des éléments fils
        Element date = document.createElement("date_prescription");
        Element liste_medicaments = document.createElement("liste_medicaments");
        Element informationsPrescripteur = document.createElement("informationsPrescripteur");
        Element nom = document.createElement("nom");
        Element prenom = document.createElement("prenom");
        Element specialite = document.createElement("specialite");
        Element informationsPatient = document.createElement("informationsPatient");
        Element nomp = document.createElement("nom");
        Element prenomp = document.createElement("prenom");
        Element dateNaissance = document.createElement("date_naissance");
        
        //Ajout des valeurs
        date.appendChild(document.createTextNode(pres.getDate().toString()));
        liste_medicaments.appendChild(document.createTextNode(pres.getListe()));
        nom.appendChild(document.createTextNode(pres.getPH().getNom()));
        prenom.appendChild(document.createTextNode(pres.getPH().getPrenom()));
        specialite.appendChild(document.createTextNode(pres.getPH().getSpecialite()));

        DossierMedicoAdministratif dma = new DossierMedicoAdministratif();
        dma.rechercherUnDMA(pres.getID());
        nomp.appendChild(document.createTextNode(dma.getNom()));
        prenomp.appendChild(document.createTextNode(dma.getPrenom()));
        dateNaissance.appendChild(document.createTextNode(dma.getDateN().toString()));
        
        //Ajout des noeuds
        informationsPrescripteur.appendChild(nom);
        informationsPrescripteur.appendChild(prenom);
        informationsPrescripteur.appendChild(specialite);
        informationsPatient.appendChild(nomp);
        informationsPatient.appendChild(prenomp);
        informationsPatient.appendChild(dateNaissance);
        
        racine.appendChild(date);
        racine.appendChild(informationsPrescripteur);
        racine.appendChild(informationsPatient);
        racine.appendChild(liste_medicaments);
        
    }
    
    /* 
    * Renvoie l'objet Document de la lettre de sortie
    */
    public Document getDocument() {
        return this.document;
    }

    //Test
    public static void main(String[] args){
        //Création d'une prescription
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PH ph = new PH();
        ph = PH.rechercherUnMedecin("house", "gregory");
        PrescriptionMedicamenteuse pres = new PrescriptionMedicamenteuse(sqlDate, ph, "medic 1", "180000111");
        
        Impression_PrescriptionMedic imp = new Impression_PrescriptionMedic(pres);
        
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (Exception e) {
            System.out.println("erreur");
        }
        
        //Transformation
        final DOMSource source = new DOMSource(imp.getDocument());
        //Code à utiliser pour afficher dans un fichier
         final StreamResult sortie = new StreamResult(new File("C:\\Users\\Thao\\Documents\\NetBeansProjects\\TIS4\\AloesManager\\src\\xml\\presmedic.xml"));
        //final StreamResult result = new StreamResult(System.out);
        try {
            transformer.transform(source, sortie);
        } catch (Exception e) {
            System.out.println("erreur");
        }

        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
    }
    
}

