package ru.demkin.gd.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.demkin.gd.services.AddressService;
import ru.demkin.gd.utils.MemoryUtils;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    AddressService addressService;

    @FXML
    private Button myButton;

    @FXML
    private Label status;

    @FXML
    private TextField myField;

    @FXML
    private TextField attrField;

    public void initialize(URL location, ResourceBundle resources) {

        addressService = new AddressService();

        if (addressService.isDetectGame) {


            myField.setText(String.valueOf(addressService.getSkill()));
            attrField.setText(String.valueOf(addressService.getAttribute()));

            myButton.setOnAction(event ->
            {
                addressService.setSkill(MemoryUtils.intToBytes(Integer.parseInt(myField.getText())));
                addressService.setAttribute(MemoryUtils.intToBytes(Integer.parseInt(attrField.getText())));

            });
        }
        else
        {
            status.setText("Game not init!");
        }


    }
}
