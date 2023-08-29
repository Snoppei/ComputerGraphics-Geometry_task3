package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
/*    private int x = 0, y = 0;*/
    private ScreenConverter sc;
    private PixelDrawerBI pixelDrawer;
    private LineDrawer lineDrawer;
    private Line ox, oy;
    private java.util.List<QuadSpiral> spirals = new ArrayList<>();
    private QuadSpiral selectedSpiralByPoint = null;
    private QuadSpiral selectedSpiralByCenter = null;


    public DrawPanel() {
        sc = new ScreenConverter(-2, 2, 4, 4, 800, 600);
        ox = new Line(new RealPoint(-1, 0), new RealPoint(1, 0), Color.BLACK);
        oy = new Line(new RealPoint(0, -1), new RealPoint(0, 1), Color.BLACK);

        pixelDrawer = new PixelDrawerBI(null);
        lineDrawer = new LineDrawerDDA(pixelDrawer);

        JComboBox comboBox = new JComboBox(new String[]{"DDA", "Bresenham", "Wu"});
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selectedItem = e.getItem().toString();
                switch (selectedItem) {
                    case "DDA":
                        lineDrawer = new LineDrawerDDA(pixelDrawer);
                        break;
                    case "Bresenham":
                        lineDrawer = new LineDrawerBresenham(pixelDrawer);
                        break;
                    case "Wu":
                        lineDrawer = new LineDrawerWu(pixelDrawer);
                        break;
                }

                System.out.println("HERE");
                repaint();
            }
        });

        this.add(comboBox);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
/*        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                ScreenPoint sp = new ScreenPoint(e.getX(), e.getY());
                RealPoint rp = sc.s2r(sp);
                l.setP2(rp);
                repaint();
            }
        });*/
    }

    @Override
    protected void paintComponent(Graphics origG) {
        sc.setSw(getWidth());
        sc.setSh(getHeight());
        origG.setColor(new Color(255, 255, 255, 255));
        origG.fillRect(0, 0, getWidth(), getHeight());

        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB); // двойная буфферизация

        Graphics2D g = (Graphics2D)bi.createGraphics();                                                         // для устранения мерцания
        g.setColor(new Color(255, 255, 255, 255));
        g.fillRect(0, 0, getWidth(), getHeight());

/*        g.setColor(Color.BLUE);
        drawLine(g, sc, ox);
        drawLine(g, sc, oy);
        g.setColor(Color.BLACK);*/

        pixelDrawer.setBufferedImage(bi);

        drawLine(ox);
        drawLine(oy);
        lineDrawer.drawLine(new ScreenPoint(500, 50), new ScreenPoint(5, 55), Color.BLACK);

/*        for(Line l : allLines)
        drawLine(g, sc, l);
        if(currentLine != null) {
            g.setColor(Color.RED);
            drawLine(g, sc, currentLine);
        }
        if(editingLine != null) {
            g.setColor(Color.GREEN);
            drawLine(g, sc, editingLine);
        }*/
/*        g.setColor(Color.BLACK);
        g.drawLine(getWidth() / 2, getHeight() / 2, x, y);*/

        for (QuadSpiral spiral : spirals) {
            drawQuadSpiral(spiral);
        }

        origG.drawImage(bi, 0, 0, null);
        g.dispose();
        //repaint();
    }

    private void drawQuadSpiral(QuadSpiral qs) {
        List<Line> realLines = qs.getLines(); // не реализовано пока что

        for (Line l : realLines)
            drawLine(l);

        ScreenPoint screenCenter = sc.r2s(qs.getCenter());
        pixelDrawer.drawPixel(screenCenter.getC(), screenCenter.getR(), qs.getColor());
    }

    private static QuadSpiral findQuadSpiralByPoint(ScreenConverter sc, List<QuadSpiral> spirals, ScreenPoint searchPoint, int eps) {
        for (QuadSpiral qs : spirals) {
            for (Line pLine : qs.getLines()) {
                if (closeToLineEnd(sc, pLine, searchPoint, eps)) return qs;
            }
        }

        return null;
    }

    private static QuadSpiral findQuadSpiralByCenter(ScreenConverter sc, List<QuadSpiral> spirals, ScreenPoint searchPoint, int eps) {
        for (QuadSpiral qs : spirals) {
            if (isNear(sc.r2s(qs.getCenter()), searchPoint, eps)) return qs;
        }

        return null;
    }


    private void drawLine(Line l) {
        ScreenPoint p1 = sc.r2s(l.getP1());
        ScreenPoint p2 = sc.r2s(l.getP2());

        lineDrawer.drawLine(p1, p2, l.getColor());
    }

    private static double distanceToLine(ScreenPoint lp1, ScreenPoint lp2, ScreenPoint cp){
        double a = lp2.getR() - lp1.getR();
        double b = -(lp2.getC() - lp1.getC());
        double e = -cp.getC()*b + cp.getR()*a;
        double f = a*lp1.getC() - b*lp1.getR();
        double y = (a*e-b*f)/(a*a+b*b);
        double x = (a*y-e)/b;
        return Math.sqrt((cp.getC()-x)*(cp.getC()-x) + (cp.getR()-y)*(cp.getR()-y));
    }
    private static boolean isPointInRect(ScreenPoint pr1, ScreenPoint pr2, ScreenPoint cp) {
        return  cp.getC() >= Math.min(pr1.getC(), pr2.getC()) &&
                cp.getC() <= Math.max(pr1.getC(), pr2.getC()) &&
                cp.getR() >= Math.min(pr1.getR(), pr2.getR()) &&
                cp.getR() <= Math.max(pr1.getR(), pr2.getR());
    }

    private static boolean closeToLineEnd(ScreenConverter sc, Line l, ScreenPoint sp, int eps) {
        ScreenPoint a = sc.r2s(l.getP1());
        ScreenPoint b = sc.r2s(l.getP2());
        return isNear(a, sp, eps) || isNear(b, sp, eps);
    }

    private static Line findLine(ScreenConverter sc, java.util.List<Line> lines, ScreenPoint searchPoint, int eps) {
        for(Line l : lines) {
            if(closeToLineEnd(sc, l, searchPoint, eps));
                return l;
        }
        return null;
    }

    private static int distanceBetweenPoints(ScreenPoint p1, ScreenPoint p2) {
        int dx = p1.getC() - p2.getC();
        int dy = p1.getR() - p2.getR();

        return dx * dx + dy * dy;
    }

    private static boolean isNear(ScreenPoint p1, ScreenPoint p2, double eps) {
        return distanceBetweenPoints(p1, p2) < eps;
    }

/*    private static QuadSpiral findQuadSpiralByPoint(ScreenConverter sc, List<QuadSpiral> spirals, ScreenPoint searchPoint, int eps) {
        for (QuadSpiral p : spirals) {
            for (Line pLine : p.getLines()) { // реализации нету
                if (closeToLineEnd(sc, pLine, searchPoint, eps)) return p;
            }
        }

        return null;
    }*/

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            spirals.add(new QuadSpiral(sc.s2r(new ScreenPoint(e.getX(), e.getY())), 0, 8, Color.BLACK, 15, 3));

            repaint();
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (selectedSpiralByCenter != null) {
                spirals.remove(selectedSpiralByCenter);
                selectedSpiralByCenter = null;

                repaint();
            }
        }
    }

    private ScreenPoint prevPoint = null;

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            prevPoint = new ScreenPoint(e.getX(), e.getY());
        }  /*else if(SwingUtilities.isLeftMouseButton(e)) {
            if(editingLine == null) {
                Line x = findLine(sc, allLines, new ScreenPoint(e.getX(), e.getY()), 3);
                if(x != null){
                    editingLine = x;
                } else {
                    RealPoint p = sc.s2r(new ScreenPoint(e.getX(), e.getY()));
                    currentLine = new Line(p, p);
                }
            } else {
                if(closeToLineEnd(sc, editingLine, new ScreenPoint(e.getX(), e.getY()), 3)) {
                    *//**//*
                } else {
                    editingLine = null;
                }
            }
        }
        repaint();*/
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            prevPoint = null;
        } /* else if(SwingUtilities.isLeftMouseButton(e)) {
            if(currentLine != null)
                currentLine.setP2(sc.s2r(new ScreenPoint(e.getX(), e.getY())));
                allLines.add(currentLine);
                currentLine = null;
        }
        repaint();*/
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (selectedSpiralByPoint != null) {
                RealPoint curPoint = sc.s2r(new ScreenPoint(e.getX(), e.getY()));

                double dx = curPoint.getX() - selectedSpiralByPoint.getCenter().getX();
                double dy = curPoint.getY() - selectedSpiralByPoint.getCenter().getY();

                double r = Math.sqrt(dx * dx + dy * dy);
                double rotAng = Math.atan2(dx, -dy);

                selectedSpiralByPoint.setRadius(Math.max(r, 0.1));
                selectedSpiralByPoint.setStartAngle(-rotAng);

                repaint();
            } else if (selectedSpiralByCenter != null) {
                RealPoint curPoint = sc.s2r(new ScreenPoint(e.getX(), e.getY()));
                selectedSpiralByCenter.setCenter(curPoint);

                repaint();
            } else {
                ScreenPoint curPoint = new ScreenPoint(e.getX(), e.getY());
                RealPoint p1 = sc.s2r(curPoint);
                RealPoint p2 = sc.s2r(prevPoint);
                RealPoint delta = p2.minus(p1);

                sc.moveCorner(delta);
                prevPoint = curPoint;

                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        QuadSpiral hoveredQuadSpiralByPoint = findQuadSpiralByPoint(sc, spirals, new ScreenPoint(e.getX(), e.getY()), 50);
        QuadSpiral hoveredQuadSpiralByCenter = findQuadSpiralByCenter(sc, spirals, new ScreenPoint(e.getX(), e.getY()), 80);

        if (hoveredQuadSpiralByPoint == null && selectedSpiralByPoint != null) {
            selectedSpiralByPoint.setColor(Color.BLACK);
            selectedSpiralByPoint = null;

            repaint();
            return;
        }
        if (hoveredQuadSpiralByCenter == null && selectedSpiralByCenter != null) {
            selectedSpiralByCenter.setColor(Color.BLACK);
            selectedSpiralByCenter = null;

            repaint();
            return;
        }

        if (hoveredQuadSpiralByPoint != null && selectedSpiralByPoint != hoveredQuadSpiralByPoint) {
            if (selectedSpiralByPoint != null)
                selectedSpiralByPoint.setColor(Color.BLACK);

            selectedSpiralByPoint = hoveredQuadSpiralByPoint;
            selectedSpiralByPoint.setColor(Color.BLUE);

            repaint();
        } else if (hoveredQuadSpiralByCenter != null && selectedSpiralByCenter != hoveredQuadSpiralByCenter) {
            if (selectedSpiralByCenter != null)
                selectedSpiralByCenter.setColor(Color.BLACK);

            selectedSpiralByCenter = hoveredQuadSpiralByCenter;
            selectedSpiralByCenter.setColor(Color.RED);

            repaint();
        }
    }

    private static final double SCALE_STEP = 0.03;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicks = e.getWheelRotation();
        double coef = 1 + SCALE_STEP*(clicks < 0 ? -1 : 1);
        double scale = 1;
        for(int i = Math.abs(clicks); i > 0; i--){
            scale *= coef;
        }
        sc.changeScale(scale);

        repaint();
    }
}
