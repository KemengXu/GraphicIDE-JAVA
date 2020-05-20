package mid2019.graphicIDE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import mid2019.graphicIDE.Word.WList;
import mid2019.graphicsLib.G;
import mid2019.graphicsLib.Window;

public class GraphicIDE extends Window {
  public static final int mainWindowWidth = 1300;
  public static final int mainWindowHeight = 900;

  public static WList wList = new WList();
  static {
    wList.add(new Word("Dude", new Color(200, 0, 0), new Point(200, 200), "Helvetica", 0, 20));
    wList.add(new Word("Hi there", new Color(0, 200, 0), new Point(400, 300), "Helvetica", 0, 30));
  }
  public static Word sW;  // selected word
  public GraphicIDE() {
    super("GraphicIDE", mainWindowWidth, mainWindowHeight);
  }
  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.white);
    wList.show(g);
  }

  public void mousePressed(MouseEvent me){
    for(int i = 0; i < wList.size(); i++){
      if(around(me.getPoint(), wList.get(i))){
        sW = wList.get(i);
      }
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
    repaint();
  }

  public void mouseReleased(MouseEvent me){
    sW = null;
  }
  public static void main(String args[]){
    PANEL = new GraphicIDE();launch();
  }

}
