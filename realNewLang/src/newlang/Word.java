/*
 * Copyright by Marlin Eller 2020
 */
package newlang;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Marlin
 */
public class Word implements I.Behavior, Serializable{ 
  public String str = "Dude";
  public Loc loc = new Loc();
  public I.Behavior behavior = null;
  public Color color = Color.BLACK;
  public Font font = new Font("Arial", Font.PLAIN, 30);
  public FontMetrics fm;
  public int w = 0;
  
  public Word(String s){
    str = s; loc.rndLoc();
    behavior = new RndColor();
  }
  
  public void setXY(int x, int y){loc.setXY(x,y);}
  public void doit(){
    if(behavior != null){behavior.doit();}
  }
  
  public void show(Graphics g){
    g.setColor(color);
    g.setFont(font);
    fm = g.getFontMetrics();
    int x = loc.x(), y = loc.y();
    g.drawString(str, loc.x(), loc.y());
    boxText(g, x, y);
  }
  public void boxText(Graphics g, int x, int y){
    if(w == 0){w = fm.stringWidth(str);}
    g.drawRect(x, y-fm.getAscent(), w, fm.getHeight());
  }
  
  public boolean hit(int x, int y){
    int t = loc.x();
    if(x<t || x >t+w){return false;}
    t = loc.y();
    return y>=t-fm.getAscent() && y<=t+fm.getDescent();
  }

 
  
  //----------------------------LIST--------------------------------
  public static class List extends ArrayList<Word> implements Serializable{
    public void show(Graphics g){for(Word w : this){w.show(g);}}
    public Word hit(int x, int y){
      Word res = null; for(Word w : this){if(w.hit(x,y)){res = w;}} return res;
    }
  }
  
  //---------------------RND COLOR-------------------
  public class RndColor implements I.Behavior, Serializable{
    public void doit(){
      Random r = Loc.rnd;
      Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
      Word.this.color = c;
    }
  }

  
public static final String[] javaKeywords = {"abstract", "continue", "for", 
  "new", "switch", "assert", "default", "goto", "package", "synchronized", 
  "boolean", "do", "if", "private", "this", "break", "double", "implements", 
  "protected", "throw", "byte", "else", "import", "public", "throws", "case", 
  "enum", "instanceof", "return", "transient", "catch", "extends", "int", 
  "short", "try", "char", "final", "interface", "static", "void", "class", 
  "finally", "long", "strictfp", "volatile", "const", "float", "native", 
  "super", "while"};
}
