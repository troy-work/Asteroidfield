


import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import pulpcore.animation.event.RemoveSpriteEvent;
import pulpcore.image.CoreImage;
import pulpcore.math.Path;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;
import static pulpcore.math.CoreMath.rand;

/**
 *
 * @author Troy Cox
 */
final public class Scout extends ImageSprite {

    private double position = 0;
    //private Path scoutPath2 = new Path("M 38,41 L 38,161 L 162,161 L 162,324");
    private double xOffset;
    private double angleCorrection = (90 * (Math.PI / 180));
    private Path scoutPath;
    CoreImage[] trailImage = CoreImage.load("trail.png").split(10, 1);
    double angleBig = (10 * (Math.PI / 180));
    double angleSmall = (-10 * (Math.PI / 180));
    int delay;

    private int GetX() {
        return (((AsteroidField) getScene2D()).panXOffSet);
    }

    private int GetY() {
        return (((AsteroidField) getScene2D()).panYOffSet);
    }

    /**
     *
     * @param imageAsset
     * @param x
     * @param y
     * @param delay
     */
    public Scout(String imageAsset, int x, int y, int delay) {
        super(imageAsset, x, y);
        this.delay = delay;
        pixelSnapping.set(true);
        setPixelLevelChecks(true);
        setAnchor(Scout.CENTER);
        xOffset = rand(0, 300);
        int a = rand(1, 2);
        switch (a) {
            case 1:
                scoutPath = new Path("M 94,-141 C95,278 113,299 249,299 C323,299 388,132 325,53 C204,-83 109,282 250,275 C327,270 255,88 105,-180");
                break;
            case 2:
                scoutPath = new Path("M 350,-141 C 349,278 331,299 195,299 C 121,299 56,132 119,53 C 240,-83 335,282 194,275 C 117,270 189,88 339,-180");
                break;
        }
    }

    @Override
    public void update(int elapsedTime) {
        super.update(elapsedTime);
        if (delay <= 0) {
            if (position < 1) {
                position += .0025;
            }
            scoutPath.translate(GetX(), GetY());
            scoutPath.place(this, position);
            angle.set(scoutPath.getAngle(position) - angleCorrection);
            x.set(x.get() + xOffset);
            if (y.get() > 150 && angle.get() > angleSmall && angle.get() < angleBig) {
                ((AsteroidField) getScene2D()).Laser(x.getAsInt(), y.getAsInt(), angle.get() + angleCorrection, 8);

            }
            Trail(trailImage, getScene2D(), x.getAsInt(), y.getAsInt(), angle.get() + angleCorrection, 1);
            getParent().moveToTop(this);
            if (position >= 1) {
                getParent().remove(this);
            }
        } else {
            scoutPath.place(this, position);
            delay -= 1;
        }

    }

    private void Trail(CoreImage[] trail, Scene2D s, int x, int y, double angle, int numParticles) {

        final Timeline timeline = new Timeline();
        for (int i = 0; i < numParticles; i++) {
            int duration = 100;
            int moveDistance = rand(0, 2);
            int startX = x - (int) (16 * Math.cos(angle));
            int startY = y - (int) (16 * Math.sin(angle));
            int goalX = startX - (int) (moveDistance * Math.cos(angle));
            int goalY = startY - (int) (moveDistance * Math.sin(angle));

            CoreImage image = trail[rand(trail.length - 1)];
            Sprite sprite = new ImageSprite(image, startX, startY);
            sprite.angle.set(angle - angleCorrection);
            sprite.setAnchor(Sprite.SOUTH);

            sprite.alpha.set(100);
            getParent().add(sprite);
            timeline.animate(sprite.height, sprite.height.get(), sprite.width.get(), duration, Easing.REGULAR_OUT);
            timeline.animateTo(sprite.x, goalX, duration, Easing.REGULAR_OUT);
            timeline.animateTo(sprite.y, goalY, duration, Easing.REGULAR_OUT);
            timeline.at(100).animateTo(sprite.alpha, 0, duration - 10, Easing.REGULAR_OUT);
            timeline.add(new RemoveSpriteEvent(s.getMainLayer(), sprite, duration));
        }

        s.addTimeline(timeline);
    }
}