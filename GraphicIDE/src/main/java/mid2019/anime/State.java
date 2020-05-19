/*
 * Copyright by Marlin Eller 2019
 */
package mid2019.anime;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Marlin
 */
public class State{
  public static List all = new List();
  public static int xx = (10 + 70/2), yy = 10+ 35/2, W = (10 + 70);
  public static boolean dragging = false;
  public static State dragged;

  public Point loc = new Point();
  public int k;
  public boolean always;
  public String[] names; 
  
  public State(String str, boolean always){
    this.always = always;
    names = str.split("/");
    loc.x = xx; loc.y = yy; xx += W;
    all.add(this);
  }
  
  public void show(Graphics g){
    if(always || editing()){Dress.STATE.show(g, loc, false, names[k]);}
  }
  
  public void incState(){k++; if(k>=names.length){k=0;}}
  
  void doit(){incState();} // default button behavior is to cycle state
  
  public static void showAll(Graphics g){all.show(g);}
  
  public static boolean click(int x, int y){return all.click(x,y);}
  
  public static boolean editing(){return edit.k == 1;}
  public static int z(){return zLev.k;}
  
  private static void startDragging(State s){
    dragging = true; dragged = s;
    if(dragged == time){time.names[0] = "" + Actor.selected.T;}
  }
  
  void drag(int mx,int my){
    if(dragged == time){
      int t = Math.abs(this.loc.x - mx)/5;
      time.names[0] = "T:" + t;
      Actor.selected.T = t; 
    }
    if(dragged == dress){
      int t = (Math.abs(this.loc.x - mx)/10)%Dress.costumes.size();
      Actor.selected.d = Dress.costumes.get(t);
    }
    if(dragged == zip){
      int t = (Math.abs(this.loc.x - mx)/15)%Scene.all.size();
      zip.names[0] = "S:"+t;
      Scene.cur = Scene.all.get(t);
      Scene.sceneNumber = t;
      Scene.start();
    }
  }
  void mouseReleased(){
    dragging = false;
    if(dragged == time){ time.names[0] = "TIME"; }
    if(dragged == zip){zip.names[0] = "ZIP";}
    dragged = null;}
  
  //-------------BUTTONS-----------------------------
  public static State edit = new State("EDIT/PLAY", true){
    void doit(){
      incState(); 
      if(k==1){
        Anime.TIMER.stop(); Anime.time = 0; Scene.start();
        if(Actor.selected != null && !Actor.selected.v){Actor.selected = null;}
      }else{
        //Scene.saveFile();
        //Mark.List.saveMark();
        Scene.saveEdits(); Anime.TIMER.start();
      }
    }
  };
  public static State zLev = new State("Z0/Z1/Z2", false);
  public static State marks = new State("SRC/DEST/MARK/A+", false);
  public static State time = new State("TIME", false){
    void doit(){
      Actor sa = Actor.selected;
      if(sa != null && sa.a != 0){time.names[0] = ""+sa.T; startDragging(time);}  
    }
  };
  public static State dress = new State("DRESS", false){
    void doit(){
      Actor sa = Actor.selected;
      if(sa != null && sa.a != 0){startDragging(dress);}  
    }
  };
   public static State zip = new State("ZIP", false){
    void doit(){startDragging(zip);}
  };
  
  //------------------list--------------------------
  public static class List extends ArrayList<State>{
    public void show(Graphics g){for(State s:this){s.show(g);}}
    public boolean click(int x, int y){
      for(State s : this){
        if(Dress.STATE.hit(x, y, s.loc)){s.doit(); return true;}
      }
      return false;
    }
  }
}
