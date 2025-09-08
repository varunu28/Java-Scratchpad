package dependencyproblem;

public record DummySoftware(String softwareName) {

    public void install() {
        System.out.println("Installed software: " + softwareName);
    }
}
