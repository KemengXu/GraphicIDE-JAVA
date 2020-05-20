package mid2019.graphicIDE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mid2019.anime.Actor;
import mid2019.graphicsLib.G.XY;

public class Word {
  public static Map<String, int[]> colors = new HashMap<>();
  static {
    colors.put("background", new int[]{46, 46, 46});
    colors.put("commonGrey", new int[]{121, 121, 121});
    colors.put("white", new int[]{214, 214, 214});
    colors.put("yellow", new int[]{229, 181, 103});
    colors.put("green", new int[]{180, 210, 115});
    colors.put("orange", new int[]{232, 125, 62});
    colors.put("purple", new int[]{158, 134, 200});
    colors.put("pink", new int[]{176, 82, 121});
    colors.put("blue", new int[]{108, 153, 187});
    colors.put("lightBackground", new int[]{39, 40, 34});
    colors.put("lightPink", new int[]{249, 38, 114});
    colors.put("lightBlue", new int[]{102, 217, 239});
    colors.put("lightGreen", new int[]{166, 226, 46});
    colors.put("lightOrange", new int[]{253, 151, 31});
    colors.put("lightYellow", new int[]{230, 219, 116});
    colors.put("lightPurple", new int[]{174, 129, 255});
  }
  public String text;
  public Color color;
  public Point loc;
  public Font font;
  public boolean v; //visibility
  public double pixWidth;
  public double pixHeight;

  public Word(String text, Color color, Point loc, String fName, int fStyle, int fSize) {
    this.v = true;
    this.text = text;
    this.color = color;
    this.loc = loc;
    this.font = new Font(fName, fStyle, fSize);
  }
  private void show(Graphics g) {
    Rectangle2D r = g.getFontMetrics().getStringBounds(text, g);
    pixWidth = r.getWidth();
    pixHeight = r.getHeight();
    g.setColor(color);
    g.setFont(font);
    g.drawString(text, loc.x, loc.y);
  }

  public void moveTo(int x, int y){ loc.x = x; loc.y = y; }

  public static class WList extends ArrayList<Word>{
    public void show(Graphics g){for(Word w:this){if(w.v){w.show(g);}}}
  }

}
