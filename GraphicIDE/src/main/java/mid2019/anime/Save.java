/*
 * Copyright by Marlin Eller 2019
 */
package mid2019.anime;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 *
 * @author Marlin
 */
public class Save{
  public static void readFile(){
    try{
      FileInputStream fis = new FileInputStream("Anime.dat"); 
      Scanner s = new Scanner(fis);
      parseFile(s);
      fis.close();
    } catch(Exception e){
      System.out.println("problems reading Anime.dat");
      System.out.println(e);
    }
  }
  
  private static String str;
  private static String[] sBuf;
  private static int k(int i){return Integer.parseInt(sBuf[i]);}

  public static void parseFile(Scanner fs)throws Exception{
    Scene.all.add(Scene.root);
    while(fs.hasNext()){
      str = fs.nextLine();
      if(str.startsWith(" ") || str.length() < 4){continue;} // skip blank lines
      char c = str.charAt(0);
      sBuf = str.split(" +");
      switch(c){
        case 'M': parseMark(); break;
        case 'C': parseColor(); break;
        case 'D': parseDress(); break;
        case 'V': parseScene(); break;
        default: throw new Exception("WTF - bad file parse: "+str);
      }
    }
    // unpack Scene.all into the Scene list.
    Scene.createListFromAll();
    
  }
  
  private static void parseMark(){
    System.out.println("Mark "+ k(1)+" "+k(2));
    new Mark(k(1),k(2));
  }
  private static void parseColor(){
    System.out.println("Color "+ k(1)+" "+k(2)+" "+k(3));
    new Dress.Col(new Color(k(1),k(2),k(3)));
  }  
  private static void parseDress(){
    System.out.println("Dress "+ k(1)+" "+k(2));
    new Dress(true, k(1),k(2),k(3),k(4),k(5),k(6));
  }
  private static void parseScene(){
    System.out.println("Scene: "+ str);
    // Scene.all.clear();
    Scene.all.add(new Scene(str));
  } 
  
  public static void saveFile(){
     try{
      BufferedWriter w = new BufferedWriter(new FileWriter("Anime.dat"));
       //BufferedWriter w = new BufferedWriter(new OutputStreamWriter(System.out));
      w.newLine();  // write out Marks
      for(int i = 0; i<Mark.all.size(); i++){
        Mark m = Mark.all.get(i); w.write("M "+m.x+" "+m.y); w.newLine();
      }
      w.newLine(); // Write out Colors
      for(int i = 0; i<Dress.COLORS.size(); i++){
        Color c = Dress.COLORS.get(i).full; 
        w.write("C "+c.getRed()+" "+c.getGreen()+" "+c.getBlue()); w.newLine();
      }
      w.newLine(); // Write out Dresses
      for(int i = 0; i<Dress.costumes.size(); i++){
        Dress d = Dress.costumes.get(i); 
        w.write("D "+d.c+" "+d.b+" "+d.r+" "+d.w+" "+d.h+" "+d.s); w.newLine();
      }
      w.newLine(); // Scenes
      for(int i = 0; i<Scene.all.size(); i++){
        Scene s = Scene.all.get(i); w.write(s.tween); w.newLine();
      }
      w.close();
    } catch(Exception e){
      System.out.println("problems writing scene.dat");
      System.out.println(e);
    }   
  }
}
