


import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import pulpcore.animation.event.RemoveSpriteEvent;
import pulpcore.animation.event.TimelineEvent;
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
final public class ParticleGroup extends Group {

    final CoreImage[] images;
    final Scene2D scene;

    /**
     *
     * @param scene2D #pass the current scene; typcially 'this'

     * @param particleImage #image for the particle

     */
    public ParticleGroup(Scene2D scene2D, String particleImage) {
        images = CoreImage.load(particleImage).split(6, 1);

        enabled.set(false);
        setBlendMode(BlendMode.Add());
        scene = scene2D;
        scene.addLayer(this);

    }

    @Override
    public synchronized void update(int elapsedTime) {
        super.update(elapsedTime);
    }

    /**
     *
     * @param x1 #startX
     * @param y1 #startY
     * @param x2 #finishX
     * @param y2 #finishY
     * @param numParticles #number of particles to spawn
     */
    public void MakeParticles(int x1, int y1, int x2, int y2, int numParticles) {

        Timeline timeline = new Timeline();

        for (int i = 0; i < numParticles; i++) {
            int size = CoreMath.rand(4, 48);
            int duration = (200 - size) * 6;
            int moveDistance = CoreMath.rand(4, 80 - size);
            double moveDirection = CoreMath.rand(0, 2 * Math.PI);

            int startX = x1 + i * (x2 - x1) / numParticles;
            int startY = y1 + i * (y2 - y1) / numParticles;
            int goalX = startX + (int) (moveDistance * Math.cos(moveDirection));
            int goalY = startY + (int) (moveDistance * Math.sin(moveDirection));
            double startAngle = CoreMath.rand(0, 2 * Math.PI);
            double endAngle = startAngle + CoreMath.rand(-2 * Math.PI, 2 * Math.PI);

            CoreImage image = images[CoreMath.rand(images.length - 1)];
            Sprite sprite = new ImageSprite(image, startX, startY);
            sprite.setAnchor(.5,.5);
            sprite.setSize(size, size);
            add(sprite);

            timeline.animateTo(sprite.x, goalX, duration, Easing.REGULAR_OUT);
            timeline.animateTo(sprite.y, goalY, duration, Easing.REGULAR_OUT);
            timeline.animate(sprite.angle, startAngle, endAngle, duration);
            timeline.at(100).animateTo(sprite.alpha, 0, duration - 100, Easing.REGULAR_OUT);
            timeline.add(new RemoveSpriteEvent(this, sprite, duration));

        }

        scene.addTimeline(timeline);
    }
}
