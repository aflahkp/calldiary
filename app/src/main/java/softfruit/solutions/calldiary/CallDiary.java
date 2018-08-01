package softfruit.solutions.calldiary;

import android.app.Application;

import softfruit.solutions.calldiary.extra.FontOverride;

public class CallDiary extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontOverride.setDefaultFont(this, "DEFAULT", "font/product_sans_regular.ttf");
        FontOverride.setDefaultFont(this, "MONOSPACE", "font/product_sans_regular.ttf");
        FontOverride.setDefaultFont(this, "SERIF", "font/product_sans_regular.ttf");
        FontOverride.setDefaultFont(this, "SANS_SERIF", "font/product_sans_regular.ttf");
    }

}
