/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author joeca_000
 */
public class MainCanvas extends ResizableCanvas {

    Image image_base;
    Image image_new;
    GraphicsContext gc;

    public MainCanvas() {
        super();
        gc = getGraphicsContext2D();
        addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedHandler);
        addEventHandler(MouseEvent.MOUSE_DRAGGED, setOnMouseDraggedHandler);
    }

    private EventHandler<MouseEvent> onMousePressedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            System.out.println("e:" + e);
        }
    };
    
    private EventHandler<MouseEvent> setOnMouseDraggedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            System.out.println("e:" + e);
        }
    };

    public void setImageBase(Image image_base) {
        this.image_base = image_base;
    }

    public void setImageNew(Image image_new) {
        this.image_new = image_new;
    }

    @Override
    protected void draw() {
        super.draw();
        if (image_base != null) {
            gc.drawImage(image_base, 0, 0);
        }
        if (image_new != null) {
            gc.drawImage(image_new, 0, 0);
        }
    }
}
