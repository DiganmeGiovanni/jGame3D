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
import com.jme3.ui.Picture;

/**
 *
 * @author Eddy
 */
public class Interfaz {

    AssetManager assetManager;
    Node guiNode;
    BitmapFont guiFont;
    BitmapText textScore, textLife;
    int width;
    int height;
    private float yIcon, yText, xText_score, xIcon_score, xText_life, xTime, xBar_life, yBar_life;

    public Interfaz(AssetManager assetManager, Node guiNode, BitmapFont guiFont, int width, int height) {
        this.assetManager = assetManager;
        this.guiNode = guiNode;
        this.guiFont = guiFont;
        this.width = width;
        this.height = height;
        resolution();
        segundero();
        setLife("life1");
        score();
    }

    private void setLife(String life) {
        Picture pLife = new Picture(life);
        pLife.setImage(assetManager, "Interface/life_bar/life_bar_full.png", true);
        pLife.setWidth(283);
        pLife.setHeight(91);
        pLife.setPosition(xBar_life, yBar_life);
        guiNode.attachChild(pLife);

        guiFont = assetManager.loadFont("Interface/Fonts/Appleberry50.fnt");//assetManager.loadFont("Interface/Fonts/Default.fnt");
        textLife = new BitmapText(guiFont, false);
        textLife.setSize(guiFont.getCharSet().getRenderedSize());
        textLife.setColor(new ColorRGBA(.101f, 0.549f, 1f, 1f));
        textLife.setText("" + MainPrado.vidas);
        textLife.setLocalTranslation(xText_life, yText, 0);
        guiNode.attachChild(textLife);
    }

    public void checarVidas() {
//       
        if (MainPrado.salud == 4) {
            quitLife("life1", "Interface/life_bar/life_bar_full.png");
        } else {
            if (MainPrado.salud == 3) {
                quitLife("life1", "Interface/life_bar/life_bar_orange.png");
            } else {
                if (MainPrado.salud == 2) {
                    quitLife("life1", "Interface/life_bar/life_bar_red.png");
                } else {
                    if (MainPrado.salud == 1) {
                        quitLife("life1", "Interface/life_bar/life_bar_die.png");
                        MainPrado.salud = 4;                                                
                        textLife.setText("" + (MainPrado.vidas-1));
                    }
                }
            }
        }
    }

    private void quitLife(String name1, String url) {
        guiNode.detachChildNamed(name1);
        setLife(name1, url);
    }

    private void setLife(String name, String url) {
        Picture pLife = new Picture(name);
        pLife.setImage(assetManager, url, true);
        pLife.setWidth(283);
        pLife.setHeight(91);
        pLife.setPosition(xBar_life, yBar_life);
        guiNode.attachChild(pLife);
    }

    public void segundero() {

        ColorRGBA color = new ColorRGBA();
        color.set(0.4f, 0.2f, 0, 1f);

        // Display a line of text with a default font
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Appleberry60.fnt");
        BitmapText text = new BitmapText(guiFont, false);
        text.setSize(guiFont.getCharSet().getRenderedSize());
        text.setColor(color);

        HiloSegundos hs = new HiloSegundos(text);
        hs.start();
        text.setLocalTranslation(xTime, yText, 0);
        guiNode.attachChild(text);

    }

    public void score() {

        Picture pScore = new Picture("HUD score");
        pScore.setImage(assetManager, "Interface/score.png", true);
        pScore.setWidth(48);
        pScore.setHeight(48);
        pScore.setPosition(xIcon_score, yIcon);
        guiNode.attachChild(pScore);

        guiFont = assetManager.loadFont("Interface/Fonts/Appleberry60.fnt");//assetManager.loadFont("Interface/Fonts/Default.fnt");
        textScore = new BitmapText(guiFont, false);
        textScore.setSize(guiFont.getCharSet().getRenderedSize());
        textScore.setColor(new ColorRGBA(0.03921f, 0.7490f, 0.03921f, 1f));
        textScore.setText("" + MainPrado.score);
        textScore.setLocalTranslation(xText_score, yText, 0);
        guiNode.attachChild(textScore);
    }
    
    public void checarScore(){
        textScore.setText(""+MainPrado.score);
    }

    private void gameover() {
//        guiNode.detachAllChildren();
        Picture pOver = new Picture("HUD gameover");
        pOver.setImage(assetManager, "Interface/gameover.png", true);
        pOver.setWidth(width / 2);
        pOver.setHeight(height / 2);
        pOver.setPosition(width / 4, height / 4);
        guiNode.attachChild(pOver);
    }

    private void resolution() {

        if (width == 800 && height == 600) {
        } else {
            if (width == 1024 && height == 768) {
            } else {
                if (width == 1152 && height == 864) {
                } else {
                    if (width == 1280 && height == 720) {
                        yIcon = 630;
                        yText = 680;
                        xText_score = 1070;
                        xIcon_score = 1000;
                        xText_life = 107;
                        xTime = 600;
                        xBar_life = 70;
                        yBar_life = 610;
                    }
                }
            }
        }
    }
}
