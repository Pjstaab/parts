package c482.Controller;

import c482.Model.Inventory;
import c482.Model.Part;
import c482.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the primary controller for the application
 *
 */
public class InventoryController implements Initializable {
    //Parts
    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Integer> partId;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Integer> partLevel;

    @FXML
    private TableColumn<Part, Double> partCost;

    @FXML
    private TextField partSearchField;

    @FXML
    private Button exitMain;

    @FXML
    private Button addPart;

    @FXML
    private Button modifyPart;

    @FXML
    private Button deletePart;

    @FXML
    private Button modifyProduct;

    @FXML
    private Label tableMessage;

    //Product
    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Integer> productId;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Integer> productLevel;

    @FXML
    private TableColumn<Product, Double> productCost;

    @FXML
    private TextField productSearchField;

    Inventory inventory;

    /**
     * This sets the current inventory
     * @param inventory
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * This sets the selected part in the inventory
     * @param part
     */
    public void setSelectedPart(Part part) {
        ObservableList<Part> parts = inventory.getAllParts();
        for (Part updatedPart : parts) {
            if (part.getId() == updatedPart.getId()) {
                inventory.updatePart(parts.indexOf(part), updatedPart);
            }
        }
    }

    /**
     * This sets the selected product in the inventory
     * @param product
     */
    public void setSelectedProduct(Product product) {
        ObservableList<Product> products = inventory.getAllProducts();
        for (Product updatedProduct : products) {
            if (product.getId() == updatedProduct.getId()) {
                inventory.updateProduct(products.indexOf(product), updatedProduct);
            }
        }
    }

    /**
     * This method sets up the Parts table.
     */
    public void setPartsTable() {
        partsTable.setItems(this.inventory.getAllParts());
        partsTable.refresh();
    }

    /**
     * This method sets up the Products table.
     */
    public void setProductsTable() {
        productsTable.setItems(this.inventory.getAllProducts());
        productsTable.refresh();
    }

    /**
     * This method initializes the controller.
     */
    public void initialize(URL url, ResourceBundle rb) {
        setPartsTable();
        setProductsTable();
        SearchParts();
        SearchProduct();
    }

    /**
     * This method initializes the search function and returns the typed product info if it's found
     */
    public void SearchProduct() {
        productSearchField.textProperty().addListener((Obs, oldText, newText) -> {
            ObservableList<Product> foundItems = FXCollections.observableArrayList();

            if (isNumeric(productSearchField.getText()) && !productSearchField.getText().isEmpty()) {
                int number = Integer.parseInt(productSearchField.getText());
                Product p = inventory.lookupProduct(number);

                if (p == null) {
                    productsTable.setItems(null);
                } else {
                    foundItems.add(p);
                    productsTable.setItems(foundItems);
                }

            } else if (!isNumeric(productSearchField.getText()) && !productSearchField.getText().isEmpty()) {
                Product product = inventory.lookupProduct(productSearchField.getText());
                foundItems.add(product);
                productsTable.setItems(foundItems);
            } else {
                productsTable.setItems(inventory.getAllProducts());
            }
        });
    }

    /**
     * This method initializes the search function and returns the typed Parts input if found.
     */
    public void SearchParts() {
        partSearchField.textProperty().addListener((Obs, oldText, newText) -> {
            ObservableList<Part> foundItems = FXCollections.observableArrayList();

            if (isNumeric(partSearchField.getText()) && !partSearchField.getText().isEmpty()) {
                int number = Integer.parseInt(partSearchField.getText());
                Part part = inventory.lookupPart(number);

                if (part == null) {
                    partsTable.setItems(null);
                    partsTable.setPlaceholder(new Label("Part Not Found"));
                } else {
                    foundItems.add(part);
                    partsTable.setItems(foundItems);
                }

            } else if (!isNumeric(partSearchField.getText()) && !partSearchField.getText().isEmpty()) {
                Part part = inventory.lookupPart(partSearchField.getText());
                foundItems.add(part);
                partsTable.setItems(foundItems);
            } else {
                partsTable.setItems(inventory.getAllParts());
            }

        });
    }

    /**
     * Validates digit inputs.
     *
     * @param str - The value to be checked  if numeric
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    /**
     * This function brings the user to the add parts form
     * @param event
     * @throws IOException
     */
    @FXML
    private void pushToAddPartsForm(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/AddPart.fxml"));
        AddPartController controller = new AddPartController(inventory);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Add Part");
    }

    /**
     * This function brings the user to the add products form
     * @param event
     * @throws IOException
     */
    @FXML
    private void pushToAddProductsForm(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/AddProduct.fxml"));
        AddProductController controller = new AddProductController(inventory);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Add Product");
    }

    /**
     * This function brings the user to the modify parts form
     * @param event
     * @throws IOException
     */
    @FXML
    private void pushToModifyPartsForm(ActionEvent event) throws IOException {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/ModifyPart.fxml"));
            ModifyPartController controller = new ModifyPartController(inventory, selectedPart);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            stage.setScene(scene);
            stage.setTitle("Modify Part");

            controller.setPart(selectedPart);
            controller.getInventory(inventory);
        } else {
            alertBox("Please select Part to Modify");
        }
    }
    /**
     * This creates an alert box with custom text
     * @param err
     */
    private void alertBox(String err) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Error");
        alert.setContentText(err);

        alert.showAndWait();
    }

    /**
     * This method pushes to the modify products form.
     */
    @FXML
    private void pushToModifyProductsForm(ActionEvent event) throws IOException {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (productsTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/ModifyProduct.fxml"));
            ModifyProductController controller = new ModifyProductController(inventory, selectedProduct);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            stage.setScene(scene);
            stage.setTitle("Modify Product");

            controller.setProduct(selectedProduct);
            controller.setInventory(inventory);
        } else {
            alertBox("Please select Product to Modify");
        }
    }

    /**
     * This deletes a part from the inventory and updates the table
     * @param event
     */
    @FXML
    private void deletePart(ActionEvent event) {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            partsTable.getItems().remove(selectedPart);
            partsTable.refresh();
            inventory.deletePart(selectedPart);
        } else {
            alertBox("Please select part to delete");
        }
    }

    /**
     * This deletes a product from the inventory and updates the table
     * @param event
     */
    @FXML
    private void deleteProduct(ActionEvent event) {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            if (inventory.deleteProduct(selectedProduct)) {
                inventory.getAllProducts().remove(selectedProduct);
                productsTable.getItems().remove(selectedProduct);
                productsTable.refresh();
            } else {
                alertBox("Product has Associated Parts Cannot be Deleted");
            }
        } else {
            alertBox("Please select product to delete");
        }
    }

    /**
     * This is a button function that exits the application.
     */
    @FXML
    private void exitInventory() {
        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to exit?", OK, CANCEL);

        alert.setTitle("Exit the inventory system");
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res.equals(OK)) {
                Stage stage = (Stage) exitMain.getScene().getWindow();
                stage.close();
            } else {
                alert.hide();
            }
        });

    }
}