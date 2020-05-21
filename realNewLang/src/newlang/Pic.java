/* Sizable images
 * Copyright by Marlin Eller 2020
 */
package newlang;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author Marlin
 */
public class Pic{
  public static Image IMG; 
  static{
      try{
    File input = new File("pic.jpg");
    IMG = ImageIO.read(input);
    }
    catch(IOException ie)
    {
        System.out.println(ie.getMessage());
    }
  }
  public static Pic PIC = new Pic(IMG);
  
  public Image img;
  public Loc ul = new Loc(), dr = new Loc();
  
  public Pic(Image img){
    this.img = img;
    int w = img.getWidth(null), h = img.getHeight(null);
    w = w*1000/h; // rescale to fit 1000 height
    ul.setXY(0,0); dr.setXY(w, 1000);
  }
  
  public void show(Graphics g){
    int x = ul.x(), y = ul.y(), w = dr.x()-x, h = dr.y()-y;
    g.drawImage(img,x,y,w,h,null);
  }
  
}
