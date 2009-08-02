


import pulpcore.image.AnimatedImage;
import pulpcore.image.CoreImage;
import pulpcore.math.CoreMath;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;

/**
 *
 * @author Troy Cox
 */
final public class Building extends ImageSprite {

    final int xOff;
    final int yOff;
    boolean hit = false;
    int flameFrames = 0;
    private boolean doAgain = true;
    private final int frame;
    private final int totalFrames;
    private final Flame flame;


    /**
     *
     * @param imageAsset  #
     * @param x           #
     * @param y           #
     * @param xOff        #
     * @param yOff        #
     * @param scene       #
     * @param flameImage  #
     */
    public Building(CoreImage imageAsset, int x, int y, int xOff, int yOff, Scene2D scene, CoreImage[] flameImage) {
        super(imageAsset, x, y);
        this.xOff = xOff;
        this.yOff = yOff;
        pixelSnapping.set(true);
        setPixelLevelChecks(true);
        ((AnimatedImage) getImage()).setFrame(0);
        ((AnimatedImage) getImage()).pause();
        totalFrames = ((AnimatedImage) getImage()).getNumFrames();
        frame = 0;
        flame = new Flame(scene, flameImage);
    }

    @Override
    public void update(int elapsedTime) {

        if (doAgain) {
            if (hit) {
                hit = false;
                if (frame < totalFrames) {
                    ((AnimatedImage) getImage()).start();
                    doAgain = false;
                    getImage().update(0);
                }
            }
        } 
        else {
        	if (flameFrames < 1) {
                int rx = (int) CoreMath.rand(x.get() - 20, x.get() + 20);
                int ry = (int) CoreMath.rand(y.get(), y.get() - 5);
                int flameHeight = CoreMath.rand(0, 70);
                flame.MakeFire(rx, ry, rx, ry - flameHeight);
				flameFrames = 1;
			} else {
				flameFrames-=1;
			}
         }

        super.update(elapsedTime);
        x.set(((AsteroidField) getScene2D()).panX + xOff);
        y.set(((AsteroidField) getScene2D()).panY + yOff);
        enabled.set(doAgain);
    }
}
