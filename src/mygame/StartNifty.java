/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.StyleBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.console.builder.ConsoleBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;
import de.lessvoid.nifty.examples.controls.common.MenuButtonControlDefinition;
import de.lessvoid.nifty.examples.controls.scrollpanel.ScrollPanelDialogControlDefinition;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

/**
 *
 * @author Eddy
 */
public class StartNifty {
    
    final Nifty nifty;
  
    private static CommonBuilders builders = new CommonBuilders();
    public StartNifty(Nifty nifty){
        this.nifty=nifty;
    
        init();
    }
    
    public void init(){
        /**
     * nifty demo code
     */
    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");    
    nifty.registerMusic("credits", "Interface/sound/Loveshadow_-_Almost_Given_Up.ogg");
    nifty.registerMouseCursor("hand", "Interface/mouse-cursor-hand.png", 5, 4);
    //nifty.setDebugOptionPanelColors(true);
    registerMenuButtonHintStyle(nifty);
    registerStyles(nifty);
    registerConsolePopup(nifty);
    registerCreditsPopup(nifty);

    // register some helper controls
    MenuButtonControlDefinition.register(nifty);
    DialogPanelControlDefinition.register(nifty);

    // register the dialog controls
    ScrollPanelDialogControlDefinition.register1(nifty);
    ScrollPanelDialogControlDefinition.register2(nifty);
    ScrollPanelDialogControlDefinition.register3(nifty);
    ScrollPanelDialogControlDefinition.register4(nifty);
    
    createDemoScreen(nifty);
    createBlanckScreen(nifty);
    nifty.gotoScreen("demo");
    }
    
    private static Screen createDemoScreen(final Nifty nifty) {    
    Screen screen = new ScreenBuilder("demo") {

      {
        controller(new JmeControlsDemoScreenController(
                "menuButton1", "Nivel1",
                "menuButton2", "Nivel2",
                "menuButtonTextField", "Nivel3",
                "menuButtonScrollPanel", "Nivel4"));
        inputMapping("de.lessvoid.nifty.input.mapping.DefaultInputMapping"); // this will enable Keyboard events for the screen controller
        layer(new LayerBuilder("layer") {

          {
            backgroundImage("Interface/OmeEv.jpg");
            childLayoutVertical();
            panel(new PanelBuilder("navigation") {

              {
                width("100%");
                height("63px");
                backgroundColor("#5588");
                childLayoutHorizontal();
                padding("20px");
                control(MenuButtonControlDefinition.getControlBuilder("menuButton1", "Nivel 1", "ListBox demonstration\n\nThis example shows adding and removing items from a ListBox\nas well as the different selection modes that are available."));
                panel(builders.hspacer("10px"));
                control(MenuButtonControlDefinition.getControlBuilder("menuButton2", "Nivel 2", "DropDown and RadioButton demonstration\n\nThis shows how to dynamically add items to the\nDropDown control as well as the change event."));
                panel(builders.hspacer("10px"));
                control(MenuButtonControlDefinition.getControlBuilder("menuButtonTextField", "Nivel 3", "TextField demonstration\n\nThis example demonstrates the Textfield example using the password\nmode and the input length restriction. It also demonstrates\nall of the new events the Textfield publishes on the Eventbus."));
                panel(builders.hspacer("10px"));
                control(MenuButtonControlDefinition.getControlBuilder("menuButtonScrollPanel", "Nivel 4", "ScrollPanel demonstration\n\nThis simply shows an image and uses the ScrollPanel\nto scroll around its area. You can directly input\nthe x/y position you want the ScrollPanel to scroll to."));
                panel(builders.hspacer("10px"));
                control(MenuButtonControlDefinition.getControlBuilder("menuButtonCredits", "?", "Credits\n\nCredits and Thanks!", "25px"));
              }
            });
            panel(new PanelBuilder("dialogParent") {

              {
                childLayoutOverlay();
                width("100%");
                alignCenter();
                valignCenter();
                control(new ControlBuilder("Nivel1", ScrollPanelDialogControlDefinition.NAME1));
                control(new ControlBuilder("Nivel2", ScrollPanelDialogControlDefinition.NAME2));
                control(new ControlBuilder("Nivel3", ScrollPanelDialogControlDefinition.NAME3));
                control(new ControlBuilder("Nivel4", ScrollPanelDialogControlDefinition.NAME4));
              }
            });
          }
        });
      
      }
    }.build(nifty);
    return screen;
  }
  
  private  Screen createBlanckScreen(final Nifty nifty){
      Screen screen= new ScreenBuilder("blank"){
          
      }.build(nifty);
      
      return screen;
  }
  
  

  private static void registerMenuButtonHintStyle(final Nifty nifty) {
    new StyleBuilder() {

      {
        id("special-hint");
        base("nifty-panel-bright");
        childLayoutCenter();
        onShowEffect(new EffectBuilder("fade") {

          {
            length(150);
            effectParameter("start", "#0");
            effectParameter("end", "#d");
            inherit();
            neverStopRendering(true);
          }
        });
        onShowEffect(new EffectBuilder("move") {

          {
            length(150);
            inherit();
            neverStopRendering(true);
            effectParameter("mode", "fromOffset");
            effectParameter("offsetY", "-15");
          }
        });
        onCustomEffect(new EffectBuilder("fade") {

          {
            length(150);
            effectParameter("start", "#d");
            effectParameter("end", "#0");
            inherit();
            neverStopRendering(true);
          }
        });
        onCustomEffect(new EffectBuilder("move") {

          {
            length(150);
            inherit();
            neverStopRendering(true);
            effectParameter("mode", "toOffset");
            effectParameter("offsetY", "-15");
          }
        });
      }
    }.build(nifty);

    new StyleBuilder() {

      {
        id("special-hint#hint-text");
        base("base-font");
        alignLeft();
        valignCenter();
        textHAlignLeft();
        color(new Color("#000f"));
      }
    }.build(nifty);
  }

  private static void registerStyles(final Nifty nifty) {
    new StyleBuilder() {

      {
        id("base-font-link");
        base("base-font");
        color("#8fff");
        interactOnRelease("$action");
        onHoverEffect(new HoverEffectBuilder("changeMouseCursor") {

          {
            effectParameter("id", "hand");
          }
        });
      }
    }.build(nifty);

    new StyleBuilder() {

      {
        id("creditsImage");
        alignCenter();
      }
    }.build(nifty);

    new StyleBuilder() {

      {
        id("creditsCaption");
        font("Interface/verdana-48-regular.fnt");
        width("100%");
        textHAlignCenter();
      }
    }.build(nifty);

    new StyleBuilder() {

      {
        id("creditsCenter");
        base("base-font");
        width("100%");
        textHAlignCenter();
      }
    }.build(nifty);
  }
  
  

  private static void registerConsolePopup(Nifty nifty) {
    new PopupBuilder("consolePopup") {

      {
        childLayoutAbsolute();
        panel(new PanelBuilder() {

          {
            childLayoutCenter();
            width("100%");
            height("100%");
            alignCenter();
            valignCenter();
            control(new ConsoleBuilder("console") {

              {
                width("80%");
                lines(25);
                alignCenter();
                valignCenter();
                onStartScreenEffect(new EffectBuilder("move") {

                  {
                    length(150);
                    inherit();
                    neverStopRendering(true);
                    effectParameter("mode", "in");
                    effectParameter("direction", "top");
                  }
                });
                onEndScreenEffect(new EffectBuilder("move") {

                  {
                    length(150);
                    inherit();
                    neverStopRendering(true);
                    effectParameter("mode", "out");
                    effectParameter("direction", "top");
                  }
                });
              }
            });
          }
        });
      }
    }.registerPopup(nifty);
  }

  private static void registerCreditsPopup(final Nifty nifty) {
    final CommonBuilders common = new CommonBuilders();
    new PopupBuilder("creditsPopup") {

      {
        childLayoutCenter();
        panel(new PanelBuilder() {

          {
            width("80%");
            height("80%");
            alignCenter();
            valignCenter();
            onStartScreenEffect(new EffectBuilder("move") {

              {
                length(400);
                inherit();
                effectParameter("mode", "in");
                effectParameter("direction", "top");
              }
            });
            onEndScreenEffect(new EffectBuilder("move") {

              {
                length(400);
                inherit();
                neverStopRendering(true);
                effectParameter("mode", "out");
                effectParameter("direction", "top");
              }
            });
            onEndScreenEffect(new EffectBuilder("fadeSound") {

              {
                effectParameter("sound", "credits");
              }
            });
            onActiveEffect(new EffectBuilder("gradient") {

              {
                effectValue("offset", "0%", "color", "#00000000");
                effectValue("offset", "75%", "color", "#00000000");
                effectValue("offset", "100%", "color", "#00000000");
              }
            });
            onActiveEffect(new EffectBuilder("playSound") {

              {
                effectParameter("sound", "credits");
              }
            });
            padding("10px");
            childLayoutVertical();
            panel(new PanelBuilder() {

              {
                width("100%");
                height("*");
                childLayoutOverlay();
                childClip(true);
                panel(new PanelBuilder() {

                  {
                    width("100%");
                    childLayoutVertical();
                    onActiveEffect(new EffectBuilder("autoScroll") {

                      {
                        length(100000);
                        effectParameter("start", "0");
                        effectParameter("end", "-3200");
                        inherit(true);
                      }
                    });
                    panel(common.vspacer("800px"));
                                                                                                                                                                                  
                    
                    image(new ImageBuilder() {

                      {
                        style("creditsImage");
                        filename("Interface/Creditos.png");
                      }
                    });
                  }
                });
              }
            });
            panel(new PanelBuilder() {

              {
                width("100%");
                paddingTop("10px");
                childLayoutCenter();
                control(new ButtonBuilder("creditsBack") {

                  {
                    label("Atras");
                    alignRight();
                    valignCenter();
                    
                  }
                });
              }
            });
          }
        });
      }
    }.registerPopup(nifty);
  }
    
}
