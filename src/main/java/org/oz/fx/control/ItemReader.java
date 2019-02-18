package org.oz.fx.control;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ItemReader extends HBox {


    @FXML
    private JFXCheckBox label;

    @FXML
    private JFXRadioButton status;

    @FXML
    private JFXButton control;

    public ItemReader() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/layout/item_reader.fxml"));

        fxmlLoader.setRoot(this);

        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @FXML
    public void initialize() {


    }

    public String getLable() {
        return textProperty().get();
    }

    public void setLable(String value) {
        textProperty().set(value);
    }

    private StringProperty textProperty() {
        return label.textProperty();
    }


    @FXML
    protected void onControl() {

        if (status.selectedProperty().get()) {
            status.selectedProperty().setValue(false);
            status.textProperty().setValue("D");
        } else {
            status.selectedProperty().setValue(true);
            status.textProperty().setValue("A");
        }


    }

}
