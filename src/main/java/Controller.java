import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    ListView < String > clientList = new ListView < String > ();

    @FXML
    ListView < String > itemsList = new ListView < String > ();
    @FXML
    private TextField text;

    @FXML
    private TextField sendMes;

    @FXML
    private VBox v;

    @FXML
    private Button edit;
    private static Stage scene;
    public Client client;

    private String clientString = "Show Clients";
    private String messageString = "Show Text";
    int num = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clients();
        String s = scene.getTitle().toString();
        System.out.println(s);
        text.setPromptText("Please enter name and press send");
        clientList.setBlendMode(BlendMode.MULTIPLY);
        clientList.setPrefSize(600, 600);
        clientList.setMaxSize(600, 600);
    }

    public void clients() {
        client = new Client(data -> {
            Platform.runLater(() -> {
                itemsList.getItems().add(data.toString());
            });
        }, data -> {
            Platform.runLater(() -> {
                clientList.getItems().add(data.toString());
            });
        }, data -> {
            Platform.runLater(() -> {
                clientList.getItems().clear();
            });
        });

        client.start();
    }

    public void clientButton() {
        String s = edit.getText();
        if (s.equals(clientString)) {
            v.getChildren().remove(0);
            v.getChildren().add(0, clientList);
            edit.setText("Show Messages");

        } else if (s.equals(messageString)) {
            v.getChildren().remove(0);
            v.getChildren().add(0, itemsList);
            edit.setText("Show Clients");

        }

    }

    public void sendText(ActionEvent e) throws IOException {
        String s = text.getText();

        if (s.equals("")) {

        } else {
            text.clear();
            if (num == 0) {

                scene.setTitle(s);
                text.setPromptText("Hello Human");
                sendMes.setPromptText("Please Send a message: ");
                client.send(s);
                num = num + 1;
            } else {
                client.send(s);
            }
        }
    }

}