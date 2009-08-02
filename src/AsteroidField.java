import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.animation.Easing;
import pulpcore.animation.Fixed;
import pulpcore.animation.Timeline;
import pulpcore.animation.event.RemoveSpriteEvent;
import pulpcore.animation.event.SceneChangeEvent;
import pulpcore.animation.event.TimelineEvent;
import pulpcore.image.BlendMode;
import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;
import pulpcore.math.CoreMath;
import static pulpcore.math.CoreMath.rand;
import pulpcore.scene.Scene2D;
import pulpcore.sound.Sound;
import pulpcore.sprite.*;

/**
 *
 * @author Troy Cox
 */
class AsteroidField extends Scene2D {

    private final boolean fastMachine;
    private Group asteroids;
    private Label info;
    private Label info2;
    private final CoreFont font = CoreFont.load("fps.font.png");
    private ParticleGroup particleGroup;
    private Sound asteroidSound;

    private Group ground;
    int panX = 0;
    int panY = Stage.getHeight();
    int panXOffSet = 0;
    int panYOffSet = 0;
    private ImageSprite backGround;
    private final int highestPan = 160;
    private Gunner gunnerRight;
    private Gunner gunnerLeft;
    private final CoreImage gunRight = CoreImage.load("Gunner.png");
    private final CoreImage gunLeft = gunRight.mirror();
    private BlasterParticleGroup blast;
    private Sound photon;
    private final Fixed photonVolume = new Fixed(.1);
    int gunnerTargetX;
    int gunnerTargetY;
    private Shield shield;
    double shieldHealth = 255;
    private final Sound shieldHit = Sound.load("shieldHit.wav");
    private Building pinkHotel;
    private Building brownHotel;
    private Building greenOrbBuilding;
    private Building deathStarBuilding;

    private ImageSprite hud;
    private final CoreFont points = CoreFont.load("points.font.png");
    private boolean isShot = false;
    private ImageSprite shot;
    private Label displayPoints;
    private int totalPoints;
    private int buildingBonus = 400;
    private int shieldBonus = 200;
    private int bonus = 0;
    private Label displayTotalPoints;
    private final CoreFont totalPointsFont = CoreFont.load("totalPoints.font.png");
    private Label displayBonus;
    private int score = 100;
    private final Sound explode = Sound.load("explode.wav");
    private final CoreImage[] laser = CoreImage.load("particles1.png").split(6, 1);
    private ImageSprite laserSprite = new ImageSprite(laser[1], 0, 0);
    private final Group scoutLaser = new Group();
    private final Group buildings = new Group();
    private boolean photonCanPlay = true;
    private final Timeline game = new Timeline();
    private boolean isPaused = false;
    private boolean doAsteroids = false;
    private boolean doScout = false;
    private Sprite scout1;
    private Sprite scout2;
    private Label totalText;
    private Label bonusText;
    private boolean doTally = false;
    private final Sound scoreSound = Sound.load("score.wav");
    boolean displayingScore = false;
    private HighScoreDisplay display;
    private Button ok;
    private final Fixed scoreVolume = new Fixed(.1);

    /**
     *
     */
    public AsteroidField() {
        this(true,false);
    }

    /**
     *
     * @param fastMachine #true = scrolling background
     * @param showScore #true = show the score
     */
    public AsteroidField(boolean fastMachine, boolean showScore) {
        this.fastMachine = fastMachine;
        if (showScore){
            ShowHighScoreNoInput();
        }
    }


    /**
     *
     */
    @Override
    public void load() {
        backGround = new ImageSprite("AsteroidBackground.png", 0, 0);
        backGround.width.set(Stage.getWidth() + 102);
        backGround.height.set(Stage.getHeight() + 200);
        backGround.setLocation(panX - 51, panY - 680);
        backGround.pixelSnapping.set(true);
        add(backGround);

        Ground cityGround = new Ground(panX, panY, -40);
        ground = new Group();
        ground.add(cityGround);
        add(ground);

        CoreImage[] flame = CoreImage.load("flame.png").split(6, 1);

        pinkHotel = new Building(new ImageSprite("PinkHotel.png", 0, 0).getImage(), panX, panY, +455, -20,this,flame);
        pinkHotel.setAnchor(.5,1);
        buildings.add(pinkHotel);

        brownHotel = new Building(new ImageSprite("BrownHotel.png", 0, 0).getImage(), panX, panY, +340, -18,this,flame);
        brownHotel.setAnchor(.5,1);
        buildings.add(brownHotel);

        greenOrbBuilding = new Building(new ImageSprite("GreenOrb.png", 0, 0).getImage(), panX, panY, +152, -10,this,flame);
        greenOrbBuilding.setAnchor(.5,1);
        buildings.add(greenOrbBuilding);

        deathStarBuilding = new Building(new ImageSprite("DeathStarBuilding.png", 0, 0).getImage(), panX, panY, +240, -10,this,flame);
        deathStarBuilding.setAnchor(.5,1);
        buildings.add(deathStarBuilding);

        add(buildings);

        shield = new Shield(panX, panY);
        shield.alpha.set(255);
        shield.setAnchor(.5,1);
        add(shield);


        asteroidSound = Sound.load("asteroid.wav");
        particleGroup = new ParticleGroup(this, "BrownParticles.png");
        asteroids = new Group();
        add(asteroids);

        Sound sound = Sound.load("sound.wav");
        sound.play();
        blast = new BlasterParticleGroup(this);
        gunnerRight = new Gunner(gunRight, Stage.getWidth(), Stage.getHeight(), blast);
        gunnerRight.invert = true;
        add(gunnerRight);
        gunnerLeft = new Gunner(gunLeft, 0, Stage.getHeight(), blast);
        add(gunnerLeft);
        photon = Sound.load("photon.wav");

        hud = new ImageSprite("HUD.png", 0, 0);
        hud.setBlendMode(BlendMode.Add());
        hud.setAnchor(.5,.5);
        hud.alpha.set(100);
        hud.pixelSnapping.set(true);
        add(hud);

        Stage.setFrameRate(30);
        shot = new ImageSprite("Blast.png", 0, 0);
        shot.setAnchor(.5,.5);
        shot.setPixelLevelChecks(true);
        displayTotalPoints = new Label(totalPointsFont, "0", 200, Stage.getHeight() - 100);
        add(displayTotalPoints);
        displayBonus = new Label(font, "Shield Bonus + Building Bonus: ", 160, Stage.getHeight() - 20);
        add(displayBonus);
        add(scoutLaser);

        //Display the initial start message
        game.addEvent(new TimelineEvent(0) {

            @Override
            public void run() {
                isPaused = true;
                info = new Label(totalPointsFont, "Incoming asteroids.", 100, Stage.getHeight() / 3, 440, 60);
                info2 = new Label(totalPointsFont, "Save the city!!", 150, (Stage.getHeight() / 3) + 75, 340, 60);
                add(info);
                add(info2);
            }
        });
        //Start just asteroids after 5 seconds
        game.addEvent(new TimelineEvent(5000) {

            @Override
            public void run() {
                isPaused = false;
                doAsteroids = true;
                remove(info);
                remove(info2);
            }
        });
        //Tally just asteroids score after 30 seconds
        game.addEvent(new TimelineEvent(35000) {

            @Override
            public void run() {
                game.pause();
                isPaused = true;
                asteroids.removeAll();
                doAsteroids = false;
                totalText = new Label(totalPointsFont, "test1", 100, Stage.getHeight() / 3, 440, 60);
                bonusText = new Label(totalPointsFont, "test2", 150, (Stage.getHeight() / 3) + 75, 340, 60);
                add(totalText);
                add(bonusText);
                doTally = true;
            }
        });
        //ready for scouts
        game.addEvent(new TimelineEvent(35100) {

            @Override
            public void run() {
                remove(totalText);
                remove(bonusText);
                isPaused = true;
                info.setText("Scouts have been spotted.");
                info2.setText("Get Ready!!");
                add(info);
                add(info2);
            }
        });

        //Start just the scouts after Tally
        game.addEvent(new TimelineEvent(37000) {

            @Override
            public void run() {
                doTally = false;
                doScout = true;
                isPaused = false;
                remove(info);
                remove(info2);
            }
        });
        //Tally just the scouts after 30 seconds
        game.addEvent(new TimelineEvent(65000) {

            @Override
            public void run() {
                game.pause();
                isPaused = true;
                doScout = false;
                if (getMainLayer().contains(scout1)) {
                    remove(scout1);
                }
                if (getMainLayer().contains(scout2)) {
                    remove(scout2);
                }
                totalText = new Label(totalPointsFont, "test1", 100, Stage.getHeight() / 3, 440, 60);
                bonusText = new Label(totalPointsFont, "test2", 150, (Stage.getHeight() / 3) + 75, 340, 60);
                add(totalText);
                add(bonusText);
                doTally = true;
            }
        });

        //ready for scouts and asteroids
        game.addEvent(new TimelineEvent(65100) {

            @Override
            public void run() {
                remove(totalText);
                remove(bonusText);
                isPaused = true;
                info.setText("Scouts AND asteroids have be spotted.");
                info2.setText("Save the city!!");
                add(info);
                add(info2);
            }
        });

        //Start both scouts and asteroids after Tally (allow 100 for game to get paused for sure)
        game.addEvent(new TimelineEvent(67000) {

            @Override
            public void run() {
                doTally = false;
                isPaused = false;
                doAsteroids = true;
                doScout = true;
                remove(info);
                remove(info2);
            }
        });

        //Tally the scouts and asteroids after 30 seconds
        game.addEvent(new TimelineEvent(95000) {

            @Override
            public void run() {
                game.pause();
                isPaused = true;
                doAsteroids = false;
                asteroids.removeAll();
                doScout = false;
                if (getMainLayer().contains(scout1)) {
                    remove(scout1);
                }
                if (getMainLayer().contains(scout2)) {
                    remove(scout2);
                }
                totalText = new Label(totalPointsFont, "test1", 100, Stage.getHeight() / 3, 440, 60);
                bonusText = new Label(totalPointsFont, "test2", 150, (Stage.getHeight() / 3) + 75, 340, 60);
                add(totalText);
                add(bonusText);
                doTally = true;
            }
        });

        //Until we get more game, enter high scores
        game.addEvent(new TimelineEvent(96000) {

            @Override
            public void run() {
                ShowHighScoreWithInput();
                remove(totalText);
                remove(bonusText);
            }
        });

        addTimeline(game);

    }

    /**
     *
     * @param elapsedTime #call super.update(elapsedTime)
     */
    @Override
    public void update(int elapsedTime) {
        super.update(elapsedTime);
        shield.alpha.set((int) shieldHealth);
        displayTotalPoints.setText(String.valueOf(totalPoints));
        displayBonus.setText("Shield Bonus + Building Bonus: " + String.valueOf(bonus));

        if (totalText != null) {
            if (doTally) {
                TotalScore();
                totalText.setText("Score: " + String.valueOf(totalPoints));
                bonusText.setText("Building Bonus: " + String.valueOf(bonus));
            }
        }

        if (displayingScore) {
            CheckScoreInput();
        }

        if (Input.isMouseInside() && Input.isMouseMoving() && !isPaused) {
            setCursor(Input.CURSOR_OFF);
            gunnerTargetX = Input.getMouseX();
            gunnerTargetY = Input.getMouseY();
        } else {
            GetKeyBoardMovement();
        }

        if (fastMachine) {
            SetBackgroundPanning();
            backGround.setLocation(panX - 51, panY - 680);
        }

        //target.setLocation(gunnerTargetX, gunnerTargetY);
        hud.setLocation(gunnerTargetX, gunnerTargetY);

        if (Input.isMousePressed() || Input.isPressed(Input.KEY_SPACE)) {
            if (!isPaused) {
                photon.play(photonVolume);
            }
        }

        if (!isPaused && !displayingScore) {

            if (doAsteroids) {
                ManageAsteroids();
            }
            if (shieldHealth < 50) {
                DoBuildingCollisions(deathStarBuilding);
                DoBuildingCollisions(greenOrbBuilding);
                DoBuildingCollisions(brownHotel);
                DoBuildingCollisions(pinkHotel);
            } else {
                DoShieldToAsteroidCollision();
            }

            if (isShot) {
                isShot = false;
                boolean isAsteroidHit = false;
                boolean isScoutHit = false;
                for (int i = 0; i < asteroids.getNumSprites(); i++) {
                    if (shot.intersects(asteroids.get(i))) {
                        ((FallingAnimatedSprite) asteroids.get(i)).killMe = true;
                        asteroidSound.play(new Fixed(.5));
                        String scoreString = String.valueOf(score);
                        scoreString = (score > 100) ? scoreString + " SKILL" : scoreString;
                        DoScore(((FallingAnimatedSprite) asteroids.get(i)).x.get(), ((FallingAnimatedSprite) asteroids.get(i)).y.get(), scoreString, score);
                        isAsteroidHit = true;
                    }
                }
                if (scout1 != null && shot.intersects(scout1)) {
                    explode.play(new Fixed(.2));
                    int scoutScore = 400;
                    DoExplosion(scout1.x.get(), scout1.y.get(), "explode.png");
                    String scoreString = String.valueOf(score + scoutScore);
                    scoreString = (score > 100) ? scoreString + " SKILL" : scoreString;
                    DoScore(scout1.x.get(), scout1.y.get(), scoreString, score + scoutScore);
                    remove(scout1);
                    isScoutHit = true;
                }
                if (scout2 != null && shot.intersects(scout2)) {
                    explode.play(new Fixed(.2));
                    int scoutScore = 400;
                    DoExplosion(scout2.x.get(), scout2.y.get(), "explode.png");
                    String scoreString = String.valueOf(score + scoutScore);
                    scoreString = (score > 100) ? scoreString + " SKILL" : scoreString;
                    DoScore(scout2.x.get(), scout2.y.get(), scoreString, score + scoutScore);
                    remove(scout2);
                    isScoutHit = true;
                }
                score = (isAsteroidHit || isScoutHit) ? score += 50 : 100;
                remove(shot);
            }

            if (doScout && !getMainLayer().contains(scout1)) {
                scout1 = new Scout(-131, rand(10, 100));
                add(scout1);
            }
            if (doScout && !getMainLayer().contains(scout2)) {
                scout2 = new Scout(-131, rand(10, 100));
                add(scout2);
            }
            if (shieldHealth >= 50 && scoutLaser.contains(laserSprite)) {
                DoScoutLaserToShieldCollision();
            }
            if (scoutLaser.getNumSprites() > 200) {
                scoutLaser.removeAll();
            }
        }
    }

    private void CreateOK() {
        ok = Button.createLabeledButton("OK", 580, 410);
        ok.setSize(50, 50);
        ok.setAnchor(.5,.5);
        ok.setImage("OK.png");
        ok.alpha.set(200);
        Group okGroup = new Group();
        okGroup.add(ok);
        addLayer(okGroup);
    }

    private void DisplayScoreList() {
        int count;
        String[] scores;
        display.yourScore.setLocation(320, 40);
        display.yourScore.setText("#1) " + HighScoreManager.GetOnlineCurrentHighScore());
        ScrollPane pane = new ScrollPane(120, 195, 400, 250);
        count = 0;
        String[] rawScores = HighScoreManager.GetOnlineHighScores();
        for (int i = 0; i < rawScores.length - 1; i++) {
            if (rawScores[i] != null) {
                if (rawScores[i].length() != 0) {
                    count++;
                }
            }
        }
        scores = new String[count];
        for (int i = 0; i < rawScores.length - 1; i++) {
            if (rawScores[i] != null) {
                String s = "";
                if (rawScores[i].length() != 0) {
                    int l = 60 - rawScores[i].length() - rawScores[i].indexOf('-') - 1;
                    int place = rawScores[i].indexOf('-');
                    for (int n = 0; n < l; n++) {
                        s += "-";
                    }
                    try {
                        scores[i] = rawScores[i].substring(0, place) + s + rawScores[i].substring(place);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        for (int i = 0; i < scores.length - 1; i++) {
            Label label = new Label(points, "#" + String.valueOf(i + 1) + ") " + scores[i], 1, i * 30);
            label.setAnchor(0,0);
            label.setSize(375, 27);
            pane.add(label);
        }
        addLayer(pane);
    }

    private void ShowHighScoreWithInput() {
        game.pause();
        isPaused = true;
        display = new HighScoreDisplay(totalPoints, AsteroidField.this, true);
        displayingScore = true;
        setCursor(Input.CURSOR_DEFAULT);
    }

    private void ShowHighScoreNoInput() {
        game.pause();
        isPaused = true;
        display = new HighScoreDisplay(totalPoints, AsteroidField.this, false);
        displayingScore = true;
        setCursor(Input.CURSOR_DEFAULT);
        DisplayScoreList();
        CreateOK();
    }

    private void CheckScoreInput() {
        if (display.scoreBoard.isMouseHover()) {
            setCursor(Input.CURSOR_DEFAULT);
        }
        //CoreFont sysFont = CoreFont.getSystemFont().tint(GREEN);
        if (display.okButton.isClicked()) {
            display.form.remove(display.label);
            display.form.remove(display.textField);
            display.form.remove(display.okButton);
            display.form.remove(display.textBackground);
            String name = (display.textField.getText().length() > 40) ? display.textField.getText().substring(0, 40) : display.textField.getText();
            HighScoreManager.UpdateOnlineScore(name, totalPoints);
            DisplayScoreList();
            CreateOK();
        }
        if (Input.isPressed(Input.KEY_TAB)) {
            if (display.textField.hasFocus()) {
                display.textField.setFocus(false);
            } else {
                display.textField.setFocus(true);
            }
        }
        if (ok != null) {
            if (ok.isMouseOver()) {
                ok.setImage("OK.png");
                ok.alpha.animateTo(255, 0);
            } else {
                ok.setImage("OK.png");
                ok.alpha.animateTo(200, 0);

            }
            if (ok.isClicked()) {
                ok.setImage("OK.png");
                ok.alpha.animateTo(200, 0);
                addEvent(new SceneChangeEvent(new TitleScene(), 300));
            }
        }
    }

    private void TotalScore() {

        if (bonus > 0) {
            scoreSound.play(scoreVolume);
            bonus -= 100;
            totalPoints += 100;
            try {
                wait(scoreSound.getDuration());
            } catch (InterruptedException ignored) {
            }
        } else {
            game.play();
        }
    }

    private void DoExplosion(double x, double y, String file) {
        int duration = 200;
        ImageSprite explosion = new ImageSprite(CoreImage.load(file), 0, 0);
        explosion.setAnchor(.5,.5);
        explosion.width.set(10);
        explosion.height.set(5);
        explosion.x.set(x);
        explosion.y.set(y);
        explosion.alpha.set(255);
        add(explosion);
        Timeline t = new Timeline();
        t.animateTo(explosion.width, 500, duration, Easing.ELASTIC_IN_OUT);
        t.animateTo(explosion.height, 64, duration, Easing.ELASTIC_IN_OUT);
        t.at(duration - 125).animateTo(explosion.alpha, 0, duration - 50, Easing.REGULAR_OUT);
        t.add(new RemoveSpriteEvent(getMainLayer(), explosion, duration + 300));
        addTimeline(t);
    }

    private void DoScore(double x, double y, String scoreString, int score) {
        Timeline timeline = new Timeline();
        displayPoints = new Label(points, scoreString, x, y);
        displayPoints.alpha.set(255);
        add(displayPoints);
        timeline.animateTo(displayPoints.alpha, 0, 1000);
        addTimeline(timeline);
        totalPoints += score;
        shieldBonus = (shieldHealth > 50) ? 200 : 0;
        bonus += buildingBonus + shieldBonus;
    }

    private void DoShieldToAsteroidCollision() {
        for (int i = 0; i < asteroids.getNumSprites(); i++) {
            if (!((FallingAnimatedSprite) asteroids.get(i)).killMe) {
                if (shield.intersects(asteroids.get(i))) {
                    ((FallingAnimatedSprite) asteroids.get(i)).doCollisionChecking = false;
                    ((FallingAnimatedSprite) asteroids.get(i)).setPixelLevelChecks(false);
                    ((FallingAnimatedSprite) asteroids.get(i)).killMe = true;
                    shieldHealth -= 10;
                    shieldHit.play(new Fixed(.2));
                }
            }
        }
    }

    private void DoScoutLaserToShieldCollision() {
        for (int i = 0; i < scoutLaser.getNumSprites(); i++) {
            if (shield.intersects(scoutLaser.get(i))) {
                ParticleGroup scoutLaserHit = new ParticleGroup(this, "particles1.png");
                scoutLaserHit.MakeParticles(scoutLaser.get(i).x.getAsInt(), scoutLaser.get(i).y.getAsInt(), scoutLaser.get(i).x.getAsInt() - rand(1, 3), scoutLaser.get(i).y.getAsInt() - rand(1, 3), 8);
                scoutLaser.removeAll();
                shieldHealth -= 20;
                shieldHit.play(new Fixed(.2));
            }
        }
    }

    private void DoBuildingCollisions(Building building) {
        for (int i = 0; i < asteroids.getNumSprites(); i++) {
            if (building.intersects(asteroids.get(i))) {
                ((FallingAnimatedSprite)(asteroids.get(i))).killMe = true;
                building.hit = true;
                asteroidSound.play(new Fixed(.9));
                DoExplosion(((FallingAnimatedSprite) asteroids.get(i)).x.get(), ((FallingAnimatedSprite) asteroids.get(i)).y.get() + 40, "asteroidExplode.png");
                buildingBonus = (buildingBonus > 0) ? buildingBonus -= 100 : 0;
            }
        }
        for (int i = 0; i < scoutLaser.getNumSprites(); i++) {
            if (building.intersects(scoutLaser.get(i))) {
                building.hit = true;
                explode.play(new Fixed(.4));
                DoExplosion(scoutLaser.get(i).x.get(), scoutLaser.get(i).y.get() + 40, "explode.png");
                ParticleGroup scoutLaserHit = new ParticleGroup(this, "particles1.png");
                scoutLaserHit.MakeParticles(scoutLaser.get(i).x.getAsInt(), scoutLaser.get(i).y.getAsInt(), scoutLaser.get(i).x.getAsInt() - rand(1, 3), scoutLaser.get(i).y.getAsInt() - rand(1, 3), 1);
                scoutLaser.removeAll();
                buildingBonus = (buildingBonus > 0) ? buildingBonus -= 100 : 0;
                break;
            }
        }
    }

    private void GetKeyBoardMovement() {
        gunnerTargetY = (Input.isPressed(Input.KEY_UP) && gunnerTargetY > 5) ? gunnerTargetY - 5 : gunnerTargetY;
        gunnerTargetY = (Input.isPressed(Input.KEY_DOWN) && gunnerTargetY < Stage.getHeight() - 5) ? gunnerTargetY + 5 : gunnerTargetY;
        gunnerTargetY = (Input.isDown(Input.KEY_UP) && gunnerTargetY > 8) ? gunnerTargetY - 8 : gunnerTargetY;
        gunnerTargetY = (Input.isDown(Input.KEY_DOWN) && gunnerTargetY < Stage.getHeight() - 8) ? gunnerTargetY + 8 : gunnerTargetY;
        gunnerTargetX = (Input.isPressed(Input.KEY_LEFT) && gunnerTargetX > 5) ? gunnerTargetX - 5 : gunnerTargetX;
        gunnerTargetX = (Input.isPressed(Input.KEY_RIGHT) && gunnerTargetX < Stage.getWidth() - 5) ? gunnerTargetX + 5 : gunnerTargetX;
        gunnerTargetX = (Input.isDown(Input.KEY_LEFT) && gunnerTargetX > 8) ? gunnerTargetX - 8 : gunnerTargetX;
        gunnerTargetX = (Input.isDown(Input.KEY_RIGHT) && gunnerTargetX < Stage.getWidth() - 8) ? gunnerTargetX + 8 : gunnerTargetX;
    }

    private void ManageAsteroids() {
        if (asteroids.getNumSprites() < 4) {
            FallingAnimatedSprite asteroidSprite = new FallingAnimatedSprite(particleGroup);
            asteroidSprite.Start();
            asteroidSprite.explosionSound = asteroidSound;
            asteroidSprite.soundLevel.set(0.04, 0);
            asteroidSprite.doCollisionChecking = true;
            asteroidSprite.setPixelLevelChecks(true);
            asteroids.add(asteroidSprite);
            asteroids.add(asteroidSprite);
        }
    }

    private void SetBackgroundPanning() {
        panXOffSet = (gunnerTargetX > Stage.getWidth() * .7) ? - 3 : panXOffSet;
        panXOffSet = (gunnerTargetX < Stage.getWidth() * .3) ? 3 : panXOffSet;
        panXOffSet = (panX > 50 && panXOffSet > 0) ? 0 : panXOffSet;
        panXOffSet = (panX < -50 && panXOffSet < 0) ? 0 : panXOffSet;
        panX = panX + panXOffSet;
        panYOffSet = ((gunnerTargetY > Stage.getHeight() * .5) ? - 2 : panYOffSet);
        panYOffSet = ((gunnerTargetY < Stage.getHeight() * .25) ? 2 : panYOffSet);
        panYOffSet = (panY + panYOffSet < Stage.getHeight()) ? 0 : panYOffSet;
        panYOffSet = (panY + panYOffSet > Stage.getHeight() + highestPan) ? 0 : panYOffSet;
        panY = panY + panYOffSet;
    }

 
    /**
     *
     * @param gunnerTargetX #
     * @param gunnerTargetY #
     */
    public void MakeHit(int gunnerTargetX, int gunnerTargetY) {
        shot.setLocation(gunnerTargetX, gunnerTargetY);
        add(shot);
        isShot = true;
    }

    /**
     *
     * @param x #start
     * @param y #start
     * @param angle #trajectory
     */
    public void Laser(int x, int y, double angle) {
        final Timeline t = new Timeline();
        for (int i = 0; i < 8; i++) {
            int duration = 600;
            int moveDistance = CoreMath.rand(100, 300);
            int startX = x + (int) (16 * Math.cos(angle));
            int startY = y + (int) (16 * Math.sin(angle));
            int goalX = (int) (startX + (moveDistance * Math.cos(angle)));
            int goalY = (int) (startY + (moveDistance * Math.sin(angle)));
            double startAngle = CoreMath.rand(0, .1 * Math.PI);
            double endAngle = startAngle + CoreMath.rand(-.1 * Math.PI, .1 * Math.PI);

            CoreImage image = laser[CoreMath.rand(laser.length - 1)];
            laserSprite = new ImageSprite(image, startX, startY);
            laserSprite.setAnchor(.5,.5);
            laserSprite.scaleTo(16, 16, 0);
            laserSprite.alpha.set(100);
            laserSprite.setPixelLevelChecks(true);
            scoutLaser.add(laserSprite);
            if (photonCanPlay) {
                photon.play(new Fixed(.3));
                photonCanPlay = false;
            }
            t.animateTo(laserSprite.x, goalX, duration, Easing.REGULAR_OUT);
            t.animateTo(laserSprite.y, goalY, duration, Easing.REGULAR_OUT);
            t.animate(laserSprite.angle, startAngle, endAngle, duration);
            t.at((int) (duration * .2)).animateTo(laserSprite.alpha, 0, duration - (int) (duration * .2), Easing.REGULAR_OUT);
            t.add(new RemoveSpriteEvent(getMainLayer(), laserSprite, duration));
            t.addEvent(new TimelineEvent(duration) {

                @Override
                public void run() {
                    photonCanPlay = true;
                    removeTimeline(t,false);
                }
            });
        }
        addTimeline(t);
    }
}