/*
 * Copyright by Marlin Eller 2019
 */
package mid2019.anime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Marlin
 */
public class Scene implements Serializable{
  public static String theEnd = "V0a50t1M@THE END@";
  public static Scene root = new Scene("V");
  public static Scene cur; //= root;
  public static int sceneNumber = 0;
  public static ArrayList<Scene> all = new ArrayList<>();
  
  public String tween = "";
  public Scene next = null;
  
  public Scene(String str){tween = str;}
  
  public static void next(){
    cur = (cur.next == null) ? root : cur.next; 
    sceneNumber = (cur == root)? 0 : sceneNumber + 1;
    start();
  }

  public static void createListFromAll() {
    root = all.get(0);
    Scene p = root;
    for (int i = 1; i < all.size(); i++) {
      Scene s = all.get(i);
      s.next = null;
      p.next = s;
      p = s;
    }
    cur = root;  // start from beginning
    sceneNumber = 0;
  }

  public void addAfter(Scene s){next = s.next; s.next = this; loadScenes();}

  public static void start(){ // start scene by copying values from tween into actors
    String str = cur.tween; int num = 0; Actor a=null; 
    boolean inText = false; String text = ""; 
    for(int i=0; i<str.length(); i++){
      char c = str.charAt(i);
      if(inText){if(c=='@'){inText=false; a.text = text;}else{text += c;} continue;}
      if(c>='0'&&c<='9'){num = 10*num + c - '0'; continue;}
      switch(c){
        case '@': inText = true; text = ""; break;
        case 'a': a = Actor.all.get(num); a.v = true; break;
        case 'M': a.m1 = Mark.all.get(num); a.m2 = Mark.all.get(num); break;
        case 'm': a.m2 = Mark.all.get(num); break;
        case 'd': a.d = Dress.costumes.get(num); break;
        case 't': a.T = num; break;
        case 'V': Actor.hideAll(); break;
          
        default: System.out.println("WTF bad char in tween " + c);
      }
      num = 0;
    } 
  }
  
  public static void saveEdits(){ // copy values from actors into tween
    String str = "V";
    for(Actor a:Actor.all){
      if(!a.v){continue;} // skip the invisible
      str += ""+a.a+"a" + a.T+"t" + a.d.id +"d" + a.m1.id +"M" + a.m2.id +"m";
      str += "@"+a.text+"@";
    }
    cur.tween = str;
    if(cur.next == null){(new Scene(theEnd)).addAfter(cur);}
    Save.saveFile();
  }
  
  public static void loadScenes(){
    all.clear(); Scene s = root; Scene ps = root;
    while(s != null){all.add(s); ps = s; s = s.next;}
    // saveFile();
  }

}

