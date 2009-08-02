import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 *
 * @author Troy Cox
 */
public class HighScoreManager {
    private static final int numberOfScores = 500;
    private static final String [] outText = new String[numberOfScores];

    /**
     *
     * @param name #
     * @param score #
     */
    public static void UpdateOnlineScore(String name, int score) {

            try {
            // Construct data
            String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("score", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(score) , "UTF-8");

            // Set Connection
            URL Url = new URL("http://getgamesforfree.com/pulpcore/score/score.php?" + data);
            URLConnection conn = Url.openConnection();

            // Update
            conn.getContent();
        } catch (Exception ignored) {
        }
    }

    /**
     *
     * @return #returns the current high score from the server
     */
    public static String GetOnlineCurrentHighScore() {
            try {

            // Set Connection
            Calendar dt = Calendar.getInstance();
            String data = URLEncoder.encode("dt", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(dt.getTimeInMillis()), "UTF-8");
            URL Url = new URL("http://getgamesforfree.com/pulpcore/score/score.php?dt=" + data );
            URLConnection conn = Url.openConnection();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader( conn.getInputStream()));
            String line;
            int i = 0;
            while ((line = rd.readLine()) != null&&i<numberOfScores-1) {
                // Process line...
                outText[i] = line;
                i++;
            }
            rd.close();
        } catch (Exception e) {
            return "Could not connect.";
        }
            return outText[0];
    }

    /**
     *
     * @return #returns all the high scores from the server
     */
    public static String[] GetOnlineHighScores() {
            try {

            // Set Connection
            Calendar dt = Calendar.getInstance();
            String data = URLEncoder.encode("dt", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(dt.getTimeInMillis()), "UTF-8");
            URL Url = new URL("http://getgamesforfree.com/pulpcore/score/score.php?dt=" + data);
            URLConnection conn = Url.openConnection();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader( conn.getInputStream()));
            String line;
            int i = 0;
            while ((line = rd.readLine()) != null&&i<numberOfScores-1) {
                // Process line...
                outText[i] = line;
                i++;
            }
            rd.close();
        } catch (Exception ignored) {
        }
            return outText;
    }

}
