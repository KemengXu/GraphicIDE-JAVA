/*
 * Copyright by Marlin Eller 2019
 */
package mid2019.anime;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Marlin
 */
public class Actor{
  public static List all = new List();  // all actors
  public static ZList zList = new ZList();  // list of layers
  static {               // create a lot so you don't need to save the actors
    for(int i = 0; i < 15; i ++){
      (new Actor(0)).v = false;
      (new Actor(1)).v = false;
      (new Actor(2)).v = false;
    }
  }
  //public static Actor SCREEN = all.get(0); //new Actor(0);  // first actor in layer zero
  //static{SCREEN.text = "SCREEN"; }
  // public static Actor one = fetch(1);   // an actor in layer one
  public static Actor selected = null;
  
  public int a, T=50, z;
  
  public Mark m1=Mark.center(), m2=Mark.center();
  public Dress d = Dress.costumes.get(1);
  public boolean v = true;
  public Point loc = new Point();
  public String text="Who?";
  
  public Actor(int z){zList.safeGet(z).add(this); a = all.size(); all.add(this); this.z = z;}


  public boolean setLoc(int t){
    if(!v){return true;} // if you are not visible you are stopped.
    if(T==0){loc.x = m2.x; loc.y = m2.y; return true;}
    if(t>T){t = T;}
    loc.x = (m2.x*t + m1.x*(T-t))/T;
    loc.y = (m2.y*t + m1.y*(T-t))/T;
    return t==T;
  }
  
  public boolean hit(int x, int y){
    return d.hit(x,y,loc) && z==State.z();
  }
  public void show(Graphics g){ 
    setLoc(Anime.time);
    if(State.editing()){
      d.show(g, loc, z!=State.z(), text); // non-z are drab
      if(Actor.selected == this){
        g.setColor(Color.RED); g.drawOval(loc.x-10, loc.y-10, 20, 20);
      }
    }else{
      d.show(g, loc, false, text);
    }
    
  }
  
  public static Actor fetch(int z){return zList.safeGet(z).fetch(z);}
  public static boolean moveAll(int t){return all.move(t);}
  public static boolean click(int x, int y){
    if(!State.editing()){return false;}
    return zList.safeGet(State.z()).click(x,y);
  }
  
  public static void hideAll(){for(Actor a:all){a.v = false;}}
  public static void showAll(Graphics g){zList.show(g);}

  //---------------LIST----------------------------
  public static class List extends ArrayList<Actor>{
    public void show(Graphics g){for(Actor a:this){if(a.v){a.show(g);}}}  
    public boolean move(int t){
      boolean res = true; for(Actor a:this){if(!a.setLoc(t)){res = false;}}return res;
    }
    public boolean click(int x, int y){
      for(Actor a : this){
        if(a.v && a.d.hit(x,y,a.loc)){selected = a; return true;}
      }
      //selected = null; // don't do this here. need selected for click on Mark
      return false;
    }
    public Actor fetch(int z){
      for(Actor a : this){if(!a.v){a.v = true; a.d = Dress.costumes.get(1); return a;}}
      return new Actor(z);
    }
  }
  
  //-------------------ZLIST--------------------
  public static class ZList extends ArrayList<List>{
    public List safeGet(int z){while(z>=this.size()){this.add(new List());}; return get(z);}
    public void show(Graphics g){for(List list:this){list.show(g);}}
  }
  
}
