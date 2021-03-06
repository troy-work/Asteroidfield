

import pulpcore.Build;
import pulpcore.CoreSystem;
import pulpcore.image.Colors;
import pulpcore.image.CoreFont;
import pulpcore.platform.ConsoleScene;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Button;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.Label;
import pulpcore.sprite.Sprite;
import pulpcore.Stage;

/**
 * 
 * @author Troy Cox
 */
public class UncaughtExceptionScene extends Scene2D {
    
    // Only send once per browser session to avoid talkback spam
    static boolean uploadedThisSession = false;
    
    Button retryButton;
    Button consoleButton;
    
    @Override
    public void load() {
        add(new FilledSprite(Colors.rgb(0, 0, 170)));
        
        // Send the talkback fields via POST 
        if (!uploadedThisSession && "true".equals(CoreSystem.getAppProperty("talkback"))) {
            uploadedThisSession = true;
            CoreSystem.uploadTalkBackFields("/talkback.py");
        }
        
        CoreFont font = CoreFont.getSystemFont().tint(Colors.WHITE);
        Group message = Label.createMultilineLabel(font, "Oops! An error occurred.", 
            Stage.getWidth() / 2, 150, Stage.getWidth() - 20);
        message.setAnchor(.5,.5);
        add(message);
        
        if (Build.DEBUG) {
            consoleButton = Button.createLabeledButton("Show Console", Stage.getWidth() / 2, 300);
            consoleButton.setAnchor(.5,.5);
            add(consoleButton);
        }
        
        retryButton = Button.createLabeledButton("Restart", Stage.getWidth() / 2, 350);
        retryButton.setAnchor(.5,.5);
        add(retryButton);
    }
    
    @Override 
    public void update(int elapsedTime) {
        if (retryButton.isClicked()) {
            Stage.setScene(new LoadingScene());
        }
        if (Build.DEBUG && consoleButton.isClicked()) {
            Stage.pushScene(new ConsoleScene());
        }
    }
}
