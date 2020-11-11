package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.util.Renderer;
import com.google.common.primitives.Ints;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

public class SelectionAttachment {
    protected final StringProperty content = new SimpleStringProperty(this, "content", "");

    protected final IntegerProperty cursorIndex = new SimpleIntegerProperty(this, "cursorIndex", 0);
    protected final IntegerProperty selectionIndex = new SimpleIntegerProperty(this, "selectionIndex", 0);

    protected final ReadOnlyIntegerWrapper firstIndex = new ReadOnlyIntegerWrapper(this, "firstIndex", 0);
    protected final ReadOnlyIntegerWrapper secondIndex = new ReadOnlyIntegerWrapper(this, "secondIndex", 0);

    protected final ReadOnlyStringWrapper selectionContent = new ReadOnlyStringWrapper(this, "selectionContent", "");

    protected final ReadOnlyObjectWrapper<Rectangle> cursor = new ReadOnlyObjectWrapper<>(this, "cursor");
    protected final ReadOnlyObjectWrapper<Rectangle> selection = new ReadOnlyObjectWrapper<>(this, "selection");

    public SelectionAttachment() {
        firstIndex.bind(Bindings.min(cursorIndex, selectionIndex));
        secondIndex.bind(Bindings.max(cursorIndex, selectionIndex));
        selectionContent.bind(Bindings.createStringBinding(() -> content.get().substring(firstIndex.get(), secondIndex.get()), content, firstIndex, secondIndex));
        cursor.bind(Bindings.createObjectBinding(() -> Rectangle.of(
                Point.of(Renderer.textWidth(content.get().substring(0, cursorIndex.get())), 0),
                Size.of(1, Renderer.textHeight())
        ), content, cursorIndex));
        selection.bind(Bindings.createObjectBinding(() -> Rectangle.of(
                Point.of(Renderer.textWidth(content.get().substring(0, firstIndex.get())), 0),
                Size.of(Renderer.textWidth(selectionContent.get()), Renderer.textHeight())
        ), content, firstIndex, selectionContent));
    }

    public StringProperty contentProperty() {
        return content;
    }

    public IntegerProperty cursorIndexProperty() {
        return cursorIndex;
    }

    public IntegerProperty selectionIndexProperty() {
        return selectionIndex;
    }

    public ReadOnlyStringProperty selectionContentProperty() {
        return selectionContent.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Rectangle> cursorProperty() {
        return cursor.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Rectangle> selectionProperty() {
        return selection.getReadOnlyProperty();
    }

    public int moveCursorIndex(int amount) {
        return setCursorIndex(cursorIndex.get() + amount);
    }

    public int setCursorIndex(int index) {
        if (index < 0) {
            index = content.get().length() + index + 1;
        }

        int constrainedIndex = Ints.constrainToRange(index, 0, content.get().length());
        cursorIndex.set(constrainedIndex);
        return constrainedIndex;
    }

    public int moveSelectionIndex(int amount) {
        return setSelectionIndex(selectionIndex.get() + amount);
    }

    public int setSelectionIndex(int index) {
        if (index < 0) {
            index = content.get().length() + index + 1;
        }

        int constrainedIndex = Ints.constrainToRange(index, 0, content.get().length());
        selectionIndex.set(constrainedIndex);
        return constrainedIndex;
    }

    public void append(char character) {
        append(Character.toString(character));
    }

    public void append(String string) {
        int diff = selectionIndex.get() - cursorIndex.get() + string.length();
        content.set(new StringBuilder(content.get()).replace(firstIndex.get(), secondIndex.get(), string).toString());
        int index = moveCursorIndex(diff);
        setSelectionIndex(index);
    }

    public void remove(int count) {
        if (selectionIndex.get() == cursorIndex.get()) {
            int constrainedIndex = Ints.constrainToRange(cursorIndex.get() + count, 0, content.get().length());

            int firstIndex = Math.min(constrainedIndex, cursorIndex.get());
            int secondIndex = Math.max(constrainedIndex, cursorIndex.get());

            content.set(new StringBuilder(content.get()).delete(firstIndex, secondIndex).toString());
            if (count < 0) {
                int index = moveCursorIndex(count);
                setSelectionIndex(index);
            }
        } else {
            append("");
        }
    }
}
