package ru.demkin.gd.controller;

import com.sun.jna.Memory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.demkin.gd.services.AddressService;
import ru.demkin.gd.utils.JNACore;
import ru.demkin.gd.utils.MemoryUtils;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Created by evgen1000end on 13.07.2015.
 */
public class MainController implements Initializable {

    AddressService addressService = new AddressService();


    @FXML
    private Button myButton;

    @FXML
    private TextField myField;

    @FXML
    private TextField attrField;



    public void initialize(URL location, ResourceBundle resources) {

        myField.setText(String.valueOf(addressService.getSkill()));

        myButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                addressService.setSkill(MemoryUtils.intToBytes(Integer.parseInt(myField.getText())));

            }
        });
    }
}
