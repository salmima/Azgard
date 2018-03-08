/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.w3c.dom.Document;

/**
 *
 * @author Thao
 */
public class Impression {

    /**
     * Méthode permettant de créer une chaîne de caractères contenant le XML à transformer en PDF
     */
    //Il faut donner un document: il faut utiliser une des classes Impression_... puis faire instance.getDocument() à passer en paramètre à cette méthode
    public static String creationStringXML(Document document) {
        //Instanciation des objets nécessaires
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (Exception e) {
            System.out.println("erreur");
        }

        //Transformation du document
        final DOMSource source = new DOMSource(document);

        StringWriter writer = new StringWriter();
        final StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println("erreur");
        }

        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        String strResult = writer.toString();
        return strResult;
    }

    /**
     * Méthode permettant de transformer un fichier XML en PDF
     * utile pour la lettre de sortie uniquement
     */
    //Le chemin xsl vaut src/xml/nomxsl.xsl
    public static void xmlToPdf(String path_xsl, String path_xml, String nomFichier) {
        try {
            // Setup input and output files
            File xmlfile = new File(path_xml);
            File xsltfile = new File(path_xsl);
            File pdffile = new File("src/output/" + nomFichier + ".pdf");
            OutputStream out = new FileOutputStream(pdffile);
            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File("src/libraries/fop.xconf"));

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired

            TransformerFactory tFactory = TransformerFactory.newInstance();

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            //Setup input
            Source src = new StreamSource(xmlfile);

            //Setup Transformer
            Source xsltSrc = new StreamSource(xsltfile);
            Transformer transformer = tFactory.newTransformer(xsltSrc);

            //Make sure the XSL transformation's result is piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }
    
    /**
     * Méthode permettant de transformer une chaîne de caractères XML en PDF
     * utile pour imprimer les prescriptions et résultats
     */
    //Le chemin xsl vaut src/xml/nomxsl.xsl
    public static void strXMLToPDF(String path_xsl, String strXML, String nomFichier) { 
        try {
            // Setup input and output files
            File xmlfile = new File(strXML);
            File xsltfile = new File(path_xsl);
            File pdffile = new File("src/output/" + nomFichier + ".pdf");
            OutputStream out = new FileOutputStream(pdffile);
            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File("src/libraries/fop.xconf"));

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired

            TransformerFactory tFactory = TransformerFactory.newInstance();

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            //Setup input
            Source src = new StreamSource(xmlfile);

            //Setup Transformer
            Source xsltSrc = new StreamSource(xsltfile);
            Transformer transformer = tFactory.newTransformer(xsltSrc);

            //Make sure the XSL transformation's result is piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    public static void main(String[] args) {
        Impression.xmlToPdf("src/xml/Impression_pres_res.xsl", "src/xml/presmedic.xml", "essai"); //ça marche
    }

}
