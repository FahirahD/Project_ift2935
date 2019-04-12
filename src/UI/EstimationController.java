package UI;
import SQL.SQLHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EstimationController implements Initializable {
    public TextField prixEstime;
    public Button estimationOk;
    public SQLHelper SQL = new SQLHelper();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Regex pour limiter le champ à un int
        prixEstime.textProperty().addListener(new ChangeListener<String>() {
            @Override

            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    prixEstime.setText(newValue.replaceAll("[^\\d]", ""));


                }
            }
        });




    }

    public void estimation_ok_press(javafx.event.ActionEvent event) throws IOException {
        String prix_estime = prixEstime.getText();
        if (!prixEstime.getText().equals("")) {
            Stage stage = (Stage) estimationOk.getScene().getWindow();
            SQL.estimer(VendeurController.idProdAEstime,prixEstime.getText().toString());
            stage.close();
        }

    }
}
