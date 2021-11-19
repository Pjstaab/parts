/**
 * @author: Pierce Staab
 * Modify Part Controller
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class provides functionality to modify and edit parts.
 */
public class ModifyPartController implements Initializable {

    @FXML
    private RadioButton radioInHouse;

    @FXML
    private RadioButton radioOutsourced;

    @FXML
    private Label radioOption;

    @FXML
    private TextField partMachineId;

    @FXML
    private Button cancelPart;

    @FXML
    private TextField partId;

    @FXML
    private TextField partName;

    @FXML
    private TextField partMax;

    @FXML
    private TextField partMin;

    @FXML
    private TextField partPrice;

    @FXML
    private TextField partInv;

    @FXML
    private Button savePart;

    Part part;
    Inventory inventory;

    /**
     * This is the constructor for the modify part screen and sets the inventory and part at the same time
     * @param inventory This is the inventory
     * @param part This is the selected part to be modified
     */
    public ModifyPartController(Inventory inventory, Part part) {
        this.inventory = inventory;
        this.part = part;
    }

    /**
     * This sets up the part in the correct text fields to be modified by user.
     *
     * @param part This is the selected part to be modified
     */
    public void setPart(Part part) {

        this.part = part;

        partId.setText(Integer.toString(this.part.getId()));
        partName.setText(this.part.getName());
        partPrice.setText(Double.toString(this.part.getPrice()));
        partMax.setText(Integer.toString(this.part.getMax()));
        partMin.setText(Integer.toString(this.part.getMin()));
        partInv.setText(Integer.toString(this.part.getStock()));

        if (this.part instanceof InHousePart inHousePart) {
            radioInHouse.setSelected(true);
            radioOutsourced.setSelected(false);
            radioOption.setText("Machine ID");
            partMachineId.setText(Integer.toString(inHousePart.getMachineId()));
            partMachineId.promptTextProperty().setValue("Machine ID");

        } else if (this.part instanceof OutsourcedPart outsourcedPart) {
            radioInHouse.setSelected(false);
            radioOutsourced.setSelected(true);
            radioOption.setText("Company Name");
            partMachineId.setText(outsourcedPart.getCompanyName());
            partMachineId.promptTextProperty().setValue("Company Name");
        }

    }

    /**
     * This method gets the inventory from the main controller.
     *
     * @param inv This is the inventory
     */
    public void getInventory(Inventory inv) {
        this.inventory = inv;
    }

    /**
     * This method initializes the modify Part Controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<TextField> textFields = Arrays.asList(partName, partPrice, partInv, partMin, partMax, partMachineId);
        textFields.forEach(textField -> textField.textProperty().addListener((obs, old, newWord) -> {
            // TODO here
            try {
                textField.setText(newWord);
                setValues(textField);
            } catch (NumberFormatException e) {
            }
        }));
    }


    /**
     * This method gets user input and updates the required fields.
     *
     * @param ts This is a text field that has been selected by user to update info
     */
    public void setValues(TextField ts) {
        if (ts.getId().equals(partId.getId())) {
            part.setId(Integer.parseInt(ts.getText()));
        } else if (ts.getId().equals(partName.getId())) {
            part.setName(ts.getText());
        } else if (ts.getId().equals(partInv.getId())) {
            part.setStock(Integer.parseInt(ts.getText()));
        } else if (ts.getId().equals(partPrice.getId())) {
            part.setPrice(Double.parseDouble(ts.getText()));
        } else if (ts.getId().equals(partMax.getId())) {
            part.setMax(Integer.parseInt(ts.getText()));
        } else if (ts.getId().equals(partMin.getId())) {
            part.setMin(Integer.parseInt(ts.getText()));
        } else if (ts.getId().equals(partMachineId.getId())) {

            if (part instanceof InHousePart inHousePart && radioInHouse.isSelected()) {
                inHousePart.setMachineId(Integer.parseInt(ts.getText()));
            } else if (part instanceof OutsourcedPart outsourced && radioOutsourced.isSelected()) {
                outsourced.setCompanyName(ts.getText());

            } else if (part instanceof InHousePart && !radioInHouse.isSelected()) {

                inventory.deletePart(part);
                part = new OutsourcedPart(Integer.parseInt(partId.getText()),
                        partName.getText(),
                        Double.parseDouble(partPrice.getText()),
                        Integer.parseInt(partInv.getText()),
                        Integer.parseInt(partMin.getText()),
                        Integer.parseInt(partMax.getText()),
                        partMachineId.getText());
                inventory.addPart(part);

            } else {

                inventory.deletePart(part);
                part = new InHousePart(Integer.parseInt(partId.getText()),
                        partName.getText(),
                        Double.parseDouble(partPrice.getText()),
                        Integer.parseInt(partInv.getText()),
                        Integer.parseInt(partMin.getText()),
                        Integer.parseInt(partMax.getText()),
                        Integer.parseInt(partMachineId.getText()));
                inventory.addPart(part);
            }
        }
    }

    /**
     * This method listens to the inHouse radio-listener to set the correct source.
     */
    @FXML
    private void inHouseListener() {

        if (radioInHouse.isSelected()) {
            radioOption.setText("Machine ID");
        }
        partId.setText(Integer.toString(part.getId()));
        partMachineId.promptTextProperty().setValue("Machine ID");
        radioOutsourced.setSelected(false);

    }

    /**
     * This method listens to the outsource radio-listener to set the correct source.
     */
    @FXML
    private void outSourcedListener() {
        if (radioOutsourced.isSelected()) {
            radioOption.setText("Company Name");
        }
        partMachineId.promptTextProperty().setValue("Company Name");
        radioInHouse.setSelected(false);

    }

    /**
     * This method cancels the part add form if nothing is done
     * and returns to main view.
     * @param event
     */
    @FXML
    private void hideModifyPartsForm(ActionEvent event) {

        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to cancel modifying a part?", OK, CANCEL);

        alert.setTitle("Exit the modify Part Form");
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res.equals(OK)) {
                try {
                    mainScreen(event, part);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else {
                alert.hide();
            }
        });

    }

    /**
     * This returns the user to the main screen
     * @param event
     * @param part
     */
    @FXML
    private void mainScreen(ActionEvent event, Part part) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/MainInventory.fxml"));
            InventoryController controller = new InventoryController();
            controller.setInventory(inventory);
            controller.setSelectedPart(part);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }

    /**
     * This method saves the modified part and pushes it to the main controller to be updated.
     * @param event 
     */
    @FXML
    private void saveModifiedPart(ActionEvent event) {
        List<TextField> textFields = Arrays.asList(partName, partInv, partMax, partMin, partMachineId, partPrice);
        if (!checkIfEmpty(textFields)) {
            if (validationPassed(textFields)) {
                if (checkMinMaxInv(Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), Integer.parseInt(partInv.getText()))) {
                    try {
                        mainScreen(event, part);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Item not modified");
                    }

                } else {
                    alertBox("Min should be less than Max. Inv Should be between Max and Min");
                }
            } else {
                alertBox("Name should be letters, Inv, Max, Min and Price should be numbers");
            }
        } else {
            alertBox("Every Field must be filled");
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
     * This method checks that the textfields are not empty.
     * @param ts This is a text field that has been selected by user to update info
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
     * @param ts Text field to be validated
     * @return
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
}
