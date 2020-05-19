/*
 * Copyright by Marlin Eller 2019
 */
package mid2019.anime;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import mid2019.graphicsLib.Window;

/**
 *
 * @author Marlin
 */
public class Anime extends Window implements ActionListener{
  public static Font FONT = new Font("Helvetica", 0, 20);
  public static Timer TIMER;
  public static void main(String[] args){PANEL = new Anime();launch();}
  public static int time = 0;
  
  public Anime(){
    super("Anime", 1500, 1000);
    Save.readFile();
    TIMER = new Timer(30, this);
    TIMER.start();
//    Scene s = new Scene("V0a0d2M@SCREEN@1a100t1d1M3m");
//    s.addAfter(Scene.root);
  }

  public void paintComponent(Graphics g){
    g.setFont(FONT);
    Actor sa = Actor.selected;
    g.setColor(Color.WHITE); g.fillRect(0,0,2000,3000); // clear screen
    if(State.editing()){
    Mark.all.show(g);
      g.setColor(Color.BLACK);
      if(sa != null && sa.a != 0){g.drawLine(sa.m1.x, sa.m1.y, sa.m2.x, sa.m2.y);}
      g.drawString("Now is the time", 100,100);
    }
    //Dress.TEST.show(g,Mark.Center,false,"Judy");
    Actor.showAll(g);
    State.showAll(g);
    g.drawString("S:"+ Scene.sceneNumber +  " T:"+time, 100,300);
    //g.drawString(Scene.cur.tween, 100,340);
    g.drawString("Sel:"+((sa==null)?"null":""+sa.a), 100,340);
  }
  
  public void mousePressed(MouseEvent me){
    int x = me.getX(), y = me.getY();
    if(State.click(x,y)){repaint(); return;}
    if(State.editing()){
      if(Actor.click(x,y)){repaint(); return;}
      if(Mark.all.click(x,y)){repaint(); return;}
    }
  }
  
  public void mouseDragged(MouseEvent me){
    if(State.dragging){State.dragged.drag(me.getX(),me.getY()); repaint();}
  }
  
  public void mouseReleased(MouseEvent me){
   if(State.dragging){State.dragged.mouseReleased(); repaint();}   
  }

  public void keyTyped(KeyEvent ke) {
    Actor sa = Actor.selected;
    if(State.editing() & sa != null){
      char c = ke.getKeyChar();
      if((c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||"!,.?() \"'$+-=*#%^&".indexOf(c)>=0){
        sa.text += c;
      } else {
        sa.text = "";
      }
      repaint();    
    }
  }
  
  
  @Override
  public void actionPerformed(ActionEvent ae){
    time++;
    if(Actor.moveAll(time)){Scene.next(); time = 0;};
    repaint();
  }
}
