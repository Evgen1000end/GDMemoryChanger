package ru.demkin.gd.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.demkin.gd.utils.JNACore;
import ru.demkin.gd.utils.MemoryUtils;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Created by evgen1000end on 13.07.2015.
 */
public class MainController implements Initializable {

    final static long baseAddress = 0x002291E0;
    final static long[] offsets = new long[]{0x68, 0x34C, 0xA74};


    private static JNACore jnaCore;

    @FXML
    private Button myButton;

    @FXML
    private TextField myField;


    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public void initialize(URL location, ResourceBundle resources) {

        myButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                byte [] value = MemoryUtils.intToBytes(Integer.parseInt(myField.getText()));
                reverse(value);

                jnaCore = JNACore.getInstance();
                jnaCore.getFirstProcesses("Grim Dawn.exe");


              //  MemoryUtils.ChangeValue("Grim Dawn", baseAddress, offsets,value );
            }
        });
    }
}
