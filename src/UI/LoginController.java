package UI;
import SQL.SQLHelper;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import utils.User;
public class LoginController implements Initializable {

    public SQLHelper SQL = new SQLHelper();
    public TextField email_field;
    public PasswordField password_field;
    public Button submit_button;
    public ChoiceBox user_type;
    public Label invalid_label;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String st[] = { "Acheteur", "Vendeur"};
        user_type.setItems(FXCollections.observableArrayList(st));
        user_type.setValue("Acheteur");

    }

    public void login(javafx.event.ActionEvent event) throws IOException {
        String email = email_field.getText();
        String password = password_field.getText();

        String userType=user_type.getValue().toString();



        if (SQL.isValidLogin(userType,email,password))
        {
            User.email=email;
            User.password=password;

            Parent home_page_parent = null ;


            if(userType.equals("Acheteur")){
                home_page_parent = FXMLLoader.load(getClass().getResource("acheteur.fxml"));
            }
            else {
                home_page_parent = FXMLLoader.load(getClass().getResource("vendeur.fxml"));

            }



            Scene home_page_scene = new Scene(home_page_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            app_stage.hide();
            app_stage.setScene(home_page_scene);
            app_stage.show();
            // setting global u

        }
        else
        {
            email_field.clear();
            password_field.clear();
            invalid_label.setText("désolé erreur d'identification");
        }
        

}


 

}

