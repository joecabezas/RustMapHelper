/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper;

import RustMapHelper.ui.TopBar;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 *
 * @author joeca_000
 */
public class Root {

    private static final Root INSTANCE = new Root();

    private Scene scene;
    private BorderPane border_pane;
    private TopBar top_bar;
    private MainStackPane main_stack_pane;
    
    private Label credits_label;

    private Root() {

        border_pane = new BorderPane();
        top_bar = new TopBar();
        scene = new Scene(border_pane, 800, 600);

        main_stack_pane = new MainStackPane();
        
        HBox credits_hbox = new HBox();
        credits_hbox.setPadding(new Insets(5));
        credits_label = new Label("RustMapHelper v0.1 by k014");
        //credits_label.setFont(new Font());
        credits_label.setTextFill(Color.WHITE);
        credits_hbox.getChildren().add(credits_label);

        border_pane.setCenter(main_stack_pane);
        border_pane.setTop(top_bar);
        border_pane.setBottom(credits_hbox);
        border_pane.setStyle("-fx-background-color: black");
    }

    public static Root getInstance() {
        return INSTANCE;
    }

    public Scene getScene() {
        return scene;
    }

    public MainStackPane getMainStackPane() {
        return main_stack_pane;
    }

    private EventHandler<KeyEvent> onKeyReleased = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            switch (e.getCode()) {
                case P:
                    save();
                    break;
            }
        }
    };

    public void save() {
        main_stack_pane.saveImage();
    }
}
