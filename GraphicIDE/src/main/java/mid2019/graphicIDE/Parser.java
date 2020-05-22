package mid2019.graphicIDE;

import com.github.javaparser.ast.CompilationUnit;
import static com.github.javaparser.StaticJavaParser.*;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.checkerframework.checker.units.qual.A;

public class Parser {
//  public static ArrayList<String> allFile = new ArrayList<>();
//  public static ArrayList<String> allPath = new ArrayList<>();
  public static HashMap<String, List<String>> layers = new HashMap<>();

  public static void displayIt(File node, int start){
    if(node.isDirectory()){
      String[] subNote = node.list();
      for(String filename : subNote){
        String key = node.getAbsolutePath().substring(start);
        if (key.length() > 0) {layerBuilder(key, filename);}
        displayIt(new File(node, filename), start);
      }
    } else {
      layerBuilder(node.getAbsolutePath().substring(start), node.getAbsolutePath());
//      allFile.add(node.getAbsolutePath());
//      allPath.add(node.getAbsolutePath().substring(start));
    }
  }
  public static void layerBuilder(String key, String filename){
    List<String> newV = layers.getOrDefault(key, new ArrayList<>());
    newV.add(filename);
    layers.put(key, newV);
  }
  public static CompilationUnit parseF(String fileName) throws IOException {
    InputStream in = null;
    CompilationUnit cu = null;
    try
    {
      /*in = new FileInputStream("E:" + File.separator +
          "NEU" + File.separator + "courses" + File.separator +
          "MidJava" + File.separator + "GraphicIDE" + File.separator +
          "src" + File.separator + "main" + File.separator +
          "java" + File.separator + "mid2019" + File.separator +
          "graphicsLib" + File.separator + "G.java");*/
      in = new FileInputStream(fileName);
      cu = parse(in);
    } finally
    {
      in.close();
    }
    return cu;
  }

  public static void fileParser(String fileName) throws IOException {
    for(TypeDeclaration type : parseF(fileName).getTypes()) {
      // first give all this java doc member
      List<BodyDeclaration> members = type.getMembers();
      // check all member content
      for(BodyDeclaration member : members) {
        // if member state equal ClassOrInterfaceDeclaration, and you can identify it which is inner class
        if(member.isClassOrInterfaceDeclaration()) {
          //System.out.println("class: "+member.asClassOrInterfaceDeclaration().getName() + "{");
          // get inner class method
          for(MethodDeclaration method : member.asClassOrInterfaceDeclaration().getMethods()) {
            System.out.println(method.getDeclarationAsString() + " " + method.getAccessSpecifier() + " "+ method.getTypeAsString() + " " + method.getNameAsString() + " " + method.getParameters());
            //System.out.println(method.getBody());
          }
          //System.out.println("}");
        }
      }
    }
  }
  public static void main(String[] args) throws IOException {
    //System.out.println(args[0]);
//    String srcPath = "E:" + File.separator + "NEU" + File.separator + "courses" + File.separator + "MidJava" + File.separator + "GraphicIDE" + File.separator + "src";
    String srcPath = "E:\\NEU\\courses\\MidJava\\GraphicIDE\\src";
    displayIt(new File(srcPath), srcPath.length());
    for (String key: layers.keySet()){
      System.out.println(key + "->" + layers.get(key));
      if(key.length() > 5 && key.substring(key.length()-5).equals(".java")){
//        System.out.println(key + "{");
        for(String fileName: layers.get(key)) {
//          fileParser(fileName);
        }
//        System.out.println("}");
      }
    }

  }
}
