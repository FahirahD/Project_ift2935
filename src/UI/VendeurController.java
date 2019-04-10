package UI;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
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

public class VendeurController  implements Initializable {

    public SQLHelper SQL = new SQLHelper();
    ArrayList<Produit> listProduits = new ArrayList();
    @FXML
    ListView<String> list_produit_vendeur = new ListView<String>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResultSet result = SQL.getBoutique(User.email);
        try {
            result.next();
            User.boutique = result.getString(1);

            System.out.println(User.boutique);

            ResultSet resultProduit = SQL.getProduitVendeur(User.boutique);
            initListProduit(resultProduit);
        } catch (SQLException e) {
            e.printStackTrace();
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
                System.out.println("clicked on " + list_produit_vendeur.getSelectionModel().getSelectedIndex());
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




