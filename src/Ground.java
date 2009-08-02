


import pulpcore.sprite.ImageSprite;

/**
 *
 * @author Troy Cox
 */
final public class Ground extends ImageSprite {
    int xOff;
    int yOff;

    /**
     *
     * @param x          #

     * @param y          #

     * @param yOff       #
     */
    public Ground(int x, int y, int yOff) {
        super("CityLand.png", x, y);
        this.xOff = 0;
        this.yOff = yOff;
        super.pixelSnapping.set(true);
    }

    @Override
    public void update(int elapsedTime) {
        super.update(elapsedTime);
        x.set(((AsteroidField)getScene2D()).panX + xOff);
        y.set(((AsteroidField)getScene2D()).panY + yOff);

    }


}
