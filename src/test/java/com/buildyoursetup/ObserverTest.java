package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.patterns.observer.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {

    @Test
    void changingGPUNotifiesPriceObserver() {

        CPU cpu = new CPU(
                "Ryzen 5", "AMD", 95000, "AM5", 6
        );

        GPU gpu = new GPU(
                "RTX 4060", "NVIDIA", 180000, 8
        );

        RAM ram = new RAM(
                "32GB", "Corsair", 45000, 32, "DDR5"
        );

        PCBuild build = new PCBuildBuilder("Test PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        PCBuildManager manager = new PCBuildManager(build);

        PriceObserver observer = new PriceObserver();

        manager.addObserver(observer);

        GPU newGPU = new GPU(
                "RTX 4070", "NVIDIA", 300000, 12
        );

        manager.changeGPU(newGPU);

        assertEquals(
                440000,
                observer.getLastCalculatedPrice()
        );

        assertEquals(
                "RTX 4070",
                manager.getBuild().getGpu().getName()
        );
    }
}
