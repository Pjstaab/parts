module c482 {
    requires javafx.controls;
    requires javafx.fxml;


    opens c482 to javafx.fxml;
    exports c482;
    exports c482.Controller;
    opens c482.Controller to javafx.fxml;
}