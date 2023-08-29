package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PixelDrawerBI extends PixelDrawer{
    BufferedImage bi;

    public PixelDrawerBI(BufferedImage bi) {
        this.bi = bi;
    }

    @Override
    void drawPixel(int x, int y, Color color) {
        if (x > 0 && y > 0 && x < bi.getWidth() && y < bi.getHeight()) {
            bi.setRGB(x, y, color.getRGB());
        }

    }

    @Override
    Dimension getScreenSize() {
        return new Dimension(bi.getWidth(), bi.getHeight());
    }

    public BufferedImage getBufferedImgae() {
        return bi;
    }

    public void setBufferedImage(BufferedImage bi) {
        this.bi = bi;
    }
}
