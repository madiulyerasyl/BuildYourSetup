package com.buildyoursetup;

import com.buildyoursetup.persistence.BuildStorage;
import com.buildyoursetup.persistence.SavedBuild;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuildCrudTest {

    @TempDir
    Path tempDir;

    private SavedBuild createTestBuild(
            String id,
            String name
    ) {
        return new SavedBuild(
                id,
                name,
                "CPU-001",
                "GPU-001",
                "RAM-001",
                false,
                false
        );
    }

    // TEST 1: Save multiple builds
    @Test
    void savesMultipleBuilds() throws Exception {

        Path file = tempDir.resolve("builds.json");
        BuildStorage storage = new BuildStorage(file);

        SavedBuild first =
                createTestBuild("id-001", "Budget PC");

        SavedBuild second =
                createTestBuild("id-002", "Gaming PC");

        storage.saveBuilds(List.of(first, second));

        List<SavedBuild> loaded = storage.loadBuilds();

        assertEquals(2, loaded.size());
        assertEquals("Budget PC", loaded.get(0).getName());
        assertEquals("Gaming PC", loaded.get(1).getName());
    }

    // TEST 2: Reload after restart
    @Test
    void reloadsBuildsUsingNewStorageInstance()
            throws Exception {

        Path file = tempDir.resolve("builds.json");

        BuildStorage firstStorage =
                new BuildStorage(file);

        firstStorage.saveBuilds(
                List.of(createTestBuild("id-001", "Saved PC"))
        );

        // Simulates reopening the application
        BuildStorage secondStorage =
                new BuildStorage(file);

        List<SavedBuild> loaded =
                secondStorage.loadBuilds();

        assertEquals(1, loaded.size());
        assertEquals("Saved PC", loaded.get(0).getName());
        assertEquals("id-001", loaded.get(0).getId());
    }

    // TEST 3: Update existing build
    @Test
    void updatesExistingBuildWithoutChangingId()
            throws Exception {

        Path file = tempDir.resolve("builds.json");
        BuildStorage storage = new BuildStorage(file);

        SavedBuild original =
                createTestBuild("id-001", "Original PC");

        storage.saveBuilds(List.of(original));

        List<SavedBuild> builds =
                new ArrayList<>(storage.loadBuilds());

        SavedBuild updated = new SavedBuild(
                "id-001",
                "Updated PC",
                "CPU-001",
                "GPU-002",
                "RAM-001",
                true,
                false
        );

        builds.set(0, updated);
        storage.saveBuilds(builds);

        List<SavedBuild> loaded = storage.loadBuilds();

        assertEquals(1, loaded.size());
        assertEquals("id-001", loaded.get(0).getId());
        assertEquals("Updated PC", loaded.get(0).getName());
        assertEquals("GPU-002", loaded.get(0).getGpuId());
        assertTrue(loaded.get(0).isRgb());
    }

    // TEST 4: Delete existing build
    @Test
    void deletesBuildPermanently() throws Exception {

        Path file = tempDir.resolve("builds.json");
        BuildStorage storage = new BuildStorage(file);

        SavedBuild first =
                createTestBuild("id-001", "First PC");

        SavedBuild second =
                createTestBuild("id-002", "Second PC");

        storage.saveBuilds(List.of(first, second));

        List<SavedBuild> builds =
                new ArrayList<>(storage.loadBuilds());

        builds.removeIf(
                build -> "id-001".equals(build.getId())
        );

        storage.saveBuilds(builds);

        // Reopen the saved file
        BuildStorage reopenedStorage =
                new BuildStorage(file);

        List<SavedBuild> loaded =
                reopenedStorage.loadBuilds();

        assertEquals(1, loaded.size());
        assertEquals("id-002", loaded.get(0).getId());
        assertEquals("Second PC", loaded.get(0).getName());
    }

    // TEST 5: Missing JSON file
    @Test
    void returnsEmptyListWhenFileIsMissing()
            throws Exception {

        Path file = tempDir.resolve("missing.json");

        BuildStorage storage = new BuildStorage(file);

        assertTrue(storage.loadBuilds().isEmpty());
    }

    // TEST 6: Corrupted JSON
    @Test
    void throwsExceptionForCorruptedJson()
            throws Exception {

        Path file = tempDir.resolve("broken.json");

        Files.writeString(file, "{ invalid json");

        BuildStorage storage = new BuildStorage(file);

        assertThrows(
                IOException.class,
                storage::loadBuilds
        );
    }

    // TEST 7: Empty JSON file
    @Test
    void returnsEmptyListForEmptyFile()
            throws Exception {

        Path file = tempDir.resolve("empty.json");

        Files.createFile(file);

        BuildStorage storage = new BuildStorage(file);

        assertTrue(storage.loadBuilds().isEmpty());
    }
}
