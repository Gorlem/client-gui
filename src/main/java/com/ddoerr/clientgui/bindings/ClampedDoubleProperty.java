package com.ddoerr.clientgui.bindings;

import com.sun.javafx.binding.DoubleConstant;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import net.minecraft.util.math.MathHelper;

public class ClampedDoubleProperty extends SimpleDoubleProperty {
    private final ObservableDoubleValue min;
    private final ObservableDoubleValue max;

    public ClampedDoubleProperty(Object bean, String name, double initialValue, double min, double max) {
        super(bean, name, initialValue);
        this.min = DoubleConstant.valueOf(min);
        this.max = DoubleConstant.valueOf(max);
    }

    public ClampedDoubleProperty(Object bean, String name, double initialValue, ObservableDoubleValue min, ObservableDoubleValue max) {
        super(bean, name, initialValue);
        this.min = min;
        this.max = max;
    }

    @Override
    public double get() {
        return MathHelper.clamp(super.get(), min.get(), max.get());
    }
}
