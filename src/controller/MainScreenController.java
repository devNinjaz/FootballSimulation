package controller;

import com.sun.org.apache.xpath.internal.SourceTree;
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
import org.omg.PortableInterceptor.ACTIVE;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
    @FXML TextField tf, tf_msgs;

    @FXML
    private void main_loadTeamChooserViewAction(ActionEvent event) throws IOException
    {
        // switches to ChooseTeam.fxml
        Stage stage;
        Parent root;

        //get reference to the button's stage
        stage = (Stage) main_bt_ChooseTeam.getScene().getWindow();

        root = FXMLLoader.load(new URL(Main.PathToViews + "/ChooseTeam.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void main_visitGitHub(ActionEvent event)
    {
        // opens the browser and sends the user to github page
        String url = "https://github.com/termNinja/FootballSimulation";
        tf_msgs.setText(url);
    }

    @FXML
    private void main_contactUs(ActionEvent event)
    {
        tf_msgs.setText("nmicovic@outlook.com, lrankovic@outlook.com");
    }

    @FXML
    private void main_aboutEvent(ActionEvent event)
    {
        tf_msgs.setText("Simulation of Football based on Automaton Theory, created for course \"Programing Paradigms\"");
    }

    @FXML
    private void main_exit(ActionEvent event)
    {
        // terminates the application
        System.out.println("Farewell young ninja!");
        System.exit(1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
}
