/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.Timer;

/**
 *
 * @author Eddy
 */
public class Marcadores {
    
    Timer timer;
    AssetManager assetManager;
    BitmapFont guiFont;
    Node guiNode;
    
    
    public Marcadores(AssetManager assetManager, BitmapFont guiFont, Node guiNode){
        this.assetManager=assetManager;
        this.guiFont=guiFont;
        this.guiNode=guiNode;        
    }
    
    public void segundero(){        
         // Display a line of text with a default font
        guiNode.detachAllChildren();
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        BitmapText helloText = new BitmapText(guiFont, false);       
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setColor(ColorRGBA.Blue);        
        HiloSegundos hs = new HiloSegundos(helloText);
        hs.start();        
        helloText.setLocalTranslation(380, 21*27, 0);
        guiNode.attachChild(helloText);
       
    }
    
    public void encabezado(String text, float x){                
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");        
        BitmapText helloText = new BitmapText(guiFont, false);       
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setColor(ColorRGBA.Blue);        
        helloText.setText(text);
        helloText.setLocalTranslation(x, 21*27, 0);
        guiNode.attachChild(helloText);
    }
    
    public class HiloSegundos extends Thread{
        BitmapText text;
        int s=0;
        
        HiloSegundos(BitmapText text){
            this.text=text;
        }
        
        @Override
        public void run(){
            while(true){
                
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(""+e.getMessage());
                }
                text.setText(""+(s+=1));
                
                
            }
        }
        
        
    }
    
}
