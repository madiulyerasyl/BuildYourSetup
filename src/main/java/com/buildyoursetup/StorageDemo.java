package com.buildyoursetup;

import com.buildyoursetup.persistence.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StorageDemo {

    public static void main(String[] args) {

        try {
            BuildStorage storage = new BuildStorage(
                    Path.of("data", "builds.json")
            );

            List<SavedBuild> builds = new ArrayList<>();

            SavedBuild gamingPC = new SavedBuild(
                    "build-001",
                    "Gaming PC 2026",
                    "CPU-001",
                    "GPU-001",
                    "RAM-001",
                    true,
                    false
            );

            SavedBuild performancePC = new SavedBuild(
                    "build-002",
                    "Performance PC",
                    "CPU-002",
                    "GPU-002",
                    "RAM-002",
                    true,
                    true
            );

            builds.add(gamingPC);
            builds.add(performancePC);

            storage.saveBuilds(builds);

            System.out.println("Builds saved successfully!");

            List<SavedBuild> loaded =
                    storage.loadBuilds();

            System.out.println(
                    "Loaded builds: " + loaded.size()
            );

            for (SavedBuild build : loaded) {

                System.out.println(
                        "Name: " + build.getName()
                );

                System.out.println(
                        "CPU ID: " + build.getCpuId()
                );

                System.out.println(
                        "GPU ID: " + build.getGpuId()
                );

                System.out.println(
                        "RAM ID: " + build.getRamId()
                );

                System.out.println(
                        "RGB: " + build.isRgb()
                );

                System.out.println(
                        "Warranty: " + build.isWarranty()
                );

                System.out.println("-------------------");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
