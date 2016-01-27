package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by devninja on 7.1.16..
 */
public class MainScreenController implements Initializable
{
    @FXML Button main_bt_ChooseTeam;
    @FXML Button main_bt_GitHub;
    @FXML Button main_bt_Exit;
    @FXML TextField tf;

    @FXML
    private void main_loadTeamChooserViewAction(ActionEvent event) throws IOException
    {
        // switches to ChooseTeam.fxml
        Stage stage;
        Parent root;

        //get reference to the button's stage
        stage = (Stage) main_bt_ChooseTeam.getScene().getWindow();

        root = FXMLLoader.load(getClass().getResource("../view/ChooseTeam.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void main_visitGitHub(ActionEvent event)
    {
        // opens the browser and sends the user to github page
    }

    @FXML
    private void main_exit(ActionEvent event)
    {
        // terminates the application
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
}
