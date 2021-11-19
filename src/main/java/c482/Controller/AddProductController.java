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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Pierce Staab
 * This class provides functionality for adding products.
 */

public class AddProductController implements Initializable {

    @FXML
    private Button cancelProduct;

    @FXML
    private Button saveProduct;

    @FXML
    private TextField productId;

    @FXML
    private TextField productName;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productMax;

    @FXML
    private TextField productMin;

    @FXML
    private TextField productInv;

    @FXML
    private TextField searchPart;

    @FXML
    private Button removePart;

    @FXML
    private Button movePart;

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
    private int Id;
    Product product;

    ObservableList<Part> associatedItems = FXCollections.observableArrayList();

    /**
     * This is the constructor for adding products and sets the inventory at the same time
     * @param inventory - this is the inventory of parts and products
     */
    public AddProductController(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * This method sets the inventory from main.
     *
     * @param inventory - This is the inventory of parts and products
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * This hides the product form and takes the user back to the main page
     * @param event
     */
    @FXML
    private void hideAddProductsForm(ActionEvent event) {

        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to cancel adding a product?", OK, CANCEL);

        alert.setTitle("Cancel Adding Product");
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res.equals(OK)) {
                mainScreen(event);
            } else {
                alert.hide();
            }
        });
    }

    /**
     * This saves the inventory and returns the user to the main screen
     * @param action
     */
    @FXML
    private void AddNewProduct(ActionEvent action) {
        mainScreen(action);
    }

    /**
     * This adds a new product and performs validation on it
     * @param action
     */
    @FXML
    private void addProduct(ActionEvent action) {
        List<TextField> textFields = Arrays.asList(productName, productInv, productMax, productMin, productPrice);
        if (!checkIfEmpty(textFields)) {
            if (validationPassed(textFields)) {
                if (checkMinMaxInv(Integer.parseInt(productMin.getText()), Integer.parseInt(productMax.getText()), Integer.parseInt(productInv.getText()))) {
                    Double totalPartsPrice = 0.0;
                    product = new Product(Id,
                            productName.getText(),
                            Double.parseDouble(productPrice.getText()),
                            Integer.parseInt(productInv.getText()),
                            Integer.parseInt(productMin.getText()),
                            Integer.parseInt(productMax.getText()));

                    if (!associatedItems.isEmpty()) {
                        for (Part part : associatedItems) {
                            product.addAssociatedPart(part);
                            totalPartsPrice += part.getPrice();
                        }

                        if (product.getPrice() > totalPartsPrice) {
                            inventory.addProduct(product);
                            AddNewProduct(action);
                        } else {
                            alertBox("Price of a product cannot be less than the cost of the parts");
                        }
                    } else {
                        inventory.addProduct(product);
                        AddNewProduct(action);
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
     * This creates an alert box with custom text
     * @param error
     */
    private void alertBox(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Error");
        alert.setContentText(error);

        alert.showAndWait();
    }

    /**
     * This removes an associated part from the product
     */
    @FXML
    private void setRemovePart() {
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        associatedItems.remove(selectedPart);
        if (product != null) {
            product.deleteAssociatedPart(selectedPart);
        }
    }

    /**
     * This adds a part to be associated with a product
     */
    @FXML
    private void setMovePart() {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null && !associatedItems.contains(selectedPart)) {
            associatedItems.add(selectedPart);
        } else {
            alertBox("Please select Item or Add new Part");
        }
        if (product != null) {
            product.addAssociatedPart(selectedPart);
        }
    }

    /**
     * This method searches for parts and updates table view.
     */
    @FXML
    private void setSearchPart() {
        searchPart.textProperty().addListener((Obs, oldText, newText) -> {

            ObservableList<Part> foundItems = FXCollections.observableArrayList();

            if (isNumeric(searchPart.getText()) && !searchPart.getText().isEmpty()) {
                int number = Integer.parseInt(searchPart.getText());
                Part part = inventory.lookupPart(number);

                if (part == null) {

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
     * This method validates the inventory lookup.
     *
     * @param str - value to be checked if numeric
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * This method sets up the parts tableview.
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
     * This method sets up the associated parts table view.
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
     * This generates a unique id for products
     * @return a unique id for a product
     */
    @FXML
    private Integer generateUniqueNumber() {

        Random ints = new Random();
        int Id = ints.nextInt(999999);

        inventory.getAllProducts().forEach((item) -> {
            if (item.getId() == Id) {
                ints.nextInt();
            }
        });

        return Id;
    }


    /**
     * This method checks that the textfields are not empty.
     * @param ts This is a text field that has been selected by user to update info
     * @return
     */
    @FXML
    private boolean checkIfEmpty(List<TextField> ts) {

        boolean isEmpty = false;

        //add part id
        for (TextField textField : ts) {
            if (textField.getText().trim().length() == 0)
                isEmpty = true;
        }

        return isEmpty;
    }


    /**
     * this method validates strings and integers and doubles as the needed inputs.
     * @param ts This is a text field that has been selected by user to update info
     * @return
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
                        System.out.println("This is " + Id + " not  a String");
                        isValid = false;

                    } else if (Id.length() > 1) {
                        for (int i = 0; i < Id.length(); i++) {
                            if (Character.isDigit(Id.charAt(i))) {
                                System.out.println("This is " + Id.charAt(i) + " not  a String");
                                isValid = false;
                            } else {
                                isValid = true;
                            }
                        }
                    } else {
                        isValid = true;
                        System.out.println("This is " + Id + "  a String");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("The value :  " + Id + " is not a number");
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * This method makes sure that the inventory is between the min and the max and that the min isn't more than max
     * @param min
     * @param max
     * @param inv
     * @return
     */
    @FXML
    private boolean checkMinMaxInv(int min, int max, int inv) {

        if (inv >= min) {
            return max > inv;
        } else {
            return false;
        }

    }

    /**
     * This initializes the controller and the tableview.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Id = generateUniqueNumber();
        productId.setText(Integer.toString(Id));
        setPartTableView();
        setAssociatedPartsTable();
        setSearchPart();
    }

    /**
     * This returns the user to the main screen
     * @param event
     */
    @FXML
    private void mainScreen(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/MainInventory.fxml"));
        InventoryController controller = new InventoryController();
        controller.setInventory(inventory);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
