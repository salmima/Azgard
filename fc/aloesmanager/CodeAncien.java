/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fc.aloesmanager;

/**
 *
 * @author Thao
 */
public class CodeAncien {
    //            //La méthode à l'ancienne
//            ResultSetMetaData rsmd = resultats_bd.getMetaData();
//            int nbCols = rsmd.getColumnCount();
//            ArrayList<String> liste = new ArrayList();
//            while (resultats_bd.next()) {
//                for (int i = 1; i <= nbCols; i++) {
//                    liste.add(resultats_bd.getString(i));
//                }
//                System.out.println();
//            }
//
//            //On convertit la date en String en type Date
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = null;
//
//            try {
//                date = sdf.parse(liste.get(5));
//            } catch (Exception e) {
//                System.out.println("erreur");
//            }
//            //On crée le DMA de test
//            DossierMedicoAdministratif dmaTest = new DossierMedicoAdministratif(Integer.parseInt(liste.get(0)), liste.get(1), liste.get(2), Integer.parseInt(liste.get(3)), liste.get(4), date, liste.get(6));
//            //fin de la méthode à l'ancienne
}
