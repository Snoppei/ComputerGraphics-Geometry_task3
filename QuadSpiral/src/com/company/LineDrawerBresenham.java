package com.company;

import java.awt.*;

public class LineDrawerBresenham extends LineDrawer {
    PixelDrawer drawer;

    public LineDrawerBresenham(PixelDrawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void drawLine(ScreenPoint p1, ScreenPoint p2, Color color) {
        int dx = Math.abs(p2.getC() - p1.getC());
        int dy = Math.abs(p2.getR() - p1.getR());

        int sx = (p1.getC() < p2.getC()) ? 1 : -1;
        int sy = (p1.getR() < p2.getR()) ? 1 : -1;

        int err = dx - dy;

        int x = p1.getC();
        int y = p1.getR();
        while (true) {
            drawer.drawPixel(x, y, color);

            if (x == p2.getC() && y == p2.getR()) {
                break;
            }

            int e2 = 2 * err;

            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }

            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }
}