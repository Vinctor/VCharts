package com.vinctor.vchartviews.diagram;

/**
 * Created by Vinctor on 2017/4/19.
 */

public class DiagramData {
    int diagramInnerColorUnselected;
    int diagramInnerColorselected;

    int diagramBorderColorUnselected;
    int diagramBorderColorSelected;

    int textColorSelected = 0;
    int textColorUnselected = 0;

    int diagramBorderWidth = -1;

    String text;

    public DiagramData(int diagramInnerColorUnselected, int diagramInnerColorselected, int diagramBorderColorUnselected, int diagramBorderColorSelected, int textColorSelected, int textColorUnselected, int diagramBorderWidth, String text) {
        this.diagramInnerColorUnselected = diagramInnerColorUnselected;
        this.diagramInnerColorselected = diagramInnerColorselected;
        this.diagramBorderColorUnselected = diagramBorderColorUnselected;
        this.diagramBorderColorSelected = diagramBorderColorSelected;
        this.textColorSelected = textColorSelected;
        this.textColorUnselected = textColorUnselected;
        this.diagramBorderWidth = diagramBorderWidth;
        this.text = text;
    }

    public DiagramData(int color, String text) {
        this.diagramInnerColorUnselected = color;
        this.diagramInnerColorselected = color;
        this.diagramBorderColorUnselected = color;
        this.diagramBorderColorSelected = color;
        this.text = text;
    }
}
