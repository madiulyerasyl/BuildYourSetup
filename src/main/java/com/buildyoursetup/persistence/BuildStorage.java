package com.buildyoursetup.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BuildStorage {

    private final Path filePath;
    private final ObjectMapper mapper;

    public BuildStorage(Path filePath) {
        this.filePath = filePath;
        this.mapper = new ObjectMapper();
    }

    public List<SavedBuild> loadBuilds() throws IOException {

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        if (Files.size(filePath) == 0) {
            return new ArrayList<>();
        }

        return mapper.readValue(
                filePath.toFile(),
                new TypeReference<List<SavedBuild>>() {}
        );
    }

    public void saveBuilds(
            List<SavedBuild> builds
    ) throws IOException {

        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(filePath.toFile(), builds);
    }
}
