package mid2019.graphicIDE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import mid2019.anime.Actor;
import mid2019.graphicsLib.G.XY;

public class Word {
  public static Color[] colors = {};
  public String text;
  public Color color;
  public Point loc;
  public Font font;
  public boolean v; //visibility

  public Word(String text, Color color, Point loc, int fStyle, int fSize) {
    this.v = true;
    this.text = text;
    this.color = color;
    this.loc = loc;
    this.font = new Font("Helvetica", fStyle, fSize);
  }
  private void show(Graphics g) {
    g.setColor(color);
    g.setFont(font);
    g.drawString(text, loc.x, loc.y);
  }

  public void moveTo(int x, int y){ loc.x = x; loc.y = y; }

  public static class WList extends ArrayList<Word>{
    public void show(Graphics g){for(Word w:this){if(w.v){w.show(g);}}}
  }

}
