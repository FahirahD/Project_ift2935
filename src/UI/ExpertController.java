
package UI;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import utils.User;
import SQL.SQLHelper;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import utils.Produit;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownServiceException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExpertController  implements Initializable {

    public SQLHelper SQL = new SQLHelper();

    // list for estimates
    public ArrayList<String> idProduit;
    public ArrayList<String> nomProduit;
    public ArrayList<Float> prixSouhaite;

    //tab 1
    public Text titre_afficher;
    public Text condition_afficher;
    public Text etatvente_afficher;
    public Text adresse_afficher;
    public Text norue_afficher;
    public Text ville_afficher;
    public Text codepostal_afficher;



    public void init_estimate_lists() throws SQLException {
        ResultSet resultat = SQL.getProduitToEstimate();

        while (resultat.next()) {
            idProduit.add(resultat.getString("id_prod"));
            nomProduit.add(resultat.getString("titre"));
            prixSouhaite.add(resultat.getFloat("prix_souhaite"));
        }
    }

        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            idProduit = new ArrayList<String>();
            nomProduit = new ArrayList<String>();
            prixSouhaite = new ArrayList<Float>();
            try {
                this.init_estimate_lists();
            } catch (SQLException e) {
                e.printStackTrace();
            }



    }




    }
