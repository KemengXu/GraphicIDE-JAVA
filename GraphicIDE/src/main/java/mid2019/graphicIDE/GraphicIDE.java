package mid2019.graphicIDE;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;

import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import mid2019.graphicIDE.Word.WList;
import mid2019.graphicsLib.G;

public class GraphicIDE extends Window {
  public static final int mainWindowWidth = 1300;
  public static final int mainWindowHeight = 900;

  public static WList wList = new WList();
  private boolean dragger;
  private double zoomFactor = 1;
  private double prevZoomFactor = 1;
  private boolean zoomer;
  private boolean released;
  private double xOffset = 0;
  private double yOffset = 0;
  private int xDiff;
  private int yDiff;
  private Point startPoint;
  static {
    wList.add(new Word("Dude", new Color(200, 0, 0), new Point(200, 200), "Helvetica", 0, 20));
    wList.add(new Word("Hi there", new Color(0, 200, 0), new Point(400, 300), "Helvetica", 0, 30));
  }
  public static Word sW;  // selected word
  public GraphicIDE() {
    super("GraphicIDE", mainWindowWidth, mainWindowHeight);
    addMouseWheelListener(this);
  }
  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.white);
    Graphics2D g2 = (Graphics2D) g;
    if (dragger) {
      AffineTransform at = new AffineTransform();
      at.translate(xOffset + xDiff, yOffset + yDiff);
      at.scale(zoomFactor, zoomFactor);
      g2.transform(at);

      if (released) {
        xOffset += xDiff;
        yOffset += yDiff;
        dragger = false;
      }

    }
    if (zoomer) {
      AffineTransform at = new AffineTransform();

      double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
      double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

      double zoomDiv = zoomFactor / prevZoomFactor;

      xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
      yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

      at.translate(xOffset, yOffset);
      at.scale(zoomFactor, zoomFactor);
      prevZoomFactor = zoomFactor;
      g2.transform(at);
      zoomer = false;
    }
    wList.show(g);
  }

  public void mousePressed(MouseEvent me){
    for(int i = 0; i < wList.size(); i++){
      if(around(me.getPoint(), wList.get(i))){
        sW = wList.get(i);
      }
    }
    if (sW == null){
      released = false;
      startPoint = MouseInfo.getPointerInfo().getLocation();
    }
  }
  private boolean around(Point a, Word w){
//    System.out.println(w.pixWidth + " " + w.pixHeight);
//    System.out.println(a.y + " " + w.loc.y);
    return Math.pow(a.x - w.pixWidth / 2 - w.loc.x, 2) + Math.pow(a.y + w.pixHeight / 2 - w.loc.y, 2) < Math.pow(Math.max(w.pixHeight, w.pixWidth), 2);
  }

  public void mouseDragged(MouseEvent me){
    if (sW != null) {
      sW.loc = new Point((int) (me.getX() - sW.pixWidth / 2), (int) (me.getY() + sW.pixHeight / 2));
    }
    else {
      Point curPoint = me.getLocationOnScreen();
      xDiff = curPoint.x - startPoint.x;
      yDiff = curPoint.y - startPoint.y;
      dragger = true;
    }
    repaint();
  }

  public void mouseReleased(MouseEvent me){
    sW = null;
    released = true;
    repaint();
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    zoomer = true;
    //Zoom in
    if (e.getWheelRotation() < 0) {
      zoomFactor *= 1.1;
      repaint();
    }
    //Zoom out
    if (e.getWheelRotation() > 0) {
      zoomFactor /= 1.1;
      repaint();
    }
  }

  public static void main(String args[]){
    PANEL = new GraphicIDE();launch();
  }

}
