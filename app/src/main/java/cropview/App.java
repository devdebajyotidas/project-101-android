package cropview;

import android.app.Application;

/**
 * Created by XTREMSOFT on 5/19/2018.
 */

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
