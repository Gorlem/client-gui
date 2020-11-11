package com.ddoerr.clientgui.bindings;

import com.ddoerr.clientgui.models.Size;
import javafx.beans.property.SimpleObjectProperty;

public class SizeProperty extends SimpleObjectProperty<Size> implements SizeExpression {
    public SizeProperty() {
    }

    public SizeProperty(Size initialValue) {
        super(initialValue);
    }

    public SizeProperty(Object bean, String name) {
        super(bean, name);
    }

    public SizeProperty(Object bean, String name, Size initialValue) {
        super(bean, name, initialValue);
    }
}
