package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuadSpiral {
    private RealPoint center;
    private double startAngle, radius;
    private Color color = Color.black;
    private int laps = 2;
    private int step = 15;

    public QuadSpiral(RealPoint center, double startAngle, double radius, Color color, int laps, int step) {
        this.center = center;
        this.startAngle = startAngle;
        this.radius = radius;
        this.color = color;
        this.laps = laps;
        this.step = step;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }


    public void rotate(double angleDelta) {
        startAngle += angleDelta;
        startAngle %= 360.0;
    }

    public List<Line> getLines() {
        List<Line> lines = new ArrayList<>();

        double z = startAngle;
        double angle = Math.PI/2;
        double r = 0;
        double deltaR = radius/(laps*4);

        List<RealPoint> points = new ArrayList<>();
        for (int i = 0; i < laps*4; i++) {
            /*
            //g.drawLine(widthCenter + (step * i), heightCenter - (step * i), widthCenter + (step * i), heightCenter + step + (step * i));
            RealPoint real = new RealPoint(center.getX()+(step*i), center.getY()-(step*i));
            points.add(real);
            real = new RealPoint(center.getX()+(step*i), center.getY()+step+(step*i));
            points.add(real);
            //g.drawLine(widthCenter + (step * i), heightCenter + step + (step * i), widthCenter - step - (step * i), heightCenter + step + (step * i));
            real = new RealPoint(center.getX() + (step*i), center.getY()+step+(step*i));
            points.add(real);
            real = new RealPoint(center.getX() - step - (step*i), center.getY()+step+(step*i));
            points.add(real);
            //g.drawLine(widthCenter - step - (step * i), heightCenter + step + (step * i), widthCenter - step - (step * i), heightCenter - step - (step * i));
            real = new RealPoint(center.getX() - step - (step*i), center.getY() + step + (step*i));
            points.add(real);
            real = new RealPoint(center.getX()-step-(step*i), center.getY() - step - (step*i));
            points.add(real);
            //g.drawLine(widthCenter - step - (step * i), heightCenter - step - (step * i), widthCenter + step + (step * i), heightCenter - step - (step * i));
            real = new RealPoint(center.getX() - step - (step*i), center.getY() - step - (step*i));
            points.add(real);
            real = new RealPoint(center.getX() + step + (step*i), center.getY() - step - (step*i));
            points.add(real);*/
            double a = Math.cos(z);
            double b = Math.sin(z);

            RealPoint real = new RealPoint(center.getX() + (a * r),center.getY() - (b * r));
            points.add(real);

            z = z + angle;
            r = r+deltaR;
        }

        for (int i = 1; i < points.size(); i++) {
            lines.add(new Line(points.get(i - 1), points.get(i), color));
        }

        return lines;
    }


    public RealPoint getCenter() {
        return center;
    }

    public void setCenter(RealPoint center) {
        this.center = center;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getLaps() {
        return laps;
    }

    public void setLaps(int laps) {
        this.laps = laps;
    }
}
