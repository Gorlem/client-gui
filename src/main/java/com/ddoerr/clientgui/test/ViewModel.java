package com.ddoerr.clientgui.test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Random;

public class ViewModel {
    private final IntegerProperty size = new SimpleIntegerProperty(this, "size", 100);
    private final StringProperty labelText = new SimpleStringProperty(this, "labelText", "Label Text");
    private final StringProperty buttonText = new SimpleStringProperty(this, "buttonText", "Button Text");

    private final Random random = new Random();

    public IntegerProperty sizeProperty() {
        return size;
    }
    public int getSize() {
        return size.get();
    }

    public StringProperty labelTextProperty() {
        return labelText;
    }

    public StringProperty buttonTextProperty() {
        return buttonText;
    }

    public void changeSize() {
        size.set((random.nextInt(9) + 1) * 10);
    }
}
