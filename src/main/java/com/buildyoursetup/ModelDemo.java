package com.buildyoursetup;

import com.buildyoursetup.model.*;

public class ModelDemo {

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

        PCBuild build = new PCBuild("My Gaming PC");

        build.setCpu(cpu);
        build.setGpu(gpu);
        build.setRam(ram);

        System.out.println("Build: " + build.getName());
        System.out.println("CPU: " + build.getCpu());
        System.out.println("GPU: " + build.getGpu());
        System.out.println("RAM: " + build.getRam());
        System.out.println("Complete: " + build.isComplete());
        System.out.println("Total: " + build.calculateTotalPrice() + " KZT");
    }
}
