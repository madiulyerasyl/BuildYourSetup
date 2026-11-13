package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.patterns.decorator.*;

public class DecoratorDemo {

    public static void main(String[] args) {

        CPU cpu = new CPU(
                "Ryzen 5 7600", "AMD", 95000, "AM5", 6
        );

        GPU gpu = new GPU(
                "RTX 4060", "NVIDIA", 180000, 8
        );

        RAM ram = new RAM(
                "Vengeance", "Corsair", 45000, 32, "DDR5"
        );

        PCBuild pc = new PCBuildBuilder("Gaming PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        BuildOption basic = new BasicBuild(pc);

        BuildOption withRGB = new RGBDecorator(basic);

        BuildOption withRGBAndWarranty =
                new WarrantyDecorator(withRGB);

        System.out.println("=== DECORATOR PATTERN DEMO ===");

        System.out.println(
                "Basic PC: " + basic.getTotalPrice() + " KZT"
        );

        System.out.println(
                "With RGB: " + withRGB.getTotalPrice() + " KZT"
        );

        System.out.println(
                "With RGB + Warranty: "
                        + withRGBAndWarranty.getTotalPrice() + " KZT"
        );

        System.out.println(
                "Description: "
                        + withRGBAndWarranty.getDescription()
        );
    }
}
