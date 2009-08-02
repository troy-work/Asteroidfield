


import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import pulpcore.animation.event.RemoveSpriteEvent;
import pulpcore.image.BlendMode;
import pulpcore.image.CoreImage;
import pulpcore.math.CoreMath;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;

/**
 *
 * @author Troy Cox
 */
final public class BlasterParticleGroup extends Group {

    CoreImage[] images;
    int lastX, lastY;
    Scene2D scene;

    /**
     *
     * @param scene2D #the current scene


     */
    public BlasterParticleGroup(Scene2D scene2D) {
        images = CoreImage.load("BlasterParticles.png").split(6, 1);

        enabled.set(false);
        setBlendMode(BlendMode.Add());
        scene = scene2D;
        scene.addLayer(this);
        this.moveDown(this);

    }

    @Override
    public void update(int elapsedTime) {
        super.update(elapsedTime);
    }

    /**
     *
     * @param x1 #start
     * @param y1 #start
     * @param x2 #end
     * @param y2 #end
     */
    public void MakeParticles(int x1, int y1, int x2, int y2) {

        Timeline timeline = new Timeline();

        for (int i = 0; i < 80; i++) {
            int size = CoreMath.rand(10, 40 - (int)(i*.6));
            int duration = (30) * 6;
            int moveDistance = CoreMath.rand(4,10);
            //double moveDirection = CoreMath.rand(0, 2 * Math.PI);

            int startX = x1 + i * (x2 - x1) / 80;
            int startY = y1 + i * (y2 - y1) / 80;
            int goalX = startX + (moveDistance);// * Math.cos(moveDirection));
            int goalY = startY + (moveDistance);// * Math.sin(moveDirection));
            //double startAngle = CoreMath.rand(0, .1 * Math.PI);
            //double endAngle = startAngle + CoreMath.rand(-.1 * Math.PI, .1 * Math.PI);

            CoreImage image = images[CoreMath.rand(images.length - 1)];
            Sprite sprite = new ImageSprite(image, startX, startY);
            sprite.setAnchor(.5,.5);
            sprite.setSize(size, size);
            add(sprite);

            timeline.animateTo(sprite.x, goalX, duration, Easing.REGULAR_OUT);
            timeline.animateTo(sprite.y, goalY, duration, Easing.REGULAR_OUT);
            //timeline.animate(sprite.angle, startAngle, endAngle, duration);
            timeline.at(100).animateTo(sprite.alpha, 0, duration - 100, Easing.REGULAR_OUT);
            timeline.add(new RemoveSpriteEvent(this, sprite, duration));

        }

        scene.addTimeline(timeline);
        ((AsteroidField)getScene2D()).MakeHit(x2,y2);
    }
}
