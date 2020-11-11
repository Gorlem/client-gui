package com.ddoerr.clientgui.bindings;

import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Size;
import com.sun.javafx.binding.IntegerConstant;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableObjectValue;

import java.util.function.Function;

public interface SizeExpression extends ObservableObjectValue<Size> {
    default SizeBinding add(int width, int height) {
        return SizeBinding.createSizeBinding(() -> Size.of(get().getWidth() + width, get().getHeight() + height), this);
    }

    default SizeBinding addWidth(int width) {
        return add(width, 0);
    }

    default SizeBinding addHeight(int height) {
        return add(0, height);
    }

    default SizeBinding divide(ObservableIntegerValue width, ObservableIntegerValue height) {
        return SizeBinding.createSizeBinding(() -> Size.of(get().getWidth() / width.get(), get().getHeight() / height.get()), this, width, height);
    }

    default SizeBinding divideWidth(ObservableIntegerValue width) {
        return divide(width, IntegerConstant.valueOf(1));
    }

    default SizeBinding divideHeight(ObservableIntegerValue height) {
        return divide(IntegerConstant.valueOf(1), height);
    }

    default SizeBinding divide(int width, int height) {
        return divide(IntegerConstant.valueOf(width), IntegerConstant.valueOf(height));
    }

    default SizeBinding divideWidth(int width) {
        return divide(width, 1);
    }

    default SizeBinding divideHeight(int height) {
        return divide(1, height);
    }

    default SizeBinding multiply(int width, int height) {
        return SizeBinding.createSizeBinding(() -> Size.of(get().getWidth() * width, get().getHeight() * height), this);
    }

    default SizeBinding multiplyWidth(int width) {
        return divide(width, 1);
    }

    default SizeBinding multiplyHeight(int height) {
        return divide(1, height);
    }

    default SizeBinding setWidth(int width) {
        return SizeBinding.createSizeBinding(() -> Size.of(width, get().getHeight()), this);
    }

    default SizeBinding setHeight(int height) {
        return SizeBinding.createSizeBinding(() -> Size.of(get().getWidth(), height), this);
    }

    default SizeBinding addOuterInsets(ObjectProperty<Insets> insetsProperty) {
        return SizeBinding.createSizeBinding(() ->
                Size.of(get().getWidth() + insetsProperty.get().getWidth(), get().getHeight() + insetsProperty.get().getHeight()),
                this, insetsProperty);
    }

    default SizeBinding addInnerInsets(ObjectProperty<Insets> insetsProperty) {
        return SizeBinding.createSizeBinding(() ->
                Size.of(get().getWidth() - insetsProperty.get().getWidth(), get().getHeight() - insetsProperty.get().getHeight()),
                this, insetsProperty);
    }

    default SizeBinding calculate(Function<Size, Size> function) {
        return SizeBinding.createSizeBinding(() -> function.apply(get()), this);
    }
}
