module main.java.com.vsucg {
    requires javafx.controls;
    requires javafx.fxml;
    requires vecmath;
    requires java.desktop;


    opens main.java.com.vsucg to javafx.fxml;
    exports main.java.com.vsucg;
}