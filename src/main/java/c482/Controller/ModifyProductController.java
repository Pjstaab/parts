/**
 * @author: Brown oichoe
 * The Modify product form
 */

package c482.Controller;

import c482.Model.Inventory;
import c482.Model.Part;
import c482.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class provides functionality to modify a product.
 */

public class ModifyProductController implements Initializable {

    @FXML
    private Button cancelProduct;

    @FXML
    private TextField productId;

    @FXML
    private TextField productName;

    @FXML
    private TextField productInv;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productMax;

    @FXML
    private TextField productMin;

    @FXML
    private TextField searchPart;

    @FXML
    private Button saveProduct;

    @FXML
    private Button movePart;

    @FXML
    private Button removeAssociatedPart;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableView<Part> associatedPartsTable;

    @FXML
    private TableColumn<Part, Integer> partId;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Integer> partLevel;

    @FXML
    private TableColumn<Part, Double> partCost;

    @FXML
    private TableColumn<Part, Integer> associatedPartId;

    @FXML
    private TableColumn<Part, String> associatedPartName;

    @FXML
    private TableColumn<Part, Integer> associatedPartLevel;

    @FXML
    private TableColumn<Part, Double> associatedPartCost;

    Inventory inventory;
    Product product;

    ObservableList<Part> associatedItems = FXCollections.observableArrayList();

    /**
     * This method sets the inventory from main.
     *
     * @param inventory This initialises the inventory and is passed from main
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * This method updates text fields with the selected input.
     *
     * @param product - This is product that has been selected to be modified and updated
     */
    public void setProduct(Product product) {
        this.product = product;

        System.out.println(this.product.getName());

        productId.setText(Integer.toString(this.product.getId()));
        productName.setText(this.product.getName());
        productPrice.setText(Double.toString(this.product.getPrice()));
        productMax.setText(Integer.toString(this.product.getMax()));
        productMin.setText(Integer.toString(this.product.getMin()));
        productInv.setText(Integer.toString(this.product.getStock()));

        if (this.product.getAllAssociatedParts().isEmpty()) {
            associatedPartsTable.setPlaceholder(new Label(("No parts associated with this product.Add Parts")));
        } else {
            associatedPartsTable.setItems(this.product.getAllAssociatedParts());
        }

    }

    /**
     * This method removes associated parts from product.
     */
    @FXML
    private void setRemovePart() {
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        associatedItems.remove(selectedPart);
        product.deleteAssociatedPart(selectedPart);
    }

    /**
     * This method adds associated parts from product.
     */
    @FXML
    private void setMovePart() {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null && !associatedItems.contains(selectedPart)) {
            associatedItems.add(selectedPart);
            product.addAssociatedPart(selectedPart);
        } else {
            alertBox("Please select Part to add or Item is already added");
        }

    }

    /**
     * This method cancels the Modify Products form.
     *
     * @throws IOException This catches an exception thrown during input output operations
     */
    public void HideModifyProductsForm() throws IOException {

        ButtonType OK     = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Are You Sure You Want cancel Modifying Product", OK, CANCEL);

        alert.setTitle("Exit the Modify Product Form");
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res.equals(OK)) {

                try {
                    Stage  stage;
                    Parent root;

                    stage = (Stage) cancelProduct.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader();
                    root = loader.load(getClass().getResource("../Inventory_main.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                alert.hide();
            }
        });

    }

    /**
     * This method initializes the search function and returns the typed input if found.
     */
    @FXML
    private void setsearchPart() {
        searchPart.textProperty().addListener((Obs, oldText, newText) -> {

            ObservableList<Part> foundItems = FXCollections.observableArrayList();

            if (isNumeric(searchPart.getText()) && !searchPart.getText().isEmpty()) {
                int  number = Integer.parseInt(searchPart.getText());
                Part part   = inventory.lookupPart(number);

                if (part == null) {
                    System.out.println("Table is empty");

                    partTableView.setItems(null);
                    partTableView.setPlaceholder(new Label("Part Not Found"));
                } else {

                    foundItems.add(part);
                    partTableView.setItems(foundItems);

                }

            } else if (!isNumeric(searchPart.getText()) && !searchPart.getText().isEmpty()) {
                Part part = inventory.lookupPart(searchPart.getText());
                foundItems.add(part);
                partTableView.setItems(foundItems);
            } else {
                partTableView.setItems(inventory.getAllParts());
            }

        });
    }

    /**
     * This method validates that an input is digits.
     *
     * @param str - Passed string to check whether it is numeric
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
     * This method sets the parts table view.
     */
    private void setPartTableView() {
        partTableView.refresh();

        partTableView.setItems(this.inventory.getAllParts());
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        partLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    /**
     * This method sets the associated parts table.
     */
    private void setAssociatedPartsTable() {
        associatedPartsTable.refresh();
        associatedPartsTable.setItems(associatedItems);
        associatedPartsTable.setPlaceholder(new Label("No parts associated with this product"));
        associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    /**
     * This method initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setPartTableView();
        setAssociatedPartsTable();
        setsearchPart();
        List<TextField> textFields = Arrays.asList(productName, productPrice, productMin, productMax, productInv);
        textFields.forEach(textField -> textField.textProperty().addListener((obs, old, newWord) -> {
            // TODO here

            try {
                textField.setText(newWord);
                setValues(textField);
            } catch (NumberFormatException e) {
                System.out.println("I got you error");
            }
        }));

    }

    /**
     * This method updates products old values.
     *
     * @param ts This is a textfield that has been selected by user to update info
     */
    public void setValues(TextField ts) {
        if (ts.getId().equals(productId.getId())) {
            product.setId(Integer.parseInt(ts.getText()));
        } else if (ts.getId().equals(productName.getId())) {
            product.setName(ts.getText());
        } else if (ts.getId().equals(productInv.getId())) {
            product.setStock(Integer.parseInt(ts.getText()));
        } else if (ts.getId().equals(productPrice.getId())) {
            product.setPrice(Double.parseDouble(ts.getText()));
        } else if (ts.getId().equals(productMax.getId())) {
            product.setMax(Integer.parseInt(ts.getText()));
        } else {
            product.setMin(Integer.parseInt(ts.getText()));
        }
    }

    /**
     * This method saves a modified product and pushes it to main.
     */
    @FXML
    private void SaveModifiedProduct() throws IOException {

        Parent parent;
        Stage  stage;
        stage = (Stage) saveProduct.getScene().getWindow();
        List<TextField> textFields = Arrays.asList(productName, productInv, productMax, productMin, productPrice);

        if (!checkIfEmpty(textFields)) {
            if (validationPassed(textFields)) {
                if (checkMinMAxInv(Integer.parseInt(productMin.getText()), Integer.parseInt(productMax.getText()), Integer.parseInt(productInv.getText()))) {

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Inventory_main.fxml"));
                        parent = loader.load();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.setTitle("Inventory");
                        InventoryController controller = loader.getController();
                        controller.setSelectedProduct(product);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Item not modified");
                    }

                } else {
                    alertBox("Min should be less than Max. Inv Should be less than Max and Min");
                }
            } else {
                alertBox("Name should be letters, Inv,Max,Min and Price should be numbers");
            }

        } else {
            alertBox("Every Field must be filled");
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
     * This method checks that the textfields are not empty.
     */

    @FXML
    private boolean checkIfEmpty(List<TextField> ts) {

        boolean isEmpty = false;

        for (TextField textField : ts) {
            if (textField.getText().trim().length() == 0)
                isEmpty = true;
        }

        return isEmpty;
    }

    /**
     * This method is part of the validation -methods checks for the input values for max,min and inv
     * max>inv>min
     *
     * @returns boolean.
     */
    @FXML
    private boolean checkMinMAxInv(int min, int max, int inv) {

        if (inv >= min) {
            return max > inv;
        } else {
            return false;
        }

    }

    /**
     * This method validates strings and integers and doubles as the needed inputs.
     */

    @FXML
    private boolean validationPassed(List<TextField> ts) {

        boolean isValid = true;

        for (TextField textField : ts) {
            String Id = textField.getId();
            try {
                if (Id.equals(productInv.getId()) || Id.equals(productMax.getId()) || Id.equals(productMin.getId())) {
                    Integer.valueOf(textField.getText());

                } else if (Id.equals(productPrice.getId())) {

                    Double.valueOf(textField.getText());

                } else {
                    Id = textField.getText();

                    if (Character.isDigit(Id.charAt(0))) {
                        isValid = false;

                    } else if (Id.length() > 1) {
                        for (int i = 0; i < Id.length(); i++) {
                            if (Character.isDigit(Id.charAt(i))) {
                                return false;
                            } else {
                                isValid = true;
                            }

                        }
                    } else {
                        isValid = true;
                    }

                }

            } catch (NumberFormatException e) {
                isValid = false;

            }

        }

        return isValid;
    }

}
