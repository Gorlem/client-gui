package com.ddoerr.clientgui.test;

import com.ddoerr.clientgui.attachments.TooltipAttachment;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.views.AbstractView;
import com.ddoerr.clientgui.widgets.*;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.layout.GridWidget;
import com.ddoerr.clientgui.widgets.layout.ListWidget;
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
        this.setPadding(new Insets(5));
        AnchorWidget anchorWidget = new AnchorWidget();

        ListWidget listWidget = new ListWidget();
        listWidget.setStackDirection(ListWidget.StackDirection.Vertical);

        ItemWidget itemWidget = new ItemWidget();
        itemWidget.setItem(Items.MINECART);
        listWidget.addChild(itemWidget, null);

        CheckboxWidget checkboxWidget1 = new CheckboxWidget();
        listWidget.addChild(checkboxWidget1);

        CheckboxWidget checkboxWidget2 = new CheckboxWidget();
        listWidget.addChild(checkboxWidget2);

        ColorWidget colorWidget = new ColorWidget();
        colorWidget.setColor(Color.parse("RED 50%"));
        colorWidget.sizeProperty().bind(Bindings.createObjectBinding(() -> new Size(viewModel.getSize()), viewModel.sizeProperty()));
        listWidget.addChild(colorWidget, null);

        LabelWidget labelWidget = new LabelWidget();
        labelWidget.setForegroundColor(Color.fromFormatting(Formatting.GOLD));
        labelWidget.setText(viewModel.labelTextProperty().get());
        labelWidget.setPadding(new Insets(2));
        labelWidget.setMargin(new Insets(5));
        listWidget.addChild(labelWidget, null);

        ButtonWidget buttonWidget = new ButtonWidget();
        buttonWidget.setSize(new Size(100, 20));
        buttonWidget.addActionListener((event) -> {
            viewModel.changeSize();

            ButtonWidget.ButtonStyle buttonStyle = buttonWidget.getButtonStyle();
            ButtonWidget.ButtonStyle[] values = ButtonWidget.ButtonStyle.values();
            ButtonWidget.ButtonStyle next = values[(buttonStyle.ordinal() + 1) % values.length];
            buttonWidget.setButtonStyle(next);
        });
        buttonWidget.setMargin(new Insets(5));

        TooltipAttachment attachment = new TooltipAttachment(buttonWidget);
        attachment.addTextLine(new LiteralText("Hello World"));
        buttonWidget.attach(attachment);

        LabelWidget buttonLabel = new LabelWidget();
        buttonLabel.setText("Button Label");
        buttonLabel.setMargin(new Insets(2, 0, 0, 0));
        buttonLabel.setPadding(new Insets(2));

        ItemWidget buttonLabelItem = new ItemWidget();
        buttonLabelItem.setItem(Items.BARRIER);

        ListWidget buttonLabelList = new ListWidget();
        buttonLabelList.setStackDirection(ListWidget.StackDirection.Horizontal);
        buttonLabelList.addChild(buttonLabelItem);
        buttonLabelList.addChild(buttonLabel);

        AnchorWidget buttonLabelAnchor = new AnchorWidget();
        buttonLabelAnchor.addChild(buttonLabelList, AnchorWidget.Anchor.MiddleCenter);

        buttonWidget.setChild(buttonLabelAnchor);

        listWidget.addChild(buttonWidget);

        anchorWidget.addChild(listWidget, AnchorWidget.Anchor.BottomRight);
        this.setChild(anchorWidget);

        GridWidget gridWidget = new GridWidget();
        gridWidget.setSize(new Size(300, 300));
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
        colorWidget1.setMargin(new Insets(3));
        gridWidget.addChild(colorWidget1, new GridWidget.CellPosition(0, 2, 0, 2), true);

        ColorWidget colorWidget2 = new ColorWidget();
        colorWidget2.setColor(Color.ORANGE.addAlpha(0.5));
        colorWidget2.setMargin(new Insets(4));
        gridWidget.addChild(colorWidget2, new GridWidget.CellPosition(2, 1, 0, 3), true);

        ColorWidget colorWidget3 = new ColorWidget();
        colorWidget3.setColor(Color.GREEN.addAlpha(0.5));
        colorWidget3.setMargin(new Insets(2));
        gridWidget.addChild(colorWidget3, new GridWidget.CellPosition(0, 4, 2, 1), true);

        anchorWidget.addChild(gridWidget, AnchorWidget.Anchor.BottomLeft);
    }
}
