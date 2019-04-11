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
    public boolean existe;
    // tab 3

    ArrayList<Produit> listProduits = new ArrayList();
    @FXML
    ListView<String> list_produit_vendeur = new ListView<String>();
    public ArrayList<Integer> idProduit;
    public ArrayList<String> nomProduit;
    public ArrayList<String> nomCategorie;


    public ArrayList<String> getCategories() throws SQLException{
       nomCategorie = new ArrayList<String>();
        ResultSet categories = SQL.getCategories();
        nomCategorie.add("Tout");
        while (categories.next()) {
            nomCategorie.add(categories.getString("nom"));
        }
        return nomCategorie;
    }


    public boolean getProduct(String cat) {
        if(cat.equals("Tout")){
            getProductnonCategorie();
        }
        else{

        ArrayList<String> nomCategorie = new ArrayList<String>();
        ResultSet produit = SQL.getProductCategorie(cat);
        if(produit != null){
            try {
                if(produit.next()){
                    existe = true;
                    idProduit = new ArrayList<Integer>();
                    nomProduit = new ArrayList<String>();
                    idProduit.add(produit.getInt(1));
                    nomProduit.add(produit.getString(2));
                    while (produit.next()) {
                        idProduit.add(produit.getInt(1));
                        nomProduit.add(produit.getString(2));
                    }
                }
                else{
                    existe = false;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }}

    }
        return existe;
    }
    public void getProductnonCategorie() {
        existe = true;
        idProduit = new ArrayList<Integer>();
        nomProduit = new ArrayList<String>();
        ResultSet produit = SQL.getProductAffiche();

            try {
                while (produit.next()) {
                    idProduit.add(produit.getInt(1));
                    nomProduit.add(produit.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }



    }
    public void viderText(){
        titre_afficher.setText("");
        etatvente_afficher.setText("");
        codepostal_afficher.setText("");
        ville_afficher.setText("");
        norue_afficher.setText("");
        etatvente_afficher.setText("");
        condition_afficher.setText("");
    }
    public void updateDescription(){
        Integer ProduitId = idProduit.get( list_produit_vendeur.getSelectionModel().getSelectedIndex());

        ResultSet rProduit = SQL.getProduitVendeur2(ProduitId.toString());
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

       // updateDescription();
    }
    public void afficherNomProduit(){
        if (nomProduit.size() != 0) {
            ObservableList<String> items = FXCollections.observableArrayList(nomProduit);
            list_produit_vendeur.setItems(items);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ArrayList<String> nomCategorie= this.getCategories();
            categorie_ajouter.setItems(FXCollections.observableArrayList(nomCategorie));
            categorie_ajouter.setValue(nomCategorie.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        getProductnonCategorie();

        afficherNomProduit();

        list_produit_vendeur.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(existe)
                    updateDescription();
            }
        });
        list_produit_vendeur.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(existe)
                    updateDescription();
            }

        });
        categorie_ajouter.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                viderText();
                String categorie = categorie_ajouter.getItems().get((Integer) number2).toString();
                boolean existe = getProduct(categorie);
                if(existe){
                    afficherNomProduit();
                }else{
                        list_produit_vendeur.getItems().clear();
                        list_produit_vendeur.getItems().add("Aucun produit en vente");
                }


            }
        });

    }


    @FXML
    public void handleMouseClick(javafx.event.ActionEvent event) throws IOException {
        System.out.println("yo");
    }


}




