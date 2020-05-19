import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class Screen extends Window {

  public static final int mainWindowWidth = 1300;
  public static final int mainWindowHeight = 900;

  public static Word.WList wList = new Word.WList();
  static {
    wList.add(new Word(new Color(200, 0, 0), "Helvetica",0, 20, "Dude",  new Point(200, 200)));
    wList.add(new Word(new Color(0, 200, 0), "Helvetica", 0, 30, "Hi there", new Point(400, 300)));
  }
  public static Word sW;  // selected word

  public static void fillBackground(Graphics g, Color c) {
    g.setColor(c);
    g.fillRect(0,0,5000,5000);
  }

  public Screen() {
    super("Screen", mainWindowWidth, mainWindowHeight);
  }

  public void paintComponent(Graphics g) {
    Screen.fillBackground(g, Color.WHITE);
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
      sW.loc = new Point(Math.abs((int)(me.getX() - sW.pixWidth / 2)), Math.abs((int)(me.getY() + sW.pixHeight / 2)));
    }
    repaint();
  }

  public void mouseReleased(MouseEvent me){
    sW = null;
  }

}
