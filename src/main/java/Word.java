import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public Color color;

  public String fontName;
  public int fontStyle;
  public int fontSize;
  public Font font;

  public String content;
  public Word parent;
  public Map<String, Word> children;

  public Point loc;

  public boolean v; //visibility

  public double pixWidth;
  public double pixHeight;

  public Word(Color color, String fName, int fStyle, int fSize, String content, Point loc, Word parent) {
    this.color = color;
    this.font = new Font(fName, fStyle, fSize);
    this.content = content;
    this.loc = loc;
    this.v = true;
    this.parent = parent;
    this.children = new HashMap<>();
  }

  private void show(Graphics g) {
    Rectangle2D r = g.getFontMetrics().getStringBounds(content, g);
    this.pixWidth = r.getWidth();
    this.pixHeight = r.getHeight();
    g.setColor(color);
    g.setFont(font);
    g.drawString(content, loc.x, loc.y);
  }

  public void moveTo(int x, int y){ loc.x = x; loc.y = y; }

  public List<List<String>> printTree() {
    Word cur = this;
    List<List<String>> res = new ArrayList<>();
    Deque<Word> queue = new ArrayDeque<>();
    queue.offer(cur);
    while (!queue.isEmpty()) {
      int curLvl = queue.size();
      List<String> list = new ArrayList<>();
      for (int i = 0; i < curLvl; i++) {
        Word n = queue.poll();
        if (n.content.length() > 0) {
          list.add(n.content);
        }
        queue.addAll(n.children.values());
      }
      if (list.size() > 0) {
        res.add(list);
      }
    }
    for (int i = 0; i < res.size(); i++) {
      System.out.print("current level: " + i + ", contents: ");
      for (int j = 0; j < res.get(i).size(); j++) {
        System.out.print(res.get(i).get(j) + ", ");
      }
      System.out.println();
    }
    return res;
  }


  //--------------------------WordList-------------------------
  public static class WList extends ArrayList<Word> {
    public void show(Graphics g){for(Word w:this){if(w.v){w.show(g);}}}
  }

}
