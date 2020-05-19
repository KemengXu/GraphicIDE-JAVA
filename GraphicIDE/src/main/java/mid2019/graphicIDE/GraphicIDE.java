package mid2019.graphicIDE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import mid2019.graphicIDE.Word.WList;
import mid2019.graphicsLib.G;
import mid2019.graphicsLib.Window;

public class GraphicIDE extends Window {
  public static WList wList = new WList();
  static {
    wList.add(new Word("Dude", new Color(200, 0, 0), new Point(200, 200), 0, 20));
    wList.add(new Word("Hi there", new Color(0, 200, 0), new Point(400, 300), 0, 30));
  }
  public static Word sW;  // selected word
  public GraphicIDE() {
    super("GraphicIDE", 1300, 900);
  }
  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.white);
    wList.show(g);
  }

  public void mousePressed(MouseEvent me){

  }
  private boolean around(Point a, Point b){
    return Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2) < 300;
  }

  public void mouseDragged(MouseEvent me){
    for(int i = 0; i < wList.size(); i++){
      if(around(me.getPoint(), wList.get(i).loc)){
        sW = wList.get(i);
        System.out.println(sW.loc.x);
      }
    }
    sW.loc = me.getPoint();
    repaint();
  }

  public void mouseReleased(MouseEvent me){

  }
  public static void main(String args[]){
    PANEL = new GraphicIDE();launch();
  }

}
