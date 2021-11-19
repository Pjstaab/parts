/**
 * @author: Pierce Staab
 * The Add Part form
 * This class creates a new part and adds it to the inventory
 */


package c482.Controller;

import c482.Model.InHousePart;
import c482.Model.Inventory;
import c482.Model.OutsourcedPart;
import c482.Model.Part;
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
import java.util.*;

/**
 * This class provides functionality of adding a product.
 */
public class AddPartController implements Initializable {

    @FXML
    private RadioButton radioInHouse;
    @FXML
    private RadioButton radioOutsourced;
    @FXML
    private TextField partMachineId;
    @FXML
    private TextField partName;
    @FXML
    private TextField partInv;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partMax;
    @FXML
    private TextField partMin;
    @FXML
    private Label radioOption;
    @FXML
    private TextField partId;
    @FXML
    private Button cancelPart;
    @FXML
    private Button savePart;

    Inventory inventory;
    private int Id;

    AddPartController(Inventory inv) {
        inventory = inv;
    }

    /**
     * This method sets the inventory from main.
     *
     * @param v This is an inventory item
     */

    public void setInventory(Inventory v) {
        this.inventory = v;
    }

    /**
     * This method initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Id = generateUniqueNumber();
        partId.setText(Integer.toString(Id));
    }

    /**
     * This method listens for the inhouse radiobutton
     * and assigns the correct labels.
     */
    @FXML
    private void inHouseListener(ActionEvent event) {
        if (radioInHouse.isSelected()) {
            radioOption.setText("Machine ID");
        }
        partMachineId.promptTextProperty().setValue("Machine ID");
        radioOutsourced.setSelected(false);
    }

    /**
     * This method listens for the outsourced radiobutton
     * and assigns the correct labels.
     */
    @FXML
    private void outSourcedListener(ActionEvent event) {
        if (radioOutsourced.isSelected()) {
            radioOption.setText("Company Name");
        }
        partMachineId.clear();
        partMachineId.promptTextProperty().setValue("Company Name");
        radioInHouse.setSelected(false);

    }


    /**
     * This method loads the main view and returns part to inventory.
     */
    private void AddPart(ActionEvent event) throws IOException {
        mainScreen(event);
    }


    /**
     * This method cancels the part add form if nothing is done
     * and returns to main view.
     */
    public void HideAddPartsForm() {
        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to cancel adding a part?", OK, CANCEL);

        alert.setTitle("Cancel adding part");
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res.equals(OK)) {
                Stage stage;
                Parent root;
                stage = (Stage) cancelPart.getScene().getWindow();

                try {
                    root = FXMLLoader.load(getClass().getResource("/c482/MainInventory.fxml"));
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
     * This method gets the input from the user,validates inputs  and adds it to the inventory.
     */

    @FXML
    private void addedPart(ActionEvent event) throws IOException {
        List<TextField> textFields = Arrays.asList(partName, partPrice, partInv, partMin, partMax, partMachineId);

        Part part;
        if (!checkIfEmpty(textFields)) {
            if (radioInHouse.isSelected() && validationPassed(textFields)) {
                if (checkMinMaxInv(Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), Integer.parseInt(partInv.getText()))) {
                    part = new InHousePart(Id,
                            partName.getText(),
                            Double.parseDouble(partPrice.getText()),
                            Integer.parseInt(partInv.getText()),
                            Integer.parseInt(partMin.getText()),
                            Integer.parseInt(partMax.getText()),
                            Integer.parseInt(partMachineId.getText()));

                    inventory.addPart(part);
                    AddPart(event);
                } else {
                    alertBox("Min should be less than Max; and Inv should be between those two values");
                }


            } else if (radioOutsourced.isSelected() && validationPassed(textFields)) {
                if (checkMinMaxInv(Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), Integer.parseInt(partInv.getText()))) {

                    part = new OutsourcedPart(Id,
                            partName.getText(),
                            Double.parseDouble(partPrice.getText()),
                            Integer.parseInt(partInv.getText()),
                            Integer.parseInt(partMin.getText()),
                            Integer.parseInt(partMax.getText()),
                            partMachineId.getText());
                    inventory.addPart(part);
                    AddPart(event);
                } else {
                    alertBox("Min should be less than Max; and Inv should be between those two values");
                }
            } else {
                alertBox("name shouel be letters, Min,Max,price and inv should be numbers");
            }
        } else {
            alertBox("Fields Cannot be empty");
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
     * This method validates strings and integers and doubles as the needed inputs.
     */

    @FXML
    private boolean validationPassed(List<TextField> ts) {

        boolean isValid = true;

        for (TextField textField : ts) {
            String Id = textField.getId();
            try {
                if (Id.equals(partInv.getId()) || Id.equals(partMax.getId()) || Id.equals(partMin.getId())) {
                    Integer.valueOf(textField.getText());
                } else if (Id.equals(partPrice.getId())) {
                    Double.valueOf(textField.getText());
                } else if (Id.equals(partMachineId.getId())) {
                    if (radioInHouse.isSelected()) {
                        Integer.valueOf(textField.getText());
                    }
                } else {
                    Id = textField.getText();
                    if (Character.isDigit(Id.charAt(0))) {
                        isValid = false;
                    } else if (Id.length() > 1) {
                        for (int i = 0; i < Id.length(); i++) {
                            isValid = !Character.isDigit(Id.charAt(i));
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

    /**
     * This method generates unique ids for the parts.
     */

    @FXML
    private Integer generateUniqueNumber() {
        Random ints = new Random();
        int Id = ints.nextInt(999999);

        inventory.getAllParts().forEach((item) -> {

            if (item.getId() == Id) {
                ints.nextInt();
            }
        });
        return Id;
    }


    /**
     * This method is part of the validation methods checks for the input values for max, min, and inv
     * max > inv > min.
     *
     * @return boolean
     */
    @FXML
    private boolean checkMinMaxInv(int min, int max, int inv) {

        if (inv >= min) {
            return max > inv;
        } else {
            return false;
        }
    }

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
