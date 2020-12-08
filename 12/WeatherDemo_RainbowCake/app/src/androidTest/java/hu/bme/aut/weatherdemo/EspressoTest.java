package hu.bme.aut.weatherdemo;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import java.io.File;
import java.util.Objects;

public abstract class EspressoTest<T extends Activity> {

    @Rule
    public ActivityScenarioRule<T> activityScenarioRule;

    public EspressoTest(Class<T> activityClass) {
        activityScenarioRule = new ActivityScenarioRule<>(activityClass);
        final MockApplication application = (MockApplication) InstrumentationRegistry.getInstrumentation()
                .getTargetContext()
                .getApplicationContext();
        injectDependencies((AppTestComponent) application.injector);
    }

    @BeforeClass
    public static void beforeClass() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("cities.db");
    }

    @Before
    @CallSuper
    public void setup() {
        clearAppState();
    }

    protected void clearAppState() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        File root = targetContext.getFilesDir().getParentFile();
        String[] sharedPreferencesFileNames = new File(root, "shared_prefs").list();
        if (sharedPreferencesFileNames == null || sharedPreferencesFileNames.length == 0) {
            return;
        }
        for (String fileName : Objects.requireNonNull(sharedPreferencesFileNames)) {
            targetContext
                    .getSharedPreferences(
                            fileName.replace(".xml", ""),
                            Context.MODE_PRIVATE
                    )
                    .edit().clear().commit();
        }
    }

    public abstract void injectDependencies(@NonNull final AppTestComponent appAndroidTestComponent);

}
