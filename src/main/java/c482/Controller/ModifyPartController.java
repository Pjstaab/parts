/**
 * @author: Brown oichoe
 * Modify Part Controller
 */

package c482.Controller;

import c482.Model.InHousePart;
import c482.Model.Inventory;
import c482.Model.OutsourcedPart;
import c482.Model.Part;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
 * This class provides functionality to modify and edit a arts features.
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
     * This sets up the part in the correct text fields to be  modified by user.
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

        if (this.part instanceof InHousePart) {

            radioInHouse.setSelected(true);
            radioOutsourced.setSelected(false);
            InHousePart inHousePart = (InHousePart) this.part;
            radioOption.setText("Machine ID");
            partMachineId.setText(Integer.toString(inHousePart.getMachineId()));
            partMachineId.promptTextProperty().setValue("Machine ID");

        } else {
            radioInHouse.setSelected(false);
            radioOutsourced.setSelected(true);
            OutsourcedPart outsourcedPart = (OutsourcedPart) this.part;
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
     * This method initializes the Modify Part Controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        List<TextField> textFields = Arrays.asList(partName, partPrice, partInv, partMin, partMax, partMachineId);
        textFields.forEach(textField -> {
            textField.textProperty().addListener((obs, old, newWord) -> {
                // TODO here

                try {

                    textField.setText(newWord);
                    setValues(textField);
                } catch (NumberFormatException e) {
                    System.out.println("I got you error");
                }

            });
        });

    }

    /**
     * This method gets user input and updates the required fields.
     *
     * @param ts This is a textfield that has been selected by user to update info
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

            if (part instanceof InHousePart && radioInHouse.isSelected()) {

                InHousePart inHousePart = (InHousePart) part;
                inHousePart.setMachineId(Integer.parseInt(ts.getText()));

            } else if (part instanceof OutsourcedPart && radioOutsourced.isSelected()) {
                OutsourcedPart outsourced = (OutsourcedPart) part;
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
     * This method listens to the inhouse radio-listener to set the correct source.
     */
    @FXML
    private void inHouseListener() {

        if (radioInHouse.isSelected())
            radioOption.setText("Machine ID");
        partId.setText(Integer.toString(part.getId()));
        partMachineId.promptTextProperty().setValue("Machine ID");
        radioOutsourced.setSelected(false);

    }

    /**
     * This method listens to the outsource radio-listener to set the correct source.
     */
    @FXML
    private void outSourcedListener() {
        if (radioOutsourced.isSelected())
            radioOption.setText("Company Name");
        partMachineId.promptTextProperty().setValue("Company Name");
        radioInHouse.setSelected(false);

    }

    /**
     * This method cancels the part add form if nothing is done
     * and returns to main view.
     */
    @FXML
    private void HideModifyPartsForm() throws IOException {

        ButtonType OK     = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("CANCEl", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Are You Sure You Want cancel Modifying Part", OK, CANCEL);

        alert.setTitle("Exit the Modify Part Form");
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res.equals(OK)) {

                try {
                    Stage  stage;
                    Parent root;

                    stage = (Stage) cancelPart.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader();
                    root = loader.load(getClass().getResource("../Inventory_main.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                }

            } else {
                alert.hide();
            }
        });

    }

    /**
     * This method saves the modified part and pushes it to the main controller to be updated.
     */
    @FXML
    private void SaveModifiedPart() throws IOException {

        Parent parent;
        Stage  stage;
        stage = (Stage) savePart.getScene().getWindow();

        List<TextField> textFields = Arrays.asList(partName, partInv, partMax, partMin, partMachineId, partPrice);

        if (!checkIfEmpty(textFields)) {
            if (validationPassed(textFields)) {
                if (checkMinMAxInv(Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), Integer.parseInt(partInv.getText()))) {

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Inventory_main.fxml"));
                        parent = loader.load();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.setTitle("Inventory");
                        InventoryController controller = loader.getController();
                        controller.setSelectedPart(part);
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

    @FXML
    private boolean validationPassed(List<TextField> ts) throws IOException {

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
                            if (Character.isDigit(Id.charAt(i))) {

                                isValid = false;
                                return isValid;
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
