package c482;

import c482.Controller.InventoryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import c482.Model.*;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Inventory inventory = new Inventory();

        inventory.addPart(new InHousePart(1, "Screw", 600.00, 100, 50, 200, 5));
        inventory.addPart(new OutsourcedPart(2, "Nail", 300.00, 150, 50, 200, "NailedIt Ltd"));
        inventory.addPart(new InHousePart(3, "Glue", 200.00, 200, 50, 250, 23));
        inventory.addPart(new InHousePart(4, "Door Knob", 669.00, 250, 50, 300, 32));
        inventory.addPart(new OutsourcedPart(5, "Hinges", 210.00, 300, 50, 350, "Hinges Inc"));
        inventory.addPart(new InHousePart(6, "Wrench", 300.00, 350, 50, 400, 76));
        inventory.addPart(new OutsourcedPart(7, "Locks", 1000.00, 400, 50, 450, "Locks Inc"));
        inventory.addPart(new InHousePart(8678, "Frames", 10000.00, 450, 50, 500, 87));
        inventory.addPart(new InHousePart(8, "Cushions", 10000.00, 450, 50, 500, 87));
        inventory.addPart(new OutsourcedPart(9, "Leather", 100.00, 50, 50, 440, "Skins Ltd"));
        inventory.addPart(new InHousePart(10, "Soft boards", 150.00, 150, 50, 590, 93));

        inventory.addProduct(new Product(1, "Chair", 100.00, 50, 44, 104));
        inventory.addProduct(new Product(2, "Door", 250.00, 57, 33, 98));
        inventory.addProduct(new Product(3, "Cabinet", 370.00, 89, 77, 245));

        FXMLLoader          fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/c482/MainInventory.fxml"));
        InventoryController controller = new InventoryController();
        controller.setInventory(inventory);
        fxmlLoader.setController(controller);
        Scene      scene      = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Inventory");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}