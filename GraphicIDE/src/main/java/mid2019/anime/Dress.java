/*
 * Copyright by Marlin Eller 2019
 */
package mid2019.anime;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Marlin
 */
public class Dress{       // colors and shapes
  public static List costumes = new List();
  public static Col.List COLORS = new Col.List();
  public static Dress STATE = new Dress(); // for buttons
  public static Dress MARK = new Dress(false, 0,0,0,Mark.S*2, Mark.S*2, 1); // for marks
//
//  public static Dress BLANK = new Dress(true, 0,0,0,70,35,1); // for the screen
//  public static Dress TEST = new Dress(true, 3,2,2,60,60,0);
//  public static Dress RedDot = new Dress(true,4,4,0,20,20,0);
//  public static Dress OpenRedDot = new Dress(true,0,4,3,20,20,0);
//  public static Dress King = new Dress(true, 5,2,2,60,60,0);
//  public static Dress Client = new Dress(true, 7,7,2,60,60,0);
//  public static Dress Server = new Dress(true, 6,6,2,60,60,0);
  
  
  public int id=-1, c=2, b=1, r=2, w=70, h=35, s=1; // s==0 is Oval
  
  private Dress(){} // only for constructing the single Button dress  

  public Dress(boolean isCostume, int c, int b, int r, int w, int h, int s){
    this.c = c; this.b = b; this.r = r; this.w = w; this.h = h; this.s = s;
    if(isCostume){this.id = costumes.size(); costumes.add(this);}
  }
  public static Dress newButtonDress(){return new Dress();}
  
  public void show(Graphics g, Point loc, boolean drab, String text){
    Color K = drab ? COLORS.get(b).drab : COLORS.get(b).full;
    g.setColor(K); fill(g, loc.x - w/2, loc.y - h/2, w, h);
    if(b!=c && r>0){
      K = drab ? COLORS.get(c).drab : COLORS.get(c).full;
      g.setColor(K); fill(g, loc.x -w/2+r, loc.y-h/2+r, w-2*r, h-2*r);
    }
    g.setColor(Color.BLACK);
    FontMetrics fm = g.getFontMetrics();
    g.drawString(text, loc.x-fm.stringWidth(text)/2 ,loc.y + fm.getAscent()/3 );
  }
  
  public boolean hit(int mx, int my, Point loc){
    return (mx>loc.x-w/2)&&(mx<loc.x+w/2)&&(my>loc.y-h/2)&&(my<loc.y+h/2);
  }
  
  private void fill(Graphics g, int x, int y, int w, int h){
    if(s==0){g.fillOval(x,y,w,h);}else{g.fillRect(x,y,w,h);}
  }
 

//  static{
//    COLORS.add(new Col(Color.WHITE));
//    COLORS.add(new Col(Color.BLACK));
//    COLORS.add(new Col(Color.ORANGE));
//    COLORS.add(new Col(Color.GREEN));
//    COLORS.add(new Col(Color.RED));
//    COLORS.add(new Col(new Color(0xcc,0x66,0xff))); // royal purple
//    COLORS.add(new Col(new Color(0x33,0x99,0xff))); // blue
//    COLORS.add(new Col(new Color(0x33,0x99,0x33))); // forest
//    COLORS.add(new Col(new Color(0xff,0xff,0x66))); // yellow
//  }
  
  //-----------------Dress LIST-----------------------
  public static class List extends ArrayList<Dress>{}
  
  //-----------------Col----------------------
  public static class Col{
    Color full, drab;
    
    public Col(Color c){set(c); COLORS.add(this);}
    private void set(Color c){full = c; drab = new Color(c.getRed(),c.getGreen(),c.getBlue(),30);}
    
    //----------------Col.List
    public static class List extends ArrayList<Col>{}
  }
}
