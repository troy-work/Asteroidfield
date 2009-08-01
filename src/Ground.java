


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
     * @param imageAsset #
     * @param x          #
     * @param y          #
     * @param xOff       #
     * @param yOff       #
     */
    public Ground(String imageAsset, int x, int y, int xOff, int yOff) {
        super(imageAsset, x, y);
        this.xOff = xOff;
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
