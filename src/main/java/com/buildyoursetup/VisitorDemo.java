package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.patterns.visitor.*;

public class VisitorDemo {

    public static void main(String[] args) {

        CPU cpu = new CPU(
                "Ryzen 5 7600", "AMD",
                95000, "AM5", 6
        );

        GPU gpu = new GPU(
                "RTX 4060", "NVIDIA",
                180000, 8
        );

        RAM ram = new RAM(
                "Vengeance", "Corsair",
                45000, 32, "DDR5"
        );

        PCBuild pc = new PCBuildBuilder("Gaming PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        ReportVisitor reportVisitor = new ReportVisitor();

        cpu.accept(reportVisitor);
        gpu.accept(reportVisitor);
        ram.accept(reportVisitor);

        System.out.println("=== PC BUILD REPORT ===");
        System.out.println("Build: " + pc.getName());
        System.out.println(reportVisitor.getReport());

        PriceSummaryVisitor priceVisitor =
                new PriceSummaryVisitor();

        cpu.accept(priceVisitor);
        gpu.accept(priceVisitor);
        ram.accept(priceVisitor);

        System.out.println(
                "Total Price: "
                        + priceVisitor.getTotalPrice()
                        + " KZT"
        );
    }
}
