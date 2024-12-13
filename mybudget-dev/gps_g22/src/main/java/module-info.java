module org.example.gps_g22 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires java.desktop;
    requires java.sql;
    requires itextpdf;


    opens org.example.gps_g22.model.data to javafx.base;

    opens org.example.gps_g22 to javafx.fxml;
    opens org.example.gps_g22.view to javafx.fxml;

    exports org.example.gps_g22;
    exports org.example.gps_g22.view;


}