
import pulpcore.Input;
import pulpcore.image.CoreImage;
import pulpcore.sprite.ImageSprite;

/**
 *
 * @author Troy Cox
 */
public class Gunner extends ImageSprite {

    final BlasterParticleGroup blast;
    boolean invert = false;

    private int GetTargetX() {
        return ((AsteroidField) getScene2D()).gunnerTargetX;
    }

    private int GetTargetY() {
        return ((AsteroidField) getScene2D()).gunnerTargetY;
    }

    /**
     *
     * @param imageAsset #
     * @param x          #
     * @param y          #
     * @param blast      #
     */
    public Gunner(CoreImage imageAsset, int x, int y, BlasterParticleGroup blast) {
        super(imageAsset, 0, 0);
        pixelSnapping.set(true);
        this.x.set(x);
        this.y.set(y);
        this.blast = blast;
    }

    @Override
    public void update(int elapsedTime) {
        getScene2D().getMainLayer().moveToTop(this);
        super.update(elapsedTime);
        if (!((AsteroidField) getParent().getScene2D()).displayingScore) {
            this.angle.animateTo(Math.atan((this.y.get() - GetTargetY()) / (this.x.get() - GetTargetX())), 0);

            if (Input.isMousePressed() || Input.isPressed(Input.KEY_SPACE)) {
                if (invert) {
                    blast.MakeParticles(x.getAsInt() - (int) (160 * Math.cos(angle.get())), y.getAsInt() - (int) (160 * Math.sin(angle.get())), GetTargetX(), GetTargetY());
                } else {
                    blast.MakeParticles(x.getAsInt() + (int) (160 * Math.cos(angle.get())), y.getAsInt() + (int) (160 * Math.sin(angle.get())), GetTargetX(), GetTargetY());
                }
            }
        }

    }
}
