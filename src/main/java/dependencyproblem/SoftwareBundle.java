package dependencyproblem;

import java.util.List;

public record SoftwareBundle(List<Dependency> dependencies) {
}
