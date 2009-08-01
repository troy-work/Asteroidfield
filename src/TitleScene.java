import pulpcore.animation.event.SceneChangeEvent;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Button;
import pulpcore.sprite.ImageSprite;

/**
 *
 * @author Troy Cox
 */
public class TitleScene extends Scene2D {
    
    Button playButton;
    Button slowButton;
    Button highScore;
    
    @Override
    public void load() {
        
        playButton = Button.createLabeledButton("Play", 320, 225);
        playButton.setAnchor(.5,.5);
        playButton.setImage("Play.png");
        playButton.alpha.set(200);

        slowButton = Button.createLabeledButton("Slow", 320,265);
        slowButton.setAnchor(.5,.5);
        slowButton.setImage("Slow.png");
        slowButton.alpha.set(200);
        
        highScore = Button.createLabeledButton("High Score", 400, 236);
        highScore.setSize(75, 50);
        highScore.setAnchor(.5,.5);
        highScore.setImage("HighScore.png");
        highScore.alpha.set(200);

        add(new ImageSprite("background.png", 0, 0));
        add(playButton);
        add(slowButton);
        add(highScore);
    }
    
    @Override 
    public void update(int elapsedTime) {
        if (playButton.isMouseOver()){
            playButton.setImage("Play.png");
            playButton.alpha.animateTo(255, 0);
        }
        else
        {
            playButton.setImage("Play.png");
            playButton.alpha.animateTo(200, 0);

        }
        if (playButton.isClicked()) {
            playButton.setImage("Play.png");
            playButton.alpha.animateTo(200, 0);
            addEvent(new SceneChangeEvent(new AsteroidField(), 300));
        }
        if (slowButton.isMouseOver()){
            slowButton.setImage("Slow.png");
            slowButton.alpha.animateTo(255, 0);
        }
        else
        {
            slowButton.setImage("Slow.png");
            slowButton.alpha.animateTo(200, 0);

        }
        if (slowButton.isClicked()) {
            slowButton.setImage("Slow.png");
            slowButton.alpha.animateTo(200, 0);
            addEvent(new SceneChangeEvent(new AsteroidField(false,false), 300));
        }

        if (highScore.isMouseOver()){
            highScore.setImage("HighScore.png");
            highScore.alpha.animateTo(255, 0);
        }
        else
        {
            highScore.setImage("HighScore.png");
            highScore.alpha.animateTo(200, 0);

        }
        if (highScore.isClicked()) {
            highScore.setImage("HighScore.png");
            highScore.alpha.animateTo(200, 0);
            addEvent(new SceneChangeEvent(new AsteroidField(false,true), 300));
        }


    }
}
