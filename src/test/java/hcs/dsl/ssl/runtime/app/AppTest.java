package hcs.dsl.ssl.runtime.app;

import hcs.dsl.ssl.runtime.example.app.App_oldSchool;
import hcs.dsl.ssl.runtime.example.app.App_oldSchoolRealtime;
import org.junit.Ignore;

public class AppTest {

    @Ignore
    public void execOldSchoolTest() {
        App_oldSchool e = new App_oldSchool();
        e.run();
    }

    @Ignore
    public void execOldSchoolRealtimeTest() {
        App_oldSchoolRealtime e = new App_oldSchoolRealtime();
        e.run();
    }
}
