package ru.demkin.gd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import ru.demkin.gd.utils.Kernel32;
import ru.demkin.gd.utils.MemoryUtils;
import ru.demkin.gd.utils.User32;

import java.nio.ByteBuffer;


public class MainApp extends Application {

    final static long baseAddress = 0x002291E0;
    final static long[] offsets = new long[]{0x68, 0x34C, 0xA74};


    public static void main(String[] args) throws Exception {
        //launch(args);

        MemoryUtils.ChangeValue("Grim Dawn", baseAddress, offsets, new byte[]{0x05,0x00,0x00,0x00});
    }


    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/hello.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("JavaFX and Maven");
        stage.setScene(new Scene(root));


        stage.show();
    }
}