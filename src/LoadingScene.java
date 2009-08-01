

import pulpcore.Build;
import pulpcore.CoreSystem;
import pulpcore.Stage;

/**
 *
 * @author Troy Cox
 */
public class LoadingScene extends pulpcore.scene.LoadingScene {
    
    /**
     *
     */
    public LoadingScene() {
        super("AsteroidField-" + ProjectBuild.VERSION + ".zip" , new TitleScene());
        
        CoreSystem.setTalkBackField("app.name", "AsteroidField");
        CoreSystem.setTalkBackField("app.version", ProjectBuild.VERSION);
        
        Stage.setUncaughtExceptionScene(new UncaughtExceptionScene());
        Stage.invokeOnShutdown(new Runnable() {
            public void run() {
                // Shutdown network connections, DB connections, etc. 
            }
        });
    }
    
    @Override
    public void load() {
        
        // Deter hotlinking
        String[] validHosts = {
            "getgamesforfree.com", "www.getgamesforfree.com",
        };
        if (!Build.DEBUG && !CoreSystem.isValidHost(validHosts)) {
            CoreSystem.showDocument("http://getgamesforfree.com/pulpcore/");
        }
        else {
            // Start loading the zip
            super.load();
        }
    }
}
