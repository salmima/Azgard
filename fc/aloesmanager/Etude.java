package fc.aloesmanager;

import java.util.ArrayList;
import java.util.Date;
import java.sql.*;

public class Etude {

    private String intitule;

    private int duree;

    private Date dateDebut;

    private Date dateFinPrevue;

    public boolean estFinie;

    private String modalite;

    private PH PHrespo;

    private ArrayList<Protocole> listeProtocole;

}
