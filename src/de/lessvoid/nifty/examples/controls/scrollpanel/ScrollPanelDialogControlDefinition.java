package de.lessvoid.nifty.examples.controls.scrollpanel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.scrollpanel.builder.ScrollPanelBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;

/**
 * The ScrollPanelDialogControlDefinition registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 * @author void
 */
public class ScrollPanelDialogControlDefinition {
  public static final String NAME = "Hogar";
  public static final String NAME1 = "Hogar";
  public static final String NAME2 = "Bodega";
  public static final String NAME3 = "Estudio";
  public static final String NAME4 = "Prado";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register1(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME1) {{
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
            
          childLayoutHorizontal();
          control(builders.createLabel("Hogar:"));
          panel(new PanelBuilder(){{
            width("*");
            height("*");
            backgroundImage("Interface/Hogar.png");
          }});
        }});

        panel(new PanelBuilder() {{
            width("100%");
                paddingTop("10px");
                childLayoutCenter();
                control(new ButtonBuilder("play") {

                  {
                    label("Play");
                    alignCenter();
                    valignCenter();
                    interactOnClick("startGame(blank)");
                    
                  }
                });
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
  
  public static void register2(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME2) {{
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
            
          childLayoutHorizontal();
          control(builders.createLabel("Bodega:"));
          panel(new PanelBuilder(){{
            width("*");
            height("*");
            backgroundImage("Interface/Bodega.png");
          }});
        }});

        panel(new PanelBuilder() {{
            width("100%");
                paddingTop("10px");
                childLayoutCenter();
                control(new ButtonBuilder("play") {

                  {
                    label("Play");
                    alignCenter();
                    valignCenter();
                  }
                });
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
  
  public static void register3(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME3) {{
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
            
          childLayoutHorizontal();
          control(builders.createLabel("ScrollPanel:"));
          panel(new PanelBuilder(){{
            width("*");
            height("*");
            backgroundImage("Interface/background-new.png");
          }});
        }});

        panel(new PanelBuilder() {{
            width("100%");
                paddingTop("10px");
                childLayoutCenter();
                control(new ButtonBuilder("play") {

                  {
                    label("Play");
                    alignCenter();
                    valignCenter();
                  }
                });
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
  
  public static void register4(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME4) {{
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
            
          childLayoutHorizontal();
          control(builders.createLabel("Prado:"));
          panel(new PanelBuilder(){{
            width("*");
            height("*");
            backgroundImage("Interface/Prado.png");
          }});
        }});

        panel(new PanelBuilder() {{
            width("100%");
                paddingTop("10px");
                childLayoutCenter();
                control(new ButtonBuilder("play") {

                  {
                    label("Play");
                    alignCenter();
                    valignCenter();
                  }
                });
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
