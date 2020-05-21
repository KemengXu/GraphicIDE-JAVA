import com.sun.jmx.remote.internal.ArrayQueue;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Screen extends Window {

  public static final int mainWindowWidth = 1300;
  public static final int mainWindowHeight = 900;

  public static Word.WList wList = new Word.WList();
//  static {
//    wList.add(new Word(new Color(200, 0, 0), "Helvetica",0, 20, "Dude",  new Point(200, 200)));
//    wList.add(new Word(new Color(0, 200, 0), "Helvetica", 0, 30, "Hi there", new Point(400, 300)));
//  }


  public static Map<String, List<String>> layers = getLayers("/Users/jingyiwang/Desktop/Student_repo_Jingyi_Wang/Mid2019/src");
  public static Word root = getTree(layers), sW = root;
  static {
    //List<List<String>> levels = root.printTree();
    int i = 0;
    for (Word word : sW.children.values()) {
      // System.out.println("content: " + word.content);
      word.loc.y += 50 * i;
      i++;
      wList.add(word);
    }
  }

  public static Map<String, List<String>> getLayers(String fName) {
    Parser.displayIt(new File(fName), fName.length());
    return Parser.layers;
  }
  public static Word getTree(Map<String, List<String>> layers) {
    Word root = new Word(Color.BLACK, "Helvetica", 0, 20, "", new Point(0,0), null), cur;
    for (String key : layers.keySet()) {
      if (key == null || key.length() == 0) {
        continue;
      }
      cur = root;
      String[] lvls = key.split(File.separator);
      for (int i = 0; i < lvls.length; i++) {
        Map<String, Word> map = cur.children;
        if (!map.containsKey(lvls[i])) {
          map.put(lvls[i], new Word(Color.BLACK, "Helvetica", 0, 20, lvls[i], new Point(200 + 50 * i, 200), cur));
        }
        cur = cur.children.get(lvls[i]);
      }
    }
    return root.children.get("");
  }


//  public static void main(String[] args) {
//    Map<String, List<String>> layers = getLayers("/Users/jingyiwang/Desktop/Student_repo_Jingyi_Wang/Mid2019/src");
//    TreeNode root = getTree(layers);
//    List<List<String>> levels = root.printTree();
//    for (int i = 0; i < levels.get(selectedLayer).size(); i++) {
//      Word curWord = new Word(Color.BLACK, "Helvetica", 0, 10, levels.get(selectedLayer).get(i), new Point(200, 200));
//      wList.add(curWord);
//    }
//
//  }


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

  public static void fillBackground(Graphics g, Color c) {
    g.setColor(c);
    g.fillRect(0,0,5000,5000);
  }

  public Screen() {
    super("Screen", mainWindowWidth, mainWindowHeight);
  }

  public void paintComponent(Graphics g) {
    Screen.fillBackground(g, Color.WHITE);
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
    if(xOffset == 0 && yOffset == 0) {
      for(int i = 0; i < wList.size(); i++){
        if(around(me.getPoint(), wList.get(i))){
          sW = wList.get(i);
        }
      }
    }
    if (sW == null){
      released = false;
      startPoint = MouseInfo.getPointerInfo().getLocation();
    }
  }
  private boolean around(Point a, Word w){
    return Math.pow(a.x - w.pixWidth / 2 - w.loc.x, 2) + Math.pow(a.y + w.pixHeight / 2 - w.loc.y, 2) < Math.pow(Math.max(w.pixHeight, w.pixWidth), 2);
  }

  public void mouseDragged(MouseEvent me){
    if (sW != null) {
      sW.loc = new Point(Math.abs((int)(me.getX() - sW.pixWidth / 2)), Math.abs((int)(me.getY() + sW.pixHeight / 2)));
    } else {
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
      wList.clear();
      for (Word word : sW.children.values()) {
        wList.add(word);
      }
    } else { //Zoom out
      zoomFactor /= 1.1;
      wList.clear();
      if (sW.parent != null) {
        sW = sW.parent;
      }
      if (sW.parent != null) {
        sW = sW.parent;
      }
      for (Word word : sW.children.values()) {
        wList.add(word);
      }
    }
    repaint();
  }

  public void mouseClicked(MouseEvent event) {
    if (event.getClickCount() == 2) {
      xOffset = 0;
      yOffset = 0;
    }
  }

  //------------------------------TreeNode-----------------------
//  public static class TreeNode {
//    public String content;
//    public TreeNode parent;
//    public Map<String, TreeNode> children;
//
//    public TreeNode() {
//      this.content = "";
//      this.parent = null;
//      this.children = new HashMap<>();
//    }
//
//    public TreeNode(String content) {
//      this.content = content;
//      this.parent = null;
//      this.children = new HashMap<>();
//    }
//
//    public TreeNode(String content, TreeNode parent) {
//      this.content = content;
//      this.parent = parent;
//      this.children = new HashMap<>();
//    }
//
//    public List<List<String>> printTree() {
//      TreeNode cur = this;
//      List<List<String>> res = new ArrayList<>();
//      Deque<TreeNode> queue = new ArrayDeque<>();
//      queue.offer(cur);
//      while (!queue.isEmpty()) {
//        int curLvl = queue.size();
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < curLvl; i++) {
//          TreeNode n = queue.poll();
//          if (n.content.length() > 0) {
//            list.add(n.content);
//          }
//          queue.addAll(n.children.values());
//        }
//        if (list.size() > 0) {
//          res.add(list);
//        }
//      }
//      for (int i = 0; i < res.size(); i++) {
//        System.out.print("current level: " + i + ", contents: ");
//        for (int j = 0; j < res.get(i).size(); j++) {
//          System.out.print(res.get(i).get(j) + ", ");
//        }
//        System.out.println();
//      }
//      return res;
//    }
//  }


}
