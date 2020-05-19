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
public class Mark extends Point{    // x, y location
  public static final int S = 8; // size of line to draw when showing
  public static List all = new List();
  public static Mark trash() { return all.get(0); }
  public static Mark end() {return all.get(1);}
  public static Mark center() {return all.get(2);}
  
  public int id;
  public boolean v = true;
  public String tween;
  
  public Mark(int x, int y){this.x = x; this.y = y; id = all.size(); all.add(this);}
  
  public boolean hit(int x, int y){return Dress.MARK.hit(x, y, this);}
  public boolean click(int x, int y){
    Actor sa = Actor.selected; boolean saGood = sa != null && sa.a != 0;
    if(saGood && id==0){sa.v = false; Actor.selected = null; return true;}
    int k = State.marks.k;
    switch(k){
      case 0: if(saGood){sa.m1 = this;} break; // set SRC of selected actor to this mark
      case 1: if(saGood){sa.m2 = this;} break; // set DEST of selected actor to this mark
      case 2: this.v = false; break; // hide this mark
      case 3: 
        Actor a = Actor.fetch(State.z()); a.m1 = this; a.m2 = this; // add actor
        if(saGood){a.d = sa.d;}
    }
    return true;
  }
  
  public void show(Graphics g){
    if(id==0){ //trash}
      g.setColor(Color.DARK_GRAY);
      g.drawLine(x-S, y+S, x+S, y+S);
      g.drawLine(x-S-3, y-S, x-S, y+S);
      g.drawLine(x+S+3, y-S, x+S, y+S);
    }else{
      g.setColor(Color.LIGHT_GRAY);
      g.drawLine(x-S, y, x+S, y);
      g.drawLine(x, y-S, x, y+S);
    }
  }
  public static void fetch(int x, int y){
    for(Mark m : all){if(!m.v){m.v = true; m.x = x; m.y = y; return;}}
    new Mark(x,y);
  }

  public static class List extends ArrayList<Mark>{
    public void show(Graphics g){for(Mark m:this){if(m.v){m.show(g);}}}
    public boolean click(int x, int y){
      if(State.editing()){
        for(Mark m : this){if(m.v && m.hit(x,y)){return m.click(x,y);}}
        if(State.marks.k == 2){
          fetch(x,y);                // creating a mark ...
        } else {
          Actor.selected = null;     // or just clicking on blank screen
        }
        return true;
      }
      return false;     
    }
  }
}
