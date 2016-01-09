/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper;

import RustMapHelper.models.Data;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javax.imageio.ImageIO;

/**
 *
 * @author joeca_000
 */
public class MainStackPane extends StackPane {

    protected Group group;
    private Group origin_group;

    private ImageView image_view;

    private Point2D origin;
    protected Point2D target_position;
    protected Point2D first_click;
    protected Point2D first_target_position;

    protected Translate first_target_translate;

    protected Node target_node;

    public static final double ZOOM_FACTOR = 0.2;

    public MainStackPane() {
        setupGroup();
        setupEvents();
        OpenExistingMap(Data.getInstance().GetBaseMapPath());
    }

    private void setupGroup() {
        origin_group = new Group();
        origin_group.setManaged(false);
        getChildren().add(origin_group);

        group = new Group();
        group.setManaged(false);
        origin_group.getChildren().add(group);
    }

    private void setupEvents() {
        layoutBoundsProperty().addListener(onChangeBounds);

        addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedHandler);
        addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedHandler);
        addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedHandler);
        addEventHandler(ScrollEvent.ANY, onScrollEventHandler);
    }

    private ChangeListener<Bounds> onChangeBounds = new ChangeListener<Bounds>() {
        @Override
        public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
            origin = new Point2D(newValue.getWidth(), newValue.getHeight());
            origin_group.getTransforms().clear();
            origin_group.getTransforms().add(new Translate(origin.getX() / 2, origin.getY() / 2));
        }
    };

    private EventHandler<MouseEvent> onMousePressedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            System.out.println(e);
            if (target_node == null) {
                target_node = group;
            }
            first_click = Point2D.ZERO.add(e.getX(), e.getY());
            Point2D pos = target_node.localToParent(Point2D.ZERO);
            first_target_translate = new Translate(pos.getX(), pos.getY());
            e.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            System.out.println("target:" + target_node);
            Point2D mouse_position = Point2D.ZERO.add(e.getX(), e.getY());

            Point2D delta_position = mouse_position.subtract(first_click);
            System.out.println("delta:" + delta_position);

            target_node.getTransforms().clear();
            target_node.getTransforms().add(first_target_translate);
            target_node.getTransforms().add(new Translate(delta_position.getX(), delta_position.getY()));

            e.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseReleasedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            target_node = null;
            e.consume();
        }
    };

    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent e) {
            double zoom = 1 + ZOOM_FACTOR * ((e.getDeltaY() < 0) ? -1 : 1);
            origin_group.setScaleX(origin_group.getScaleX() * zoom);
            origin_group.setScaleY(origin_group.getScaleY() * zoom);
            e.consume();
        }
    };

    private void AddImage(Image image) {
        image_view = new ImageView();
        image_view.setImage(image);
        group.getChildren().add(image_view);

        image_view.setTranslateX(-image_view.getImage().getWidth() / 2);
        image_view.setTranslateY(-image_view.getImage().getHeight() / 2);
    }

    public void saveImage() {
        System.out.println("saveImage");
        File file = new File(Data.getInstance().GetBaseMapPath());

        SnapshotParameters snapshot_parameters = new SnapshotParameters();
        snapshot_parameters.setFill(Color.TRANSPARENT);

        double scale_x = group.getScaleX();
        double scale_y = group.getScaleY();

        group.setScaleX(1);
        group.setScaleY(1);

        WritableImage writable_image = group.snapshot(snapshot_parameters, null);

        group.setScaleX(scale_x);
        group.setScaleY(scale_y);

        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writable_image, null);

        try {
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) {
            Logger.getLogger(MainStackPane.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public final void AddNewMap(File file, Point2D pos) {
        AddNewMap(file);
        image_view.setTranslateX(pos.getX());
        image_view.setTranslateY(pos.getY());
    }
    
    public final void AddNewMap(File file) {
        OpenMap(file, EnumSet.of(
                ImageUtils.FLAGS.REMOVE_ALPHA_LABEL,
                ImageUtils.FLAGS.TRANSPARENT_IMAGE,
                ImageUtils.FLAGS.TRIM_IMAGE
        ));
    }
    
    public final void AddNewMap(Image image) {
        AddImage(image);
    }

    public final void OpenExistingMap(String path) {
        OpenMap(path, EnumSet.noneOf(ImageUtils.FLAGS.class));
    }

    private void OpenMap(String path, EnumSet<ImageUtils.FLAGS> enum_set) {
        File file_base_map = new File(path);
        OpenMap(file_base_map, enum_set);
    }

    protected void OpenMap(File file, EnumSet<ImageUtils.FLAGS> enum_set) {
        if (file.exists()) {
            AddImage(ImageUtils.CreateImage(file, enum_set));
        }
    }

    public Boolean hasMapLoaded() {
        return image_view != null;
    }
}
