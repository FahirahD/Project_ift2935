package UI;

import SQL.SQLHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import utils.Produit;
import utils.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AcheteurController implements Initializable {

    public SQLHelper SQL = new SQLHelper();
    //tab 1
    public Text titre_afficher;
    public Text condition_afficher;
    public Text etatvente_afficher;
    public Text adresse_afficher;
    public Text norue_afficher;
    public Text ville_afficher;
    public Text codepostal_afficher;
    //tab 2
    public TextField titre_ajouter;
    public ChoiceBox condition_ajouter;
    public ChoiceBox categorie_ajouter;
    public TextArea description_ajouter;
    public TextField norue_ajouter;
    public TextField nomrue_ajouter;
    public TextField codepostal_ajouter;
    public TextField ville_ajouter;
    public Button button_ajouter_produit;
    public TextField prix_ajouter;
    // tab 3

    ArrayList<Produit> listProduits = new ArrayList();
    @FXML
    ListView<String> list_produit_vendeur = new ListView<String>();
    public ArrayList<String> idProduit;
    public ArrayList<String> nomProduit;


    public ArrayList<String> getCategories() throws SQLException{
        ArrayList<String> nomCategorie = new ArrayList<String>();
        ResultSet categories = SQL.getCategories();
        while (categories.next()) {
            nomCategorie.add(categories.getString("nom"));
        }
        return nomCategorie;
    }


    public void getProduct(String cat) {
        ArrayList<String> nomCategorie = new ArrayList<String>();
        ResultSet categories = SQL.getProductCategorie(cat);
        idProduit = new ArrayList<String>();
        nomProduit = new ArrayList<String>();

        while (true) {
            try {
                while (categories.next()) {
                    idProduit.add(categories.getString("id_prod"));
                    nomProduit.add(categories.getString("titre"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }
    public void getProductnonCategorie() {
        ResultSet produit = SQL.getProductAffiche();
        idProduit = new ArrayList<String>();
        nomProduit = new ArrayList<String>();

        while (true) {
            try {
                while (produit.next()) {
                    idProduit.add(produit.getString("id_prod"));
                    nomProduit.add(produit.getString("titre"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }
    public void updateDescription(){
        String ProduitId = idProduit.get( list_produit_vendeur.getSelectionModel().getSelectedIndex());

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

    public void change_cat(){

        getProduct(categorie_ajouter.getValue().toString());

        list_produit_vendeur.setItems(FXCollections.observableArrayList(nomProduit));

        updateDescription();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        list_produit_vendeur.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updateDescription();
            }
        });
        list_produit_vendeur.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            updateDescription();
            }

        });

    }


    @FXML
    public void handleMouseClick(javafx.event.ActionEvent event) throws IOException {
        System.out.println("yo");
    }

    public void  ajouterProduit(){
        String boutique = User.boutique;
        String titre = titre_ajouter.getText();
        String categorie = categorie_ajouter.getValue().toString();
        String condition = condition_ajouter.getValue().toString();
        String description= description_ajouter.getText();
        String norue = norue_ajouter.getText();
        String nomrue = nomrue_ajouter.getText();
        String codepostal= codepostal_ajouter.getText();
        String ville = ville_ajouter.getText();
        String prix = prix_ajouter.getText();

        System.out.println(boutique+titre+categorie+condition+description+norue+nomrue+codepostal+ville+prix);

        SQL.ajouterProduitVendeur(boutique,categorie,titre,prix,condition,description,norue,nomrue,codepostal,ville);


    }

    public void initListProduit(ResultSet produit) throws SQLException {

        while (produit.next()) {
            Produit produit1 = new Produit(produit.getString(1), produit.getString(2));
            listProduits.add(produit1);
        }
    }
}




