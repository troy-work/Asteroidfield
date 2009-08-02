import pulpcore.Stage;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Button;
import pulpcore.sprite.ImageSprite;

/**
 *
 * @author Troy Cox
 */
public class OptionScene extends Scene2D {
    
    Button toggleButton;
    Button backButton;
    Button errorButton;
    
    @Override
    public void load() {
        toggleButton = Button.createLabeledToggleButton("Some Toggle", 320, 300);
        toggleButton.setAnchor(.5,.5);
        errorButton = Button.createLabeledToggleButton("Some Error", 320, 350);
        errorButton.setAnchor(.5,.5);
        backButton = Button.createLabeledButton("<< Back", 320, 400);
        backButton.setAnchor(.5,.5);
        
        add(new ImageSprite("background.png", 0, 0));
        add(toggleButton);
        add(errorButton);
        add(backButton);
    }
    
    @Override 
    public void update(int elapsedTime) {
        if (backButton.isClicked()) {
            // Go back to the previous scene
            Stage.popScene();
        }
        if (errorButton.isClicked()) {
            // Force showing the uncaught exception scene
            throw new RuntimeException();
        }
    }
}
