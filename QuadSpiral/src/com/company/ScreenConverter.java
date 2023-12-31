package com.company;

public class ScreenConverter {
    private double cx, cy, rw, rh;
    private  int sw, sh;

    public ScreenConverter(double cx, double cy, double rw, double rh, int sw, int sh) {
        this.cx = cx;
        this.cy = cy;
        this.rw = rw;
        this.rh = rh;
        this.sw = sw;
        this.sh = sh;
    }

    public ScreenPoint r2s(RealPoint p) {
        double x = (p.getX() - cx)/rw * sw;
        double y = (cy - p.getY())/rh * sh;
        return new ScreenPoint((int)x, (int)y);
    }

    public RealPoint s2r(ScreenPoint p) {
        double x = cx + p.getC() * rw / sw;
        double y = cy - p.getR() * rh / sh;
        return new RealPoint(x, y);

    }

    public void moveCorner(RealPoint delta) {
        cx += delta.getX();
        cy += delta.getY();
    }

    public void changeScale(double s) {
        rw = (rw) * s;
        rh = (rh) * s;

    }

    public double getCx() {
        return cx;
    }

    public void setCx(double cx) {
        this.cx = cx;
    }

    public double getCy() {
        return cy;
    }

    public void setCy(double cy) {
        this.cy = cy;
    }

    public double getRw() {
        return rw;
    }

    public void setRw(double rw) {
        this.rw = rw;
    }

    public double getRh() {
        return rh;
    }

    public void setRh(double rh) {
        this.rh = rh;
    }

    public int getSw() {
        return sw;
    }

    public void setSw(int sw) {
        this.sw = sw;
    }

    public int getSh() {
        return sh;
    }

    public void setSh(int sh) {
        this.sh = sh;
    }
}
