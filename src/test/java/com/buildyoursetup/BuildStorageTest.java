package com.buildyoursetup;

import com.buildyoursetup.persistence.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuildStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void savesAndLoadsBuilds() throws Exception {

        Path file = tempDir.resolve("builds.json");

        BuildStorage storage = new BuildStorage(file);

        SavedBuild build = new SavedBuild(
                "test-001",
                "Test Gaming PC",
                "CPU-001",
                "GPU-001",
                "RAM-001",
                true,
                false
        );

        storage.saveBuilds(List.of(build));

        List<SavedBuild> loaded =
                storage.loadBuilds();

        assertEquals(1, loaded.size());
        assertEquals("Test Gaming PC", loaded.get(0).getName());
        assertEquals("CPU-001", loaded.get(0).getCpuId());
        assertTrue(loaded.get(0).isRgb());
        assertFalse(loaded.get(0).isWarranty());
    }

    @Test
    void returnsEmptyListWhenFileDoesNotExist() throws Exception {

        Path file = tempDir.resolve("missing.json");

        BuildStorage storage = new BuildStorage(file);

        List<SavedBuild> loaded =
                storage.loadBuilds();

        assertTrue(loaded.isEmpty());
    }
}
