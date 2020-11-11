package com.ddoerr.clientgui.bindings;

import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.WeakListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class BindingUtil {
    public static ObservableValue<OrderedText> text(StringProperty stringProperty) {
        return Bindings.createObjectBinding(() -> new LiteralText(stringProperty.get()).asOrderedText(), stringProperty);
    }

    public static ObservableList<Widget<?>> single(ObjectProperty<Widget<?>> property) {
        ObservableList<Widget<?>> widgets = FXCollections.observableArrayList(property.get());

        property.addListener(change -> {
            widgets.clear();
            widgets.add(property.get());
        });

        return widgets;
    }

    public static <T> ObservableList<Widget<?>> map(ObservableList<T> listSource, Function<T, ? extends Widget<?>> mapping) {
        ObservableList<Widget<?>> observableList = FXCollections.observableArrayList(listSource.stream().map(mapping).collect(toList()));

        listSource.addListener((ListChangeListener<? super T>) change -> {
            while (change.next()) {
                if (change.wasPermutated()) {
                    observableList.subList(change.getFrom(), change.getTo()).clear();
                    observableList.addAll(change.getFrom(), change.getList().subList(change.getFrom(), change.getTo())
                            .stream().map(mapping).collect(toList()));
                } else {
                    if (change.wasRemoved()) {
                        observableList.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                    if (change.wasAdded()) {
                        observableList.addAll(change.getFrom(), change.getAddedSubList()
                                .stream().map(mapping).collect(toList()));
                    }
                }
            }
        });

        return observableList;
    }
}
