package mid2019.graphicIDE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;

import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mid2019.graphicIDE.Word.WList;
import mid2019.graphicsLib.G;

public class GraphicIDE extends Window {
  public static final int mainWindowWidth = 1300;
  public static final int mainWindowHeight = 900;

  public static WList wList = new WList();
  private boolean dragger;
  private double zoomFactor = 1;
  private double prevZoomFactor = 1;
  private double lastUpdatedZF = 1;
  private boolean zoomer;
  private boolean released;
  private double xOffset = 0;
  private double yOffset = 0;
  private int xDiff;
  private int yDiff;
  private Point startPoint;
  //  static {
//    wList.add(new Word(new Color(200, 0, 0), "Helvetica",0, 20, "Dude",  new Point(200, 200)));
//    wList.add(new Word(new Color(0, 200, 0), "Helvetica", 0, 30, "Hi there", new Point(400, 300)));
//  }

  public static Map<String, List<String>> getLayers(String fName) {
    Parser.displayIt(new File(fName), fName.length());
    return Parser.layers;
  }
  public static Map<String, List<String>> layers = getLayers( "E:\\NEU\\courses\\MidJava\\GraphicIDE\\src\\main");
  //---------------------------------------------------------
  public static Word sW;
  public static List<String> currPath = new ArrayList();
  static {
    int i = 3;
    for(String k: layers.keySet()){
      if (k.length() > 0 && !k.substring(1).contains("\\")){
        currPath.add(k);
        wList.add(new Word(Color.BLACK, "Helvetica", 0, 20, k.substring(1), new Point(i*100,(i++)*100), null));
      }
    }
  }
  //---------------------------------------------------------



//  public static Word root = getTree(layers), sW = root;
//  static {
//    //List<List<String>> levels = root.printTree();
//    int i = 0;
//    for (Word word : sW.children.values()) {
//      // System.out.println("content: " + word.content);
//      word.loc.y += 50 * i;
//      i++;
//      wList.add(word);
//    }
//  }
//
//  public static Map<String, List<String>> getLayers(String fName) {
//    Parser.displayIt(new File(fName), fName.length());
//    return Parser.layers;
//  }
//  public static Word getTree(Map<String, List<String>> layers) {
//    Word root = new Word(Color.BLACK, "Helvetica", 0, 20, "", new Point(0,0), null), cur;
//    for (String key : layers.keySet()) {
//      if (key == null || key.length() == 0) {
//        continue;
//      }
//      cur = root;
//      System.out.println(key);
//      String[] lvls = key.split("\\\\");
//      for (int i = 0; i < lvls.length; i++) {
//        Map<String, Word> map = cur.children;
//        if (!map.containsKey(lvls[i])) {
//          map.put(lvls[i], new Word(Color.BLACK, "Helvetica", 0, 20, lvls[i], new Point(200 + 50 * i, 200), cur));
//        }
//        cur = cur.children.get(lvls[i]);
//      }
//    }
//    return root.children.get("");
//  }
  public GraphicIDE() {
    super("GraphicIDE", mainWindowWidth, mainWindowHeight);
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
//      System.out.println(MouseInfo.getPointerInfo().getLocation().getY() + " " + getLocationOnScreen().getY());
      double zoomDiv = zoomFactor / prevZoomFactor;

      xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;   // xRel + div*(xOffset - xRel)
      yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;
      at.translate(xOffset, yOffset);
      at.scale(zoomFactor, zoomFactor);
      prevZoomFactor = zoomFactor;
      g2.transform(at);
      zoomer = false;
      updateWList();
    }
    wList.show(g);
  }

  private void updateWList() {
    if(zoomFactor > lastUpdatedZF + 0.5){
      lastUpdatedZF = zoomFactor;
      int ind = 2;
      int length = wList.size();
      for (int i = 0; i < length; i++){
        System.out.println(wList);
        System.out.println(currPath);
        Word word = wList.get(0);
        wList.remove(word);
        String key = currPath.get(0);
        currPath.remove(key);
        if(!layers.keySet().contains(key)){continue;}
        for (String s: layers.get(key)){
          wList.add(new Word(Color.BLACK, "Helvetica", 0, 20, s, new Point(word.loc.x,word.loc.y + ind), null));
          currPath.add(key + "\\" + s);
          ind += 15;
        }
      }
    }
  }

  public void mousePressed(MouseEvent me){
    if(xOffset == 0 && yOffset == 0) {
      for (int i = 0; i < wList.size(); i++) {
        if (around(me.getPoint(), wList.get(i))) {
          sW = wList.get(i);
        }
      }
    }
    released = false;
    startPoint = MouseInfo.getPointerInfo().getLocation();
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

public void mouseWheelMoved(MouseWheelEvent e) {
  zoomer = true;
  if (e.getWheelRotation() < 0) { //Zoom in
    zoomFactor *= 1.1;
    if(sW!=null) {
      for (Word word : sW.children.values()) {
        wList.add(word);
      }
    }
  } else { //Zoom out
    zoomFactor /= 1.1;
//    if(sW!=null) {
//      if (sW.parent != null) {
//        sW = sW.parent;
//      }
//      if (sW.parent != null) {
//        sW = sW.parent;
//      }
//      for (Word word : sW.children.values()) {
//        wList.add(word);
//      }
////      sW = wList.get(0);
//    }
  }
  repaint();
}

  public void mouseClicked(MouseEvent event)
  {
    if (event.getClickCount() == 2) {
      AffineTransform at = new AffineTransform();
      xOffset = 0;
      yOffset = 0;
      zoomFactor = 1;
      at.translate(xOffset, yOffset);
      at.scale(zoomFactor, zoomFactor);
      prevZoomFactor = zoomFactor;
      zoomer = false;
      repaint();
    }
  }

  public static void main(String args[]){
    PANEL = new GraphicIDE();launch();
  }

}
