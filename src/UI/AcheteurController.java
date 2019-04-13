package UI;
import SQL.SQLHelper;
import java.lang.String;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.postgresql.util.PSQLException;
import utils.Produit;
import utils.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;






public class AcheteurController implements Initializable {

    public SQLHelper SQL = new SQLHelper();

    //tab 1
    public Text affiche_requette;
    public Text boutique_achet;
    public Text titre_achet;
    public Text condition_achet;
    public Text adresse_achet;
    public Text adresse_achet2;
    public Text description_achet;
    public Text offre_ouvert;
    public Text offre_rejete;
    public Text offre_accepte;
    public Text offre_ouvert1;
    public Text offre_rejete1;
    public Text offre_accepte1;
    public Text textprix;
    public ChoiceBox categorie_ajouter;
    public boolean existe;
    public Button offre_boutton;
    public TextField offre_prix;
    public Label offre_confirmation;
    public CheckBox afficheNego;
    public boolean affiche_nego_active;
    private ObservableList<ObservableList> data;
    public  TableView tableview;
    public ChoiceBox requetBox;


    // tab 3

   // ArrayList<Produit> listProduits = new ArrayList();
    @FXML
    ListView<String> list_produit_vendeur = new ListView<String>();
    public ArrayList<Integer> idProduit;
    public ArrayList<String> nomProduit;
    public ArrayList<String> nomCategorie;
    public ArrayList<String> requette1;
    //private Object ResultTableModel;


    public void deconnection( javafx.event.ActionEvent event){

        Parent loginPage = null ;


        try {
            loginPage = FXMLLoader.load(getClass().getResource("login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene home_page_scene = new Scene(loginPage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();

    }
    public ArrayList<String> getCategories() throws SQLException{
       nomCategorie = new ArrayList<String>();
        ResultSet categories = SQL.getCategories();
        nomCategorie.add("Tout");
        while (categories.next()) {
            nomCategorie.add(categories.getString("nom"));
        }
        return nomCategorie;
    }

    public void remplirRequetteBox(){
        requette1 = new ArrayList<String>();
        requette1.add("Nom, prenom, courriel de l’acheteur dont le produit achete proviens de la ville ('Florenceville') et de la catégorie ('Telephones')");
        requette1.add("courriel de tous les experts ayant estime les produits de l’utilisateur'placerat@lacinia.co.uk'");
        requette1.add("5 plus grands vendeurs sur la plateforme (par nb de ventes ventes) de la ville'Florenceville'\n" +
                "de la derniere annee (a partir de la date actuelle)");
        requette1.add("courriel du vendeur ou tous les produits qui ont ete vendus se trouvaient a Florenceville");
        requette1.add("l'historique des estimations de tous les produits evalues et leur decision, avec leur prix de vente si il y a lieu");
        requette1.add("retournant l'historique des ventes d'un vendeur specifie");
        requette1.add("liste des produits de la categorie specifiee et dont le prix est inferieur a 10");
        requette1.add("retournant les produits en estimé par l'expert specife");
        requette1.add("retournant les produits en achetes de l'acheteur specife");
        requette1.add("tous les produits en vente de la boutique specifee");
        requetBox.setItems(FXCollections.observableArrayList(requette1));
        //requetBox.setValue(nomCategorie.get(0));



    }
    public boolean getProduct(String cat) {
        if(cat.equals("Tout") || cat.isEmpty()){
            getProductnonCategorie();
        }
        else{

        ArrayList<String> nomCategorie = new ArrayList<String>();
        ResultSet produit;
        if(affiche_nego_active)
            produit = SQL.getProductCategorieNego(cat,User.email);
        else
            produit = SQL.getProductCategorie(cat,User.email);

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
        ResultSet produit;
        if(affiche_nego_active)
            produit = SQL.getProductAfficheNego(User.email);
        else
            produit = SQL.getProductAffiche(User.email);


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
        boutique_achet.setText("");
        titre_achet.setText("");
          condition_achet.setText("");
          adresse_achet.setText("");
          adresse_achet2.setText("");
          description_achet.setText("");

    }
    public void updatePrix(Integer ProduitId){
        ResultSet rPrixNego = SQL.getPrixInfoNego(ProduitId.toString(),User.email);
        String ouvert = "";
        String accepte = "";
        String rejete = "";

        try {
            while(rPrixNego.next()){
                String prix = rPrixNego.getString(1);


                String etatoffre = rPrixNego.getString(2);

                if(etatoffre == null)
                    ouvert += prix;
                else
                    if(etatoffre.equals("rejete"))
                        rejete += prix + "; ";
                    else
                        accepte += prix;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        offre_ouvert1.setText(ouvert);
        offre_rejete1.setText(rejete);
        offre_accepte1.setText(accepte);
    }
    public void updateDescription(){
        Integer ProduitId = idProduit.get( list_produit_vendeur.getSelectionModel().getSelectedIndex());
        offre_prix.setText("");
        ResultSet rProduit = SQL.getProduitAfficheInfo(ProduitId.toString());
        if(affiche_nego_active){
            updatePrix(ProduitId);
        }

        try {
            rProduit.next();
            String boutique = rProduit.getString(1);
            String titre  = rProduit.getString(2);
            String etat  = rProduit.getString(3);
            String description = rProduit.getString(4);
            String norue= rProduit.getString(5);
            String nomrue = rProduit.getString(6);
            String codePost = rProduit.getString(7);
            String ville = rProduit.getString(8);
            offre_confirmation.setText("");
            boutique_achet.setText(boutique);
            titre_achet.setText(titre);
            condition_achet.setText(etat);
            adresse_achet.setText(norue +" "+ nomrue);
            adresse_achet2.setText(codePost + "  "+ ville);
            description_achet.setText(description);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setTextView(boolean isNego){
            textprix.setVisible(!isNego);
            offre_prix.setVisible(!isNego);
            offre_boutton.setVisible(!isNego);
            offre_confirmation.setVisible(!isNego);
            affiche_nego_active = isNego;
            offre_ouvert.setVisible(isNego);
            offre_rejete.setVisible(isNego);
            offre_accepte.setVisible(isNego);
             offre_accepte1.setVisible(isNego);
            offre_ouvert1.setVisible(isNego);
            offre_rejete1.setVisible(isNego);
            ChangerAffichageProduit();
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
    public void  negocier() throws IOException , SQLException {
        String courriel = User.email;
        String offre = offre_prix.getText().toString();
        try {
            double value = Double.parseDouble(offre);
            if(value<0)
                   offre_confirmation.setText(value + " est negative");
            else {
                Integer ProduitId = idProduit.get(list_produit_vendeur.getSelectionModel().getSelectedIndex());
                ResultSet resultat = SQL.negocier(ProduitId.toString(), courriel, offre);
                //ResultSet resultat = SQL.testnegocier();
                resultat.next();
                String reponse = resultat.getString(1);
                offre_confirmation.setText(reponse +"\n Offre:"+offre+"$");
                offre_prix.setText("");
                ChangerAffichageProduit();
            }


        } catch (NumberFormatException e) {
            offre_confirmation.setText(offre + " n'est pas un nombre");
        }



    }
    public void ChangerAffichageProduit(){
        //viderText();
        String categorie = categorie_ajouter.getValue().toString();

        boolean existe = getProduct(categorie);
        if(existe){
            afficherNomProduit();
        }else{
            list_produit_vendeur.getItems().clear();
            list_produit_vendeur.getItems().add("Aucun produit en vente");
        }
    }

    public void buildData(ResultSet rs){
        //Connection c ;

        data = FXCollections.observableArrayList();
        try{
            // c = DBConnect.connect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            //String SQL = "SELECT * from users";
            //ResultSet

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableview.getColumns().addAll(col);
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        affiche_nego_active = false;
        offre_ouvert.setVisible(false);
        offre_rejete.setVisible(false);
        offre_accepte.setVisible(false);
        try {
            ArrayList<String> nomCategorie= this.getCategories();
            categorie_ajouter.setItems(FXCollections.observableArrayList(nomCategorie));
            categorie_ajouter.setValue(nomCategorie.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        remplirRequetteBox();

        getProductnonCategorie();

        afficherNomProduit();

        list_produit_vendeur.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(existe) {
                    viderText();
                    updateDescription();
                }
                else{
                    viderText();
                    updateDescription();
                }
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
                offre_prix.setText("");

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
        requetBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                ResultSet resultat = null;
                System.out.print(number2.intValue());
                switch(number2.intValue()) {
                    case 0:
                        resultat = SQL.requetteComplexe1();
                        break;
                    case 1:
                        resultat = SQL.requetteComplexe2();
                        break;
                    case 2:
                        resultat = SQL.requetteComplexe3();
                        break;
                    case 3:
                        resultat = SQL.requetteComplexe4();
                        break;
                    case 4:
                        resultat = SQL.requetteComplexe5();
                        break;
                    case 5:
                        resultat = SQL.requetteSimple8();
                        break;
                    case 6:
                        resultat = SQL.requetteSimple7();
                        break;
                    case 7:
                        resultat = SQL.requetteSimple3();
                        break;
                    case 8:
                        resultat = SQL.requetteSimple2();
                        break;
                    case 9:
                        resultat = SQL.requetteSimple1();
                        break;

                }
                tableview.getColumns().clear();
                buildData(resultat);


            }
        });
        offre_boutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(existe) {
                    try {
                        negocier();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    offre_confirmation.setText("Veuillez choisir un article");
                }}
        });

        afficheNego.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                setTextView(newValue);
               if(newValue){
                   ChangerAffichageProduit();
               }
               else{
                   ChangerAffichageProduit();
               }
            }
        });
    }


}




