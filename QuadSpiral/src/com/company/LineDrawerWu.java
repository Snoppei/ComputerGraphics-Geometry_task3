package com.company;

import java.awt.*;

public class LineDrawerWu extends LineDrawer {
    PixelDrawer drawer;

    public LineDrawerWu(PixelDrawer drawer) {
        this.drawer = drawer;
    }

    Color addAlphaChannel(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, alpha)));
    }

    @Override
    public void drawLine(ScreenPoint p1, ScreenPoint p2, Color color) {
        int x1 = p1.getC();
        int x2 = p2.getC();
        int y1 = p1.getR();
        int y2 = p2.getR();


        if (Math.abs(x2) <= Math.abs(x1)) {
            x1 += x2;
            x2 = x1 - x2;
            x1 -= x2;
            y1 += y2;
            y2 = y1 - y2;
            y1 -= y2;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;

        double gradient = 0;
        if (Math.abs(dx) > Math.abs(dy)) {
            gradient = (double) dy / dx;
            double intery = y1 + gradient;
            drawer.drawPixel(x1, y1, color);
            for (int x = x1; x < x2; ++x) {
                drawer.drawPixel(x, (int)intery, addAlphaChannel(color, (int) (255 - fractionalPart(intery) * 255)));
                drawer.drawPixel(x, (int)intery + 1, addAlphaChannel(color, (int) (fractionalPart(intery) * 255)));

                intery += gradient;
            }
            drawer.drawPixel(x2, y2, color);
        } else {
            gradient = (double) dx / dy;
            double interx = x1 + gradient;
            drawer.drawPixel(x1, y1, color);
            for (int y = y1; y < y2; ++y) {
                drawer.drawPixel((int)interx, y, addAlphaChannel(color, (int) (255 - fractionalPart(interx) * 255)));
                drawer.drawPixel((int)interx + 1, y, addAlphaChannel(color, (int) (fractionalPart(interx) * 255)));

                interx += gradient;
            }
            drawer.drawPixel(x2, y2, color);

            x1 += x2;
            x2 = x1 - x2;
            x1 -= x2;
            y1 += y2;
            y2 = y1 - y2;
            y1 -= y2;

            gradient = (double) dx / dy;
            interx = x1 + gradient;
            drawer.drawPixel(x1, y1, color);
            for (int y = y1; y < y2; ++y) {
                drawer.drawPixel((int)interx, y, addAlphaChannel(color, (int) (255 - fractionalPart(interx) * 255)));
                drawer.drawPixel((int)interx + 1, y, addAlphaChannel(color, (int) (fractionalPart(interx) * 255)));

                interx += gradient;
            }
            drawer.drawPixel(x2, y2, color);
        }
    }

    private double fractionalPart(double x) {
        return x % 1;
    }
}
