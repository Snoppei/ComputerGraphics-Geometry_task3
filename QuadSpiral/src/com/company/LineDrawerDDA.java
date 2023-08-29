package com.company;

import java.awt.*;

public class LineDrawerDDA extends LineDrawer {
    PixelDrawer drawer;

    public LineDrawerDDA(PixelDrawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void drawLine(ScreenPoint p1, ScreenPoint p2, Color color) {
        int dx = p2.getC() - p1.getC();
        int dy = p2.getR() - p1.getR();

        int step;
        step = Math.max(Math.abs(dx), Math.abs(dy));

        double x_incr = (double)dx / step;
        double y_incr = (double)dy / step;

        double x = p1.getC();
        double y = p1.getR();

        for (int i = 0; i < step; i++) {
            int screenX = (int)Math.round(x);
            int screenY = (int)Math.round(y);

            drawer.drawPixel(screenX, screenY, color);

            x += x_incr;
            y += y_incr;
        }
    }
}