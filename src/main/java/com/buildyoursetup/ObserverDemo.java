package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.patterns.observer.*;

public class ObserverDemo {

    public static void main(String[] args) {

        // Step 1: Create initial components

        CPU cpu = new CPU(
                "Ryzen 5 7600", "AMD",
                95000, "AM5", 6
        );

        GPU gpu = new GPU(
                "RTX 4060", "NVIDIA",
                180000, 8
        );

        RAM ram = new RAM(
                "Vengeance 32GB", "Corsair",
                45000, 32, "DDR5"
        );

        // Step 2: Build PC using Builder Pattern

        PCBuild build = new PCBuildBuilder("Gaming PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        // Step 3: Create Subject

        PCBuildManager manager = new PCBuildManager(build);

        // Step 4: Create Observers

        PriceObserver priceObserver = new PriceObserver();
        NotificationObserver notificationObserver =
                new NotificationObserver();

        // Step 5: Subscribe Observers

        manager.addObserver(priceObserver);
        manager.addObserver(notificationObserver);

        System.out.println("=== OBSERVER PATTERN DEMO ===");

        System.out.println(
                "Initial price: "
                        + build.calculateTotalPrice() + " KZT"
        );

        // Step 6: Change GPU

        System.out.println("\nChanging GPU...");

        GPU newGPU = new GPU(
                "RTX 4070", "NVIDIA",
                300000, 12
        );

        manager.changeGPU(newGPU);

        // Step 7: Change RAM

        System.out.println("\nChanging RAM...");

        RAM newRAM = new RAM(
                "Vengeance 64GB", "Corsair",
                90000, 64, "DDR5"
        );

        manager.changeRAM(newRAM);

        System.out.println(
                "\nFinal price: "
                        + build.calculateTotalPrice() + " KZT"
        );
    }
}
