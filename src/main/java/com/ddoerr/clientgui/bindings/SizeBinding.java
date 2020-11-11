package com.ddoerr.clientgui.bindings;

import com.ddoerr.clientgui.models.Size;
import com.sun.javafx.binding.Logging;
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.Callable;

public abstract class SizeBinding extends ObjectBinding<Size> implements SizeExpression {
    public static SizeBinding createSizeBinding(final Callable<Size> func, final Observable... dependencies) {
        return new SizeBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected Size computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    Logging.getLogger().warning("Exception while evaluating binding", e);
                    return null;
                }
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                        FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<>(dependencies);
            }
        };
    }
}
