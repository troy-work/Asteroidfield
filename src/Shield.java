


import pulpcore.animation.Fixed;
import pulpcore.sound.Sound;
import pulpcore.sprite.ImageSprite;


/**
 *
 * @author Troy Cox
 */
final public class Shield extends ImageSprite {
    final int xOff;
    final int yOff;
    final Sound die = Sound.load("shieldDie.wav");

    /**
     *
     * @param x


     * @param y

     */
    public Shield(int x, int y) {
        super("Shield.png", 0, 0);
        this.xOff = 322;
        this.yOff = 0;
        this.pixelSnapping.set(true);
        this.setPixelLevelChecks(true);
    }

    @Override
    public void update(int elapsedTime) {
        super.update(elapsedTime);
        x.set(((AsteroidField)getScene2D()).panX + xOff);
        y.set(((AsteroidField)getScene2D()).panY + yOff);
        if (((AsteroidField)getScene2D()).shieldHealth<50){
            die.play(new Fixed(.1));
            this.getParent().remove(this);
        }
    }
}