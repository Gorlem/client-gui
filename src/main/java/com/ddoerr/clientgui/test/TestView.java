package com.ddoerr.clientgui.test;

import com.ddoerr.clientgui.attachments.TooltipAttachment;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.views.AbstractView;
import com.ddoerr.clientgui.widgets.functional.*;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.layout.CardWidget;
import com.ddoerr.clientgui.widgets.layout.GridWidget;
import com.ddoerr.clientgui.widgets.layout.StackWidget;
import com.ddoerr.clientgui.widgets.visual.ColorWidget;
import com.ddoerr.clientgui.widgets.visual.ItemWidget;
import com.ddoerr.clientgui.widgets.visual.LabelWidget;
import javafx.beans.binding.Bindings;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class TestView extends AbstractView {
    private final ViewModel viewModel;

    public TestView() {
        viewModel = new ViewModel();
    }

    @Override
    public void build() {
        this.setPadding(Insets.of(5));
        AnchorWidget anchorWidget = new AnchorWidget();

        StackWidget stackWidget = new StackWidget();
        stackWidget.setStackDirection(StackWidget.StackDirection.Vertical);

        ItemWidget itemWidget = new ItemWidget();
        itemWidget.setItem(Items.MINECART);
        stackWidget.addChild(itemWidget);

        CheckboxWidget checkboxWidget1 = new CheckboxWidget().setMargin(Insets.of(1));
        stackWidget.addChild(checkboxWidget1);

        CheckboxWidget checkboxWidget2 = new CheckboxWidget().setMargin(Insets.of(1));
        stackWidget.addChild(checkboxWidget2);

        ColorWidget colorWidget = new ColorWidget();
        colorWidget.setColor(Color.parse("RED 50%"));
        colorWidget.sizeProperty().bind(Bindings.createObjectBinding(() -> Size.of(viewModel.getSize()), viewModel.sizeProperty()));
        stackWidget.addChild(colorWidget);

        LabelWidget labelWidget = new LabelWidget();
        labelWidget.setForegroundColor(Color.fromFormatting(Formatting.GOLD));
        labelWidget.setText(viewModel.labelTextProperty().get());
        labelWidget.setPadding(Insets.of(2));
        labelWidget.setMargin(Insets.of(5));
        stackWidget.addChild(labelWidget);

        ButtonWidget buttonWidget = new ButtonWidget();
        buttonWidget.setSize(Size.of(100, 20));
        buttonWidget.addActionListener((event) -> {
            viewModel.changeSize();

            buttonWidget.setEnabled(false);
        });
        buttonWidget.setMargin(Insets.of(5));
        buttonWidget.setText("Hello World");

        TooltipAttachment tooltip = new TooltipAttachment(buttonWidget);
        tooltip.addTextLine(new LiteralText("Hello World"));
        buttonWidget.attach(tooltip);

        stackWidget.addChild(buttonWidget);

        anchorWidget.addChild(stackWidget, AnchorWidget.Anchor.BottomRight);
        this.setChild(anchorWidget);

        GridWidget gridWidget = new GridWidget();
        gridWidget.setSize(Size.of(300, 300));
        gridWidget.setGridDefinition(new GridWidget.GridDefinition(4, 3));

//        int color = 0;
//        for (int column = 0; column < 4; column++) {
//            for (int row = 0; row < 3; row++) {
//                color++;
//                ColorWidget cellWidget = new ColorWidget();
//                cellWidget.setColor(Color.colors[color]);
//                gridWidget.addChild(cellWidget, new GridWidget.CellPosition(column, row, true));
//            }
//        }

        ColorWidget colorWidget1 = new ColorWidget();
        colorWidget1.setColor(Color.BLUE.addAlpha(0.5));
        colorWidget1.setMargin(Insets.of(3));
        gridWidget.addChild(colorWidget1, new GridWidget.CellPosition(0, 2, 0, 2), true);

        ColorWidget colorWidget2 = new ColorWidget();
        colorWidget2.setColor(Color.ORANGE.addAlpha(0.5));
        colorWidget2.setMargin(Insets.of(4));
        gridWidget.addChild(colorWidget2, new GridWidget.CellPosition(2, 1, 0, 3), true);

        ColorWidget colorWidget3 = new ColorWidget();
        colorWidget3.setColor(Color.GREEN.addAlpha(0.5));
        colorWidget3.setMargin(Insets.of(2));
        gridWidget.addChild(colorWidget3, new GridWidget.CellPosition(0, 4, 2, 1), true);

        anchorWidget.addChild(gridWidget, AnchorWidget.Anchor.BottomLeft);

        anchorWidget.addChild(new TextFieldWidget().setSize(Size.of(200, 20)), AnchorWidget.Anchor.TopRight);

        CardWidget cardWidget = new CardWidget();
        cardWidget.setSize(Size.of(100, 20));
        cardWidget.addChild(new ButtonWidget().setText("Btn 1").Do(w -> w.addActionListener(e -> cardWidget.setSelectedIndex(1))));
        cardWidget.addChild(new ButtonWidget().setText("Btn 2").Do(w -> w.addActionListener(e -> cardWidget.setSelectedIndex(2))));
        cardWidget.addChild(new ButtonWidget().setText("Btn 3").Do(w -> w.addActionListener(e -> cardWidget.setSelectedIndex(0))));

        anchorWidget.addChild(cardWidget, AnchorWidget.Anchor.TopLeft);

        anchorWidget.addChild(new TabWidget()
                        .setSize(Size.of(250, 100))
                        .addTab(new LiteralText("Hello World").asOrderedText(), new ColorWidget().setColor(Color.RED))
                        .addTab(new LiteralText("Second tab").asOrderedText(), new ColorWidget().setColor(Color.BLUE))
                        .addTab(new LiteralText("End").asOrderedText(), new ColorWidget().setColor(Color.GREEN)),
                AnchorWidget.Anchor.TopCenter);
    }
}
