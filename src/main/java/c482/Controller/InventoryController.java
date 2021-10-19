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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setSelectedPart(Part part) {
        ObservableList<Part> parts = inventory.getAllParts();
        for (Part updatedPart : parts) {
            if (part.getId() == updatedPart.getId()) {
                inventory.updatePart(parts.indexOf(part), updatedPart);
            }
        }
    }

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
     * This method initializes the search function and returns the typed Product input if found.
     */
    public void SearchProduct() {
        productSearchField.textProperty().addListener((Obs, oldText, newText) -> {
            ObservableList<Product> foundItems = FXCollections.observableArrayList();

            if (isNumeric(productSearchField.getText()) && !productSearchField.getText().isEmpty()) {
                int     number = Integer.parseInt(productSearchField.getText());
                Product p      = inventory.lookupProduct(number);

                if (p == null) {
                    productsTable.setItems(null);
                    productsTable.setPlaceholder(new Label("Product Not Found"));
                    alertBox("Part not Found");
                } else {
                    foundItems.add(p);
                    productsTable.setItems(foundItems);
                }

            } else if (!isNumeric(productSearchField.getText()) && !productSearchField.getText().isEmpty()) {
                if (inventory.lookupPart(productSearchField.getText()) == null) {
                    alertBox("Product not Found");
                } else {
                    Product product = inventory.lookupProduct(productSearchField.getText());
                    foundItems.add(product);
                    productsTable.setItems(foundItems);
                }
            } else {
                productsTable.setItems(inventory.getAllProducts());
            }
        });
    }

    /**
     * This method initializes the search function and returns the typed Parts input if found.
     * <p>
     * Part G a: This function can result in a logical error where a user is not able to search for both
     * products using name and id correctly. First,it can result in a number exception error,a return of the
     * wrong item or no items,or cause the data not to be populated.To fix this - add a listener to the search_field
     * to listen for changes ,initialize an empty observable array to hold the return values-This is helpful because
     * searching with an part Id only returns a part and the tableview only accepts observable lists Then include the
     * isNumeric function to determine whether the input is a digit or string so that we can use the appropriate functions.
     * Check whether the search_field is empty to avoid the number exception error when trying to convert it to integer.
     * To synchronize both part id and name - add the id to an observable list and return a specific item - The lookup
     * part overloaded method returns a specific instance while the name returns a List.
     */
    public void SearchParts() {
        partSearchField.textProperty().addListener((Obs, oldText, newText) -> {
            ObservableList<Part> foundItems = FXCollections.observableArrayList();

            if (isNumeric(partSearchField.getText()) && !partSearchField.getText().isEmpty()) {
                int  number = Integer.parseInt(partSearchField.getText());
                Part part   = inventory.lookupPart(number);

                if (part == null) {
                    partsTable.setItems(null);
                    partsTable.setPlaceholder(new Label("Part Not Found"));
                    alertBox("Product not Found");
                } else {
                    foundItems.add(part);
                    partsTable.setItems(foundItems);

                }

            } else if (!isNumeric(partSearchField.getText()) && !partSearchField.getText().isEmpty()) {
                if (inventory.lookupPart(partSearchField.getText()) == null) {
                    alertBox("Product not Found");
                } else {
                    Part part = inventory.lookupPart(partSearchField.getText());
                    foundItems.add(part);
                    partsTable.setItems(foundItems);
                }

            } else {
                partsTable.setItems(inventory.getAllParts());
            }

        });
    }

    /**
     * validates digit inputs.
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
     * This method pushes to the Add parts form.
     */
    @FXML
    private void pushToAddPartsForm(ActionEvent event) throws IOException {
        Parent parent;
        Stage  stage;
        stage = (Stage) modifyPart.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/AddPart.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Add Part");
        AddPartController controller = loader.getController();
        controller.setInventory(this.inventory);
    }

    /**
     * This method pushes to the Add products form.
     */

    @FXML
    private void pushToAddProductsForm() throws IOException {
        Parent parent;
        Stage  stage;
        stage = (Stage) modifyPart.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/AddProduct.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Add Product");
        AddProductController controller = loader.getController();
        controller.setInventory(inventory);
    }

    /**
     * This method pushes to the Modify Parts form.
     */
    @FXML
    private void pushToModifyPartsForm(ActionEvent event) throws IOException {
        if (partsTable.getSelectionModel().getSelectedItem() != null) {
            Part   selectedPart = partsTable.getSelectionModel().getSelectedItem();
            Parent parent;
            Stage  stage;
            stage = (Stage) modifyPart.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/ModifyPart.fxml"));
            parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Modify Part");
            ModifyPartController controller = loader.getController();
            controller.setPart(selectedPart);
            controller.getInventory(inventory);
        } else {
            alertBox("Please select Part to Modify");
        }
    }

    /**
     * This is a warning dialog box.
     */
    private void alertBox(String err) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Error");
        alert.setContentText(err);

        alert.showAndWait();
    }

    /**
     * This method pushes to the Modify Products form.
     */
    @FXML
    private void pushToModifyProductsForm() throws IOException {
        if (productsTable.getSelectionModel().getSelectedItem() != null) {
            Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
            Parent  parent;
            Stage   stage;
            stage = (Stage) modifyProduct.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/ModifyProduct.fxml"));
            parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Modify Product");
            ModifyProductController controller = loader.getController();
            controller.setProduct(selectedProduct);
            controller.setInventory(inventory);
        } else {
            alertBox("Please select Product to Modify");
        }
    }

    /**
     * This a Button function that deletes a part from the tableview and the partlist.
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
     * This a Button function that deletes a product from the tableview and the product list.
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
        ButtonType OK     = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to exit?", OK, CANCEL);

        alert.setTitle("Exit the Inventory system");
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