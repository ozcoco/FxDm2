package org.oz.fx.control;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ItemReader extends VBox {


    @FXML
    private TextField txSomething;

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

        System.out.println("init ItemReader");

        txSomething.setStyle("-fx-background-color: #99CCFF;");

    }


    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return txSomething.textProperty();
    }


    @FXML
    protected void onSomething() {
        System.out.println("The button was clicked!");
    }

}
