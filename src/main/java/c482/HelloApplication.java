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

        inventory.addPart(new InHousePart(1, "GPU", 1000.00, 20, 10, 200, 9));
        inventory.addPart(new OutsourcedPart(2, "CPU", 500.00, 100, 10, 300, "AMD"));
        inventory.addPart(new InHousePart(3, "RAM", 300.00, 100, 50, 250, 420));
        inventory.addPart(new InHousePart(4, "Motherboard", 300.00, 150, 10, 500, 69));
        inventory.addPart(new OutsourcedPart(5, "PSU", 200.00, 200, 30, 550, "Seasonic"));
        inventory.addPart(new InHousePart(6, "Case", 300.00, 250, 20, 300, 123));
        inventory.addPart(new OutsourcedPart(7, "Fan", 20.00, 300, 10, 850, "Noctua"));
        inventory.addPart(new InHousePart(8678, "CPU Cooler", 100.00, 450, 20, 900, 321));
        inventory.addPart(new InHousePart(8, "Mouse", 30.00, 450, 40, 600, 1919));
        inventory.addPart(new OutsourcedPart(9, "Keyboard", 300.00, 50, 10, 420, "Kono"));

        inventory.addProduct(new Product(1, "Prebuilt", 1000.00, 54, 34, 124));
        inventory.addProduct(new Product(2, "Custom", 2500.00, 10, 1, 99));

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