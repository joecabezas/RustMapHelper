/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper;

import java.io.File;
import java.util.EnumSet;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author joeca_000
 */
public class AddMapPieceStackPane extends MainStackPane {

    public ImageView image_new_view_preview;

    public void AddNewMapImage(File file) {

        Image new_image_map_preview = ImageUtils.CreateImage(file, EnumSet.of(
                ImageUtils.FLAGS.TRANSPARENT_IMAGE,
                ImageUtils.FLAGS.REMOVE_ALPHA_LABEL,
                ImageUtils.FLAGS.TRIM_IMAGE,
                ImageUtils.FLAGS.NEW_IMAGE
        ));

        image_new_view_preview = new ImageView();
        image_new_view_preview.setImage(new_image_map_preview);
        group.getChildren().add(image_new_view_preview);
        image_new_view_preview.addEventHandler(MouseEvent.MOUSE_PRESSED, onNewImageMousePressedHandler);
    }
    
    public void AddNewMapImage(Image image) {

        image_new_view_preview = new ImageView();
        image_new_view_preview.setImage(image);
        group.getChildren().add(image_new_view_preview);
        image_new_view_preview.addEventHandler(MouseEvent.MOUSE_PRESSED, onNewImageMousePressedHandler);
    }

    private final EventHandler<MouseEvent> onNewImageMousePressedHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent e) {
            if (e.isSecondaryButtonDown()) {
                System.out.println("NEW:" + e);
                target_node = image_new_view_preview;
            }
        }
    };
}
