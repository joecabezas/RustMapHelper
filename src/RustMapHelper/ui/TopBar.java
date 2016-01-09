/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper.ui;

import RustMapHelper.AddMapPieceStage;
import RustMapHelper.ImageUtils;
import RustMapHelper.Root;
import RustMapHelper.StageFactory;
import java.io.File;
import java.util.EnumSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author joeca_000
 */
public class TopBar extends HBox {

    final FileChooser file_chooser;
    Button btn_add_image;
    Button btn_add_image_from_clipboard;
    Button btn_save;

    public TopBar() {
        super(5);

        file_chooser = new FileChooser();
        configureFileChooser(file_chooser);

        btn_add_image = new Button("Add image from file");
        btn_add_image_from_clipboard = new Button("Paste image from clipboard");
        btn_save = new Button("Save");

        getChildren().add(btn_add_image);
        getChildren().add(btn_add_image_from_clipboard);
        getChildren().add(btn_save);

        btn_add_image.setOnAction(onBtnAddImageAction);
        btn_add_image_from_clipboard.setOnAction(onBtnAddImageFromClipboardAction);
        btn_save.setOnAction(onBtnSaveAction);
    }

    EventHandler<ActionEvent> onBtnAddImageAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(final ActionEvent e) {
            File file = file_chooser.showOpenDialog(null);
            if (file != null) {
                if (Root.getInstance().getMainStackPane().hasMapLoaded()) {
                    StageFactory.getAddMapPieceStage().Open(file);
                } else {
                    Root.getInstance().getMainStackPane().AddNewMap(file);
                    Root.getInstance().save();
                }
            }
        }
    };

    EventHandler<ActionEvent> onBtnAddImageFromClipboardAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(final ActionEvent e) {
            Image image_clipboard = ImageUtils.createImageFromClipboard(EnumSet.of(
                    ImageUtils.FLAGS.TRANSPARENT_IMAGE,
                    ImageUtils.FLAGS.TRIM_IMAGE,
                    ImageUtils.FLAGS.NEW_IMAGE
            ));

            if (image_clipboard != null) {
                if (Root.getInstance().getMainStackPane().hasMapLoaded()) {
                    StageFactory.getAddMapPieceStage().AddImage(image_clipboard);
                } else {
                    Root.getInstance().getMainStackPane().AddNewMap(image_clipboard);
                    Root.getInstance().save();
                }
            } else {
                System.out.println("NO IMAGE IN CLIPBOARD!");
            }
        }
    };

    EventHandler<ActionEvent> onBtnSaveAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(final ActionEvent e) {
            Root.getInstance().save();
        }
    };

    private static void configureFileChooser(final FileChooser file_chooser) {
        file_chooser.setTitle("Select Map Screenshot");
        file_chooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        file_chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg; *.png")
        );
    }

}
