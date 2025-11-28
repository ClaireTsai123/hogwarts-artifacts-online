package com.example.hogwartsartifactsonline.artifact;

public class ArtifactNotFoundException extends RuntimeException {
    public ArtifactNotFoundException(String artifactId) {
        super("Could not found artifact with id: " + artifactId + ".");
    }
}
