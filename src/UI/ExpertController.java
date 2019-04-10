
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
    public Text titre_afficher;
    public Text condition_afficher;
    public Text etatvente_afficher;
    public Text adresse_afficher;
    public Text norue_afficher;
    public Text ville_afficher;
    public Text codepostal_afficher;
    public ArrayList<String> idProduit;
    public ArrayList<String> nomProduit;
    public ArrayList<Float> prixSouhaite;
    public String courriel;

    ArrayList<Produit> listProduits = new ArrayList();
    @FXML
    ListView<String> list_produit_vendeur = new ListView<String>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idProduit= new ArrayList<String>();
        nomProduit= new ArrayList<String>();
        prixSouhaite = new ArrayList<Float>();
        ResultSet resultat = SQL.getBoutique(User.email);
        try {
           ;
            while (resultat.next()) {
                idProduit.add(resultat.getString("id_prod"));
                nomProduit.add(resultat.getString("titre"));
                prixSouhaite.add(resultat.getFloat("prix_souhaite"));
            }
        } catch (SQLException e ) {
            JDBCTutorialUtilities.printSQLException(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }


        String[] nomProduit = new String[listProduits.size()];

        for (int i = 0; i < listProduits.size(); i++) {
            nomProduit[i] = listProduits.get(i).getTitre();
        }
        System.out.println(nomProduit[0]);
        if (listProduits.size() != 0) {
            ObservableList<String> items = FXCollections.observableArrayList(nomProduit);
            list_produit_vendeur.setItems(items);
        }

        list_produit_vendeur.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String ProduitId = listProduits.get( list_produit_vendeur.getSelectionModel().getSelectedIndex()).getId();

                ResultSet rProduit = SQL.getProduitVendeur2(ProduitId);
                try {
                    rProduit.next();
                    String cat  = rProduit.getString(1);
                    String titre  = rProduit.getString(2);
                    String etat  = rProduit.getString(3);
                    String etatprod  = rProduit.getString(4);
                    String prix = rProduit.getString(5);
                    String description = rProduit.getString(6);
                    String norue= rProduit.getString(7);
                    String nomrue = rProduit.getString(8);
                    String codePost = rProduit.getString(9);
                    String ville = rProduit.getString(10);

                    titre_afficher.setText(titre);
                    etatvente_afficher.setText(titre);
                    codepostal_afficher.setText(codePost);
                    ville_afficher.setText(ville);
                    norue_afficher.setText(norue);
                    etatvente_afficher.setText(etatprod);
                    condition_afficher.setText(etat);




                } catch (SQLException e) {
                    e.printStackTrace();
                }



            }
        });

    }


    @FXML
    public void handleMouseClick(javafx.event.ActionEvent event) throws IOException {
        System.out.println("yo");
    }

    public void initListProduit(ResultSet produit) throws SQLException {

        while (produit.next()) {
            Produit produit1 = new Produit(produit.getString(1), produit.getString(2));
            listProduits.add(produit1);
        }
    }
}