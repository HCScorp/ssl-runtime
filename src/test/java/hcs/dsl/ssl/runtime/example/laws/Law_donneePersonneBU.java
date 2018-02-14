package hcs.dsl.ssl.runtime.example.laws;


import hcs.dsl.ssl.runtime.law.file.Pt;
import hcs.dsl.ssl.runtime.law.file.RawFileLaw;

public class Law_donneePersonneBU extends RawFileLaw<Integer> {
    public Law_donneePersonneBU() {
        super(
                new Pt<>(1518220800, 0),
                new Pt<>(1518221000, 1),
                new Pt<>(1518221200, 2),
                new Pt<>(1518221560, 5),
                new Pt<>(1518222020, 8),
                new Pt<>(1518222420, 4),
                new Pt<>(1518222650, 2),
                new Pt<>(1518222910, 1),
                new Pt<>(1518223280, 0)
        );
    }
}
