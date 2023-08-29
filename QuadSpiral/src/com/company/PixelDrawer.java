package com.company;

import java.awt.*;

public abstract class PixelDrawer {
    abstract void drawPixel(int x, int y, Color color);
    abstract Dimension getScreenSize();
}
