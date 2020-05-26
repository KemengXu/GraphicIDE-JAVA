/*
 * Copyright by Marlin Eller 2020
 */
package newlang;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author Marlin
 */
public class SC implements Serializable{ // Structured Code - lines and blocks
  public static final int xOff = 5, yOff = 10, tabSize = 15, wGap = 5, lGap = 4; 
  public Word head;
  public Line.List lines = new Line.List();
  public int nM, yMargin; //Margin count and Y val used/modified by Line.layout
  public boolean isVis = true; 
  private Cursor cursor; //cursor for editing with !Del and \n and add
  
  public SC(){
    head = new Word("@"); 
    head.behavior = new I.Behavior(){
      public void doit(){
        SC.this.isVis = !SC.this.isVis;
      }
    };
    cursor = new Cursor(this);
  }
  
  public void add(Word w){
    cursor.validate();
    if(w.str.equals("\\n")){
      cursor.newLine(); 
      return;}
    if(w.str.equals("!del")){cursor.delete(); return;}
    cursor.add(new Word(w));
  }
  
  public int xMargin(){return head.loc.x() + xOff +nM*tabSize;}
  
  public void show(Graphics g){
    if(isVis){g.setColor(Color.WHITE);g.fillRect(head.loc.x(), head.loc.y(), 20, 20);}
    head.show(g);
    if(!isVis){return;}
    nM = 0; yMargin = head.loc.y() + yOff;
    lines.layout(this, g); // figure out y value for line and upDate yMargin for next.
    lines.show(g);
  }
  
  public Word wordHit(int x, int y){
    if(head.hit(x,y)){
      return head;
    }
    return isVis ? lines.wordHit(x, y) : null;
  }
  
  //----------------------------CURSOR-------------------------------
  private static class Cursor{
    private SC sc;
    private int iLine = -1, iWord = -1;
    private Cursor(SC sc){this.sc = sc;}
    
    private void validate(){
      int nLine = sc.lines.size();
      if(iLine<0 || iLine>nLine){iLine = (nLine==0 ? 0:nLine-1);}
      if(iLine == nLine){iWord = 0; return;}
      int nWord = sc.lines.get(iLine).words.size();
      if(iWord <0 || iWord > nWord){iWord = nWord;}
    } 
   
    private void newLine(){
      int nLine = sc.lines.size();
      if(iLine==nLine){sc.lines.add(new Line()); iLine++; return;}
      // otherwise we may need to split an existing line
      Line oldLine = sc.lines.get(iLine);
      Line newLine = new Line();
      iLine++;// after the newLine char we end up on the new line
      sc.lines.add(iLine, newLine);
      while(iWord < oldLine.words.size()){
        newLine.words.add(oldLine.words.get(iWord));
        oldLine.words.remove(iWord);
      }
      iWord = 0; // and we point right after the newLine
    }
    
    private void delete(){
      int nLine = sc.lines.size();
      if(nLine == 0){return;} // nothing to delete
      if(iLine == nLine){  // nothing to delete but must move cursor
        iLine--; 
        iWord = sc.lines.get(iLine).words.size();
        return;
      }
      if(iWord != 0){iWord = 0; sc.lines.get(iLine).words.remove(iWord); return;}  
    // word was zero so we are deleting a new line and folding two lines together
      if(iLine == 0){return;} // can't delete initial newline
      Line a = sc.lines.get(iLine);
      iLine--;
      Line prev = sc.lines.get(iLine);
      sc.lines.remove(iLine);
      for(int i = 0; i<a.words.size(); i++){
        prev.words.add(a.words.get(i));
      }
      iWord = prev.words.size();    
    }
       
    private void add(Word w){
      if(iLine == sc.lines.size()){sc.lines.add(new Line());}
      Line line = sc.lines.get(iLine);
      line.words.add(iWord, w);
      iWord++;
    }
  }
  
  public static class Line implements Serializable{
    public static final String PLUS = "+", MINUS = "-";
    public static final Font LINE_FONT = new Font("Arial", Font.PLAIN, 20);

    public Word head = new Word("-"); // it can toggle to -
    public Word.List words = new Word.List();
    public int asc = 0, desc = 0, width = 0;
    public Block block = null; // line can end with a block

    public Line(){head.behavior = new Outline(this);}

    public void add(Word w){
      Word newW = new Word(w);
      //newW.sty = new Word.Sty(Color.ORANGE, LINE_FONT);
      words.add(newW);
    }
    
    public void layout(SC sc, Graphics g){
      int x = sc.xMargin(), y = sc.yMargin;
      head.setXY(x, y); x+=head.w + wGap;
      setAscDesc(); // accumulate across all words in line
      if(block != null){block.layout(sc,g);}
      y += asc + lGap; sc.yMargin = y+desc; // once we know max asc & desc, we can set y
      //if(head.str.length()>1){g.drawString("-",x,y);  x += head.fm.stringWidth("-");}
      for(Word word : words){word.setXY(x, y); x += wGap + word.w;
      }
    }
    
    private void setAscDesc(){
      // this fails because fm is not valid for a line head in an SC that's
      // never been rendered.
      //asc = head.fm.getAscent(); desc = head.fm.getDescent();
      asc =0; desc =0;
      if(head.fm != null){
        asc = head.fm.getAscent(); desc = head.fm.getDescent();
      }
      if(isVis()){
        for(Word word : words){
          if(word.fm.getAscent()>asc){asc = word.fm.getAscent();}
          if(word.fm.getDescent()>desc){desc = word.fm.getAscent();}
        }      
      }
    }
           
    
    public boolean isVis(){return head.str.startsWith(MINUS);}
    
    public void show(Graphics g){
      int x = head.loc.x(), y = head.loc.y();
      head.show(g);
      if(isVis()){words.show(g);}
    } 

    /*
    public void locateList(int x, int y){
      int xtraSpace = 5;
      for(int i = 0; i<words.size(); i++){
        Word w = words.get(i);
        w.setXY(x+xtraSpace, y);
        x += xtraSpace + w.w;
      }
    }
 */ 
    //-------------------------Line Head Behavior
    public static class Outline implements I.Behavior, Serializable {
      Line line;
      public Outline(Line line){this.line = line;}
      public void doit(){
        String s = line.head.str;
        line.head.str = ((s.startsWith(PLUS))? MINUS:PLUS)+ s.substring(1);
      }
    }
    
    //---------------------Line List-------------------
    public static class List extends ArrayList<Line> implements Serializable{
      public void layout(SC sc, Graphics g){for(Line line : this){line.layout(sc, g);}}
      public void show(Graphics g){for(Line line : this){line.show(g);}}
      public Word wordHit(int x, int y){
        for(Line z:this){
          Word w = z.head;
          if(w.hit(x, y)){return w;}
        }
        return null;
      }
    }
  }
  
  public static class Block implements Serializable{
    
    public void layout(SC sc, Graphics g){}
  }
  

}
