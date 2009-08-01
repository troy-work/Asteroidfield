import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import pulpcore.animation.event.RemoveSpriteEvent;
import pulpcore.image.CoreImage;
import pulpcore.math.CoreMath;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;
import pulpcore.sprite.Group;


/**
 *
 * @author Troy Cox
 */
public final class Flame {

	private Scene2D scene;
    private CoreImage[] flame;
    /**
     *
     * @param scene #current scene
     * @param flame #image array for flame
     */
    public Flame(Scene2D scene, CoreImage[] flame){
		this.scene = scene;
		this.flame = flame;
	}


    /**
     * 
     * @param x1             #start
     * @param y1             #start
     * @param x2             #end
     * @param y2             #end
     * @param numParticles   #density
     */
    public void MakeFire(int x1, int y1, int x2, int y2, int numParticles) {

        final Timeline backtimeline = new Timeline();

       for (int i = 0; i < numParticles; i++) {
           int size = CoreMath.rand(25, 50);
           int duration = (100 - size) * 10;
           if (duration < 0) {
               duration = 10;
           }
           int moveDistance = CoreMath.rand(70, 150 - size);
           double moveDirection = 180;//CoreMath.rand(0);

           int startX = x1 + i * (x2 - x1) / numParticles;
           int startY = y1 + i * (y2 - y1) / numParticles;
           int goalX = startX + (int) (moveDistance * Math.cos(moveDirection));
           int goalY = startY + (int) (moveDistance * Math.sin(moveDirection));

           CoreImage image = flame[CoreMath.rand(flame.length - 1)];
           Sprite sprite = new ImageSprite(image, startX, startY);
           sprite.setAnchor(.5,.5);

           sprite.setSize(size, size);
           scene.add(sprite);
           scene.getMainLayer().moveToBottom(sprite);
           scene.getMainLayer().moveUp(sprite);
           backtimeline.animateTo(sprite.x, goalX, duration, Easing.REGULAR_OUT);
           backtimeline.animateTo(sprite.y, goalY, duration, Easing.REGULAR_OUT);
           backtimeline.at(100).animateTo(sprite.alpha, 0, duration - 100, Easing.REGULAR_OUT);
           backtimeline.add(new RemoveSpriteEvent(scene.getMainLayer(), sprite, duration));
       }

       scene.addTimeline(backtimeline);

       MakeFrontFire(x1, y1, x2, y2, numParticles);
   }

   /**
    *
    * @param x1             #start
    * @param y1             #start
    * @param x2             #end
    * @param y2             #end
    * @param numParticles   #density
    */
   public void MakeFrontFire(int x1, int y1, int x2, int y2, int numParticles) {

       final Timeline fronttimeline = new Timeline();

       y2 = (int) (y2 - ((y2 - y1) * .8));
       for (int i = 0; i < numParticles; i++) {
           int size = CoreMath.rand(15, 20);
           int duration = (100 - size) * 10;
           int moveDistance = CoreMath.rand(1, 4 * size);
           double moveDirection = 180;//CoreMath.rand(0,5);
           int startX = x1 + i * (x2 - x1) / numParticles;
           int startY = y1 + i * (y2 - y1) / numParticles;
           int goalX = startX + (int) (moveDistance * Math.cos(moveDirection));
           int goalY = startY + (int) (moveDistance * Math.sin(moveDirection));

           CoreImage image = flame[CoreMath.rand(flame.length - 1)];
           Sprite sprite = new ImageSprite(image, startX, startY);
           sprite.setAnchor(.5,.5);

           sprite.setSize(size * 6, size * 2);
           sprite.alpha.set(50);
           scene.add(sprite);
           fronttimeline.animate(sprite.height, sprite.height.get(), sprite.width.get(), duration, Easing.REGULAR_OUT);
           fronttimeline.animateTo(sprite.x, goalX, duration, Easing.REGULAR_OUT);
           fronttimeline.animateTo(sprite.y, goalY, duration, Easing.REGULAR_OUT);
           fronttimeline.at(100).animateTo(sprite.alpha, 0, duration - 100, Easing.REGULAR_OUT);
           fronttimeline.add(new RemoveSpriteEvent(scene.getMainLayer(), sprite, duration));
       }

       scene.addTimeline(fronttimeline);
   }

}
