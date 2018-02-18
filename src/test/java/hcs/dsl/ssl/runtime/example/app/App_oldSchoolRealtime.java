package hcs.dsl.ssl.runtime.example.app;

import hcs.dsl.ssl.runtime.area.AreaInstance;
import hcs.dsl.ssl.runtime.example.areas.Area_bureau;
import hcs.dsl.ssl.runtime.example.areas.Area_parking;
import hcs.dsl.ssl.runtime.example.areas.Area_salleDeClasse;
import hcs.dsl.ssl.runtime.app.App;
import hcs.dsl.ssl.runtime.app.Config;

public class App_oldSchoolRealtime extends App {

    public App_oldSchoolRealtime() {
        super("oldSchoolRealtime",
                new Config(true, null, null, null),

                new AreaInstance(new Area_parking(), "P1"),
                new AreaInstance(new Area_parking(), "P2"),

                new AreaInstance(new Area_salleDeClasse(), "C1"),
                new AreaInstance(new Area_salleDeClasse(), "C2"),
                new AreaInstance(new Area_salleDeClasse(), "C3"),

                new AreaInstance(new Area_bureau(), "B1"),
                new AreaInstance(new Area_bureau(), "B2"),
                new AreaInstance(new Area_bureau(), "B3"),
                new AreaInstance(new Area_bureau(), "B4")
        );
    }
}
