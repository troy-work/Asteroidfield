    import pulpcore.image.CoreFont;
    import pulpcore.image.CoreImage;
    import pulpcore.Input;
    import pulpcore.scene.Scene2D;
    import pulpcore.sprite.Button;
    import pulpcore.sprite.FilledSprite;
    import pulpcore.sprite.Group;
    import pulpcore.sprite.ImageSprite;
    import pulpcore.sprite.Label;
    import pulpcore.sprite.Sprite;
    import pulpcore.sprite.TextField;
    import pulpcore.Stage;
    import static pulpcore.image.Colors.*;

    /**
     *
     * @author Troy Cox
     */
    public class HighScoreDisplay extends Group {

        TextField textField;
        Button okButton;
        Group form;
        int totalPoints;
        Scene2D scene;
        FilledSprite scoreBoard;
        Label yourScore;
        Label label;
        Sprite textBackground;
        private boolean getInput;

        /**
         *
         * @param totalPoints #pints that were just scored
         * @param scene #current scene
         * @param getInput #true = get name input
         */
        public HighScoreDisplay(int totalPoints, Scene2D scene, boolean getInput) {
        this.scene = scene;
        this.totalPoints = totalPoints;
        this.getInput = getInput;
        Initialize();
    }

        /**
         *
         */
        public void Initialize() {
            CoreFont scorefont = CoreFont.load("totalPoints.font.png");
            CoreFont font = CoreFont.getSystemFont().tint(WHITE);

            // Create the form fields
            scoreBoard = new FilledSprite(BLACK);
            scoreBoard.alpha.set(200);
            scoreBoard.width.set(400);
            scoreBoard.height.set(250);
            scoreBoard.setAnchor(.5,.5);
            scoreBoard.setLocation(320, 320);

            yourScore = new Label("Score: " + String.valueOf(totalPoints), 320, 250);
            yourScore.setSize(400, 64);
            yourScore.setFont(scorefont);
            yourScore.setAnchor(.5,.5);

            label = new Label(font, "Name: ", 250, 310);
            label.setAnchor(1,.5);

            textField = new TextField("", 255, 310, 150, font.getHeight());
            textField.setAnchor(0,.5);
            textField.setFocus(true);
            textBackground = createTextFieldBackground(textField);

            CoreImage buttonImage = CoreImage.load("button.png");
            okButton = new Button(buttonImage.split(3), 320, 360);
            okButton.setAnchor(.5,0);
            okButton.setKeyBinding(Input.KEY_ENTER);

            // Add the form fields to a group
            form = new Group(Stage.getWidth() / 2, Stage.getHeight() / 2);
            form.setAnchor(.5,.5);
            form.add(new ImageSprite("background.png", 0, 0));
            form.add(scoreBoard);
            form.add(yourScore);
            if (getInput) {
                form.add(label);
                form.add(textBackground);
                form.add(textField);
                form.add(okButton);
            }
            form.pack();

            scene.addLayer(form);
        }

        /**
         *
         * @param field #the text input field
         * @return #returns the input field with a background image
         */
        public Sprite createTextFieldBackground(TextField field) {
            field.selectionColor.set(rgb(0x1d5ef2));
            ImageSprite background = new ImageSprite("textfield.png", field.x.get()-5, field.y.get());
            background.setAnchor(0,.5);
            return background;
        }

    }
