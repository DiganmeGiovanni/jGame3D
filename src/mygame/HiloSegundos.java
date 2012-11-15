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
public class HiloSegundos extends Thread{
               
        BitmapText text;
        
        
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
                text.setText(""+(MainPrado.s+=1));                                
            }
        }
        
        
    }
    

