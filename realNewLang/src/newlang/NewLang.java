/*
 * Copyright by Marlin Eller 2020
 */
package newlang;

import GraphicsLib.Window;
import static GraphicsLib.Window.launch;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Marlin
 */
public class NewLang extends Window implements MouseWheelListener{
  public static Color JmpColor = new Color(200,200,200);
  public static int curX = 0, curY = 0; // updated by mouse motion
  public static Word dragWord = null; 
  public static boolean dragging = false;
  public static int dragX, dragY, dragThresh = 50;
  public static Word.List words = new Word.List();
  public static KeyLogic keyLogic = new KeyLogic();
  
  public NewLang(){
    super("NewLang", 1650, 1000);
    fetchWords();  
  }
  
  public void loadKeywords(){
    for(int i = 0; i<Word.javaKeywords.length; i++){
      Word w = new Word(Word.javaKeywords[i]);
      words.add(w);
    }
  }

  public void mousePressed(MouseEvent me){
    int dragX = me.getX(), dragY = me.getY();
    dragWord = words.hit(dragX, dragY);
    if(dragWord == null && dragX <50 && dragY<50){
      saveAll(); System.out.println("Saved?");
    }
  }
  
  public void mouseReleased(MouseEvent me){
    Word w = words.hit(me.getX(), me.getY());
    if(dragWord != null && !dragging){dragWord.doit();}
    dragging = false;
    dragWord = null;
    repaint();
  }
  
  public void mouseDragged(MouseEvent me){
    int x = me.getX(), y = me.getY();
    if(dragWord != null){
      if(dragging || (Math.abs(x-dragX)+Math.abs(y-dragY) > dragThresh)){
        dragging = true;
        dragWord.setXY(x-dragWord.w/2, y);
        repaint();
      }
    }
  }
  
  public void mouseMoved(MouseEvent me){ // track mouse location
    curX = me.getX(); curY = me.getY();
  }
  
  public static Color CHILL = new Color(255,255,255,50);
  protected void paintComponent(Graphics g){
    Pic.PIC.show(g);
    g.setColor(CHILL); g.fillRect(0, 0, 3000, 3000);
    words.show(g);
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent mwe){
    //System.out.println("Moved" + mwe.getWheelRotation()+ " "+curX+" "+curY );
    Loc.curView.incScale(mwe.getWheelRotation(), curX, curY);
    repaint();
  }
  
  @Override
  public void keyPressed(KeyEvent ke){
    keyLogic.next(ke);
  }
  
  //-------------------Serialization----------------------------
  
  public void saveAll(){
    try{
      FileOutputStream fout = new FileOutputStream("Words.dat");
      ObjectOutputStream oout = new ObjectOutputStream(fout);
      oout.writeObject(words); 
      oout.close();
      fout.close();
    }catch(Exception e){ 
      e.printStackTrace();
    }
  }
  
  public void fetchWords(){
    try {
      FileInputStream fin = new FileInputStream("Words.dat");
      ObjectInputStream oin = new ObjectInputStream(fin);
      words = (Word.List) oin.readObject();
      oin.close();
      fin.close();
    }catch(Exception e) { 
      e.printStackTrace();
      loadKeywords();
    }
  }
  
  //------------------Key Logic---------------------
  public static class KeyLogic{
    public boolean oneLine = false;
    public String theLine = "";
    public char lineType = 'a';
    public static String lineTypes = "#";
    
    public void next(KeyEvent ke){
      char c = ke.getKeyChar();
      System.out.println("pressed: "+((int)c)+" "+ c);
      if(oneLine && c != '\n'){theLine += c; return;}
      if(oneLine && c == '\n'){closeLine();}
      if(lineTypes.indexOf(c)>=0){startLine(c);}
    }
    
    public void startLine(char c){
      System.out.println("start Line");
      lineType = c; theLine = ""; oneLine = true;
    }
    public void closeLine(){
      System.out.println("Close Line");
      switch(lineType){
        case '#': 
          Word w = new Word("#"+theLine); 
          w.loc.setXY(curX, curX); w.color = JmpColor;
          w.behavior = new Loc.View(Loc.curView);
          words.add(w);
          break;
        default: break;
      }
      oneLine = false;
      theLine = "";
    }
  }
  
  
  public static void main(String[] args){
    NewLang nl = new NewLang();
    PANEL = nl;
    PANEL.addMouseWheelListener(nl);
    launch();
  }
}
