/*
 * Copyright by Marlin Eller 2020
 */
package newlang;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Marlin
 */
public class Loc implements Serializable{
  public double xx, yy;
  
  public static Random rnd = new Random();
  public static View curView = new View();
  
  public void rndLoc(){xx = rnd.nextDouble(); yy = rnd.nextDouble();}
  public int x(){return (int)Math.floor(xx*curView.scale + curView.xT);}
  public int y(){return (int)Math.floor(yy*curView.scale + curView.yT);}
  public void setXY(int x, int y){
    xx = (x-curView.xT)/curView.scale;
    yy = (y-curView.yT)/curView.scale;
  }
  
  //---------------------------VIEW----------------------------------
  public static class View implements I.Behavior, Serializable {
    public static View HOME = new View();
    public double scale = 1000.0;
    public double xT = 0.0, yT = 0.0;
    
    private View(){}
    public View(View v){this.set(v);}
  
    /* we use the coordinate transform G = L*S + T : Global = Local*Scale + Translation
     Global is the integer screen coords and Local is the double xx, yy
     The mouse wheel math is this: We want to change the old scale S into a new Scale
     M*S where we multiply like by 110% or by 90% we do this at some Global coord
     which we wish to remain fixed. so relation between oldScal newScale,new Trans,fixedG is:
       fG = L*nS + nT = L*oS + oT
       -> L = (fG-nT)/nS = (fG-oT)/oS 
       -> fG - nT = nS*(fG-oT)/oS
       -> nT = fG +M*(oT-fG)  : using nS = M*oS
     So the above tells you how to compute the newTranslation from
     the oldTran, the Multiplier, and the Global coord that you want to fix.
  */
   
    public void set(View v){scale = v.scale; xT = v.xT; yT = v.yT;}
    public void doit(){curView.set(this);}
    public void initializeScale(){scale = 1000.0; xT = 0.0; yT = 0.0;}
    public void incScale(int si, int x, int y){
      double m = 1.03; // we try 3% increments to scale
      if(si<0){m = 1/m;}
      scale *=m;
      xT = x + m*(xT-x);
      yT = y + m*(yT-y);
    }
  }
  
}
