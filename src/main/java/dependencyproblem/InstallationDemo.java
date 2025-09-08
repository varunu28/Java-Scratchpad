package dependencyproblem;

import java.util.List;

public class InstallationDemo {

    public static void main(String[] args) {
        DummySoftware s1 = new DummySoftware("A");
        DummySoftware s2 = new DummySoftware("B");
        DummySoftware s3 = new DummySoftware("C");
        DummySoftware s4 = new DummySoftware("D");
        DummySoftware s5 = new DummySoftware("E");
        DummySoftware s6 = new DummySoftware("F");
        DummySoftware s7 = new DummySoftware("G");
        DummySoftware s8 = new DummySoftware("H");

        SoftwareBundle validSoftwareBundle =
            new SoftwareBundle(List.of(new Dependency(s1, s2), new Dependency(s2, s3), new Dependency(s3, s4)));

        SoftwareBundle inValidSoftwareBundle =
            new SoftwareBundle(List.of(new Dependency(s1, s2), new Dependency(s2, s1), new Dependency(s3, s4)));

        System.out.println(CheckForInstallation.verifyInstallation(List.of(
            validSoftwareBundle,
            inValidSoftwareBundle)));
    }
}
