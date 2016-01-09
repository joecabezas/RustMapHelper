/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author joeca_000
 */
public class AddMapPieceStage extends Stage {

    File new_file;

    BorderPane border_pane;
    AddMapPieceStackPane add_map_piece_stack_pane;

    HBox hbox;
    Button btn_ok;

    public AddMapPieceStage() {
        System.out.println("AddMapPieceStage");
        setTitle("Add Map Piece");

        border_pane = new BorderPane();
        border_pane.setStyle("-fx-background-color: black");

        btn_ok = new Button("OK");
        btn_ok.setOnAction(onBtnOkAction);
        hbox = new HBox();
        hbox.getChildren().add(btn_ok);

        add_map_piece_stack_pane = new AddMapPieceStackPane();

        border_pane.setCenter(add_map_piece_stack_pane);
        border_pane.setTop(hbox);
        setScene(new Scene(border_pane, 800, 600));

        setOnCloseRequest(onCloseStage);
    }

    EventHandler<WindowEvent> onCloseStage = new EventHandler<WindowEvent>() {
        @Override
        public void handle(final WindowEvent e) {
            StageFactory.destroyAddMapPieceStage();
        }
    };

    EventHandler<ActionEvent> onBtnOkAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(final ActionEvent e) {
            Point2D pos = add_map_piece_stack_pane.image_new_view_preview.localToParent(Point2D.ZERO);
            Root.getInstance().getMainStackPane().AddNewMap(new_file, pos);
            StageFactory.destroyAddMapPieceStage();
        }
    };

    public void Open(File file) {
        new_file = file;
        add_map_piece_stack_pane.AddNewMapImage(new_file);
        show();
    }
    
    public void AddImage(Image image) {
        add_map_piece_stack_pane.AddNewMapImage(image);
        show();
    }

}
