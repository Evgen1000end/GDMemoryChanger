package ru.demkin.gd;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/hello.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Grim Dawn Trainer");

//        Label nameLbl = new Label("Enter your name:");
//        TextField nameFld = new TextField();
//
//        Label msg = new Label();
//        msg.setStyle("-fx-text-fill: blue;");
//
//        Button sayHelloBtn = new Button("Say Hello");
//        Button exitBtn = new Button("Exit");
//
//        // Add the event handler for the Say Hello button
//        sayHelloBtn.setOnAction(e -> {
//            String name = nameFld.getText();
//            if (name.trim().length() > 0) {
//                msg.setText("Hello " + name);
//            } else {
//                msg.setText("Hello there");
//            }
//        });
//
//
//        exitBtn.setOnAction(e -> Platform.exit());
//
//        VBox box = new VBox();
//
//        box.setSpacing(5);
//
//        box.getChildren().addAll(nameLbl, nameFld, msg, sayHelloBtn, exitBtn);
//
//        Scene scene = new Scene(box, 400, 150);
//        stage.setScene(scene);

        stage.setScene(new Scene(root));

        stage.show();
    }
}