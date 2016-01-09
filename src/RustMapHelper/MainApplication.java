/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper;

import RustMapHelper.models.Data;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author joeca_000
 */
public class MainApplication extends Application {

    MainStackPane msp;
    public static final Color TRANSPARENT_COLOR = Color.TRANSPARENT;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Rust Map Helper");
        Data.getInstance().openDataFile("data.yml");
        stage.setScene(Root.getInstance().getScene());
        stage.show();
    }

}
