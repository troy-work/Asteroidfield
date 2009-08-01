


import pulpcore.animation.Fixed;
import pulpcore.sound.Sound;
import pulpcore.sprite.ImageSprite;


/**
 *
 * @author Troy Cox
 */
final public class Shield extends ImageSprite {
    int xOff;
    int yOff;
    Sound die = Sound.load("shieldDie.wav");

    /**
     *
     * @param imageAsset
     * @param x
     * @param y
     * @param xOff
     * @param yOff
     */
    public Shield(String imageAsset, int x, int y, int xOff, int yOff) {
        super(imageAsset, 0, 0);
        this.xOff = xOff;
        this.yOff = yOff;
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