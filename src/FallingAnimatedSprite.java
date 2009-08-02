


import java.util.Iterator;
import pulpcore.Stage;
import pulpcore.animation.Fixed;
import pulpcore.image.AnimatedImage;
import pulpcore.sound.Sound;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import static pulpcore.math.CoreMath.rand;

/**
 *
 * @author Troy Cox
 */
public class FallingAnimatedSprite extends ImageSprite{

    /**
    Ignored if boolean useRandomRotation is set to true.
     */
    private double rotationDegree = 0;
    boolean useRandomScaling = true;
    boolean useRandomRotation = true;
    double originalWidth = 0;
    double originalHeight = 0;
    /**
     * Collision checking requires the sprite to be added to a group.
     */
    boolean doCollisionChecking = false;
    boolean die = false;
    final Group splatter = new Group();
    final ParticleGroup particleGroup;
    Sound explosionSound;
    final Fixed soundLevel = new Fixed();
    private int fallSpread = 500;
    private int restageOrDie = Stage.getHeight();
    private double xMove;
    private double yMove;
    public boolean killMe = false;


    private boolean Die() {
        die = width.get() < 20 || height.get() < 20;
        return die;
    }


    public FallingAnimatedSprite(ParticleGroup particleGroup) {
        super("asteroid.png", (double) 0, (double) 0);
        this.useRandomRotation = true;
        this.useRandomScaling = true;
        this.particleGroup = particleGroup;
        this.pixelSnapping.set(true);
        this.setPixelLevelChecks(true);
    }

    @Override
    public void update(int elapsedTime) {
        super.update(elapsedTime);
        x.set(((AsteroidField) getScene2D()).panXOffSet + x.get() + xMove);
        y.set(((AsteroidField) getScene2D()).panYOffSet + y.get() + yMove);
        restageOrDie = ((AsteroidField) getScene2D()).panY + 10;
        if (doCollisionChecking && visible.get()) {

            Iterator i = getParent().iterator();
            while (i.hasNext()) {
                try {
                    FallingAnimatedSprite testSprite = (FallingAnimatedSprite) i.next();
                    if (testSprite.doCollisionChecking && testSprite.visible.get()) {
                        if (this.intersects(testSprite)) {
                            if (!this.equals(testSprite)) {
                                ManageDeath(this);
                                ManageDeath(testSprite);
                            }
                        }
                    }                } catch (Exception ignored) {
                }
            }
        }
        if (killMe) {
            KillMe();
        }

        if (IsDone() || die || Die()) {
            getParent().remove(this);
            this.visible.set(false);
        }


    }

    private void ManageDeath(FallingAnimatedSprite sprite) {
        sprite.doCollisionChecking = false;
        sprite.DoExplode();
        sprite.SetNormalMotion();
        sprite.scaleTo(sprite.width.get() * .5, sprite.height.get() * .5, 0);
        sprite.doCollisionChecking = true;
    }

    public void Start() {
        Initialize();
    }

    public void DoExplode() {
        if (explosionSound != null) {
            explosionSound.play(soundLevel);

        }
        DoSpriteBreakApart();

    }

    private void DoSpriteBreakApart() {

        for (int i = 1; i <= 4; i++) {
            if (particleGroup != null) {
                particleGroup.MakeParticles(x.getAsInt(), y.getAsInt(), (int) (x.get() - rand(-(double) 30, (double) 30)), (int) (y.get() + rand(-(double) 30, (double) 30)), 3);
            }
        }

    }

    private void KillMe() {
        scaleTo(width.get() * .01, height.get() * .01, 0);
        doCollisionChecking = false;
        die = true;
        DoExplode();
        x.set(x.get()-20);
        DoExplode();
        x.set(x.get()+40);
        DoExplode();
    }

    private void OffSetFrames() {
        ((AnimatedImage) getImage()).setFrame((int) (Math.floor((Math.random() * ((AnimatedImage) getImage()).getNumFrames()))));
    }

    private void Initialize() {
        originalHeight = height.get();
        originalWidth = width.get();
        OffSetFrames();
        ReStage();
    }

    private void ReStage() {
        pixelSnapping.set(true);
        setLocation(rand(Stage.getWidth()), - 40);
        angle.set(getRotationDegree());
        if (useRandomScaling) {
            RandomScaling();
        }
        SetNormalMotion();
    }

    public void SetRotationAngle(double angle) {
        useRandomRotation = false;
        setRotationDegree(angle);
    }

    private boolean IsDone() {
        return y.get() > restageOrDie - 1 || x.get() > Stage.getWidth() + 50 || x.get() < -50;
    }

    private void RandomRotation() {
        setRotationDegree(rand(Math.PI * 2));
    }

    private void RandomScaling() {
        scaleTo(rand(originalWidth * .5, originalWidth * 1.5), rand(originalHeight * .5, originalHeight * 1.5), 0);
    }

    public double getRotationDegree() {
        if (useRandomRotation) {
            RandomRotation();
        }
        return rotationDegree;

    }

    private void SetNormalMotion() {
        xMove = rand(-fallSpread, fallSpread) / 1000;
        yMove = 1000 / rand(300, 600);
    }

    private void setRotationDegree(double rotationDegree) {
        this.rotationDegree = rotationDegree;
    }
}

