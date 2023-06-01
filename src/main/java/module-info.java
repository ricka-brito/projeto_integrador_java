module com.example.testefx {
    requires javafx.controls;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.fxml;
        requires javafx.web;
    requires org.json;
    requires org.apache.commons.io;
    requires opencsv;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens com.example.testefx to javafx.fxml;
    exports com.example.testefx;
}