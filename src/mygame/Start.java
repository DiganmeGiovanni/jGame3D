/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.system.JmeCanvasContext;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author Eddy
 */
public class Start extends JFrame{
    
    JButton boton;
    JmeCanvasContext ctx;
    public Start(String name, JmeCanvasContext ctx){
        
        super(name);
        this.ctx= ctx;
        init();        
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        this.setVisible(true);
    }
    
    public void init(){
        
        boton= new JButton("Start");        
        boton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                        dispose();
                        JFrame juego = new JFrame("Juego");
                        juego.setSize(ctx.getSettings().getWidth(),ctx.getSettings().getHeight());
                        juego.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                        JPanel panel = new JPanel(); // a panel
                        
                        juego.add(ctx.getCanvas());
//                        juego.add(panel);
                        juego.setVisible(true);
            }           
        });
        
        this.add(boton);
    }
        
}
