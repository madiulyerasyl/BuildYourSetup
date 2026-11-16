package com.buildyoursetup;

import com.buildyoursetup.model.CPU;
import com.buildyoursetup.model.GPU;
import com.buildyoursetup.model.RAM;
import com.buildyoursetup.model.PCBuild;

import com.buildyoursetup.patterns.builder.PCBuildBuilder;

import com.buildyoursetup.patterns.decorator.BasicBuild;
import com.buildyoursetup.patterns.decorator.BuildOption;
import com.buildyoursetup.patterns.decorator.RGBDecorator;
import com.buildyoursetup.patterns.decorator.WarrantyDecorator;

import com.buildyoursetup.patterns.visitor.ReportVisitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BuildIntegrationTest {

    @Test
    void testCompletePCBuildWorkflow() {

        // 1. Create components
        CPU cpu = new CPU(
                "Ryzen 5 7600",
                "AMD",
                120000,
                "AM5",
                6
        );

        GPU gpu = new GPU(
                "RTX 4060",
                "NVIDIA",
                180000,
                8
        );

        RAM ram = new RAM(
                "Vengeance",
                "Corsair",
                35000,
                32,
                "DDR5"
        );

        // 2. Builder Pattern
        PCBuild build = new PCBuildBuilder("Gaming PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        assertTrue(build.isComplete());
        assertEquals("Gaming PC", build.getName());

        // 3. Check base price
        assertEquals(
                335000.0,
                build.calculateTotalPrice(),
                0.001
        );

        // 4. Decorator Pattern
        BuildOption decoratedBuild = new BasicBuild(build);

        decoratedBuild = new RGBDecorator(decoratedBuild);
        decoratedBuild = new WarrantyDecorator(decoratedBuild);

        assertEquals(
                375000.0,
                decoratedBuild.getTotalPrice(),
                0.001
        );

        assertTrue(
                decoratedBuild.getDescription()
                        .contains("RGB Lighting")
        );

        assertTrue(
                decoratedBuild.getDescription()
                        .contains("Extended Warranty")
        );

        // 5. Visitor Pattern
        ReportVisitor visitor = new ReportVisitor();

        build.getCpu().accept(visitor);
        build.getGpu().accept(visitor);
        build.getRam().accept(visitor);

        String report = visitor.getReport();

        // 6. Verify report content
        assertTrue(report.contains("AMD Ryzen 5 7600"));
        assertTrue(report.contains("NVIDIA RTX 4060"));
        assertTrue(report.contains("Corsair Vengeance"));

        assertTrue(report.contains("Socket: AM5"));
        assertTrue(report.contains("VRAM: 8 GB"));
        assertTrue(report.contains("Capacity: 32 GB"));
        assertTrue(report.contains("Type: DDR5"));

        System.out.println("Integration Test Passed!");
        System.out.println("Base Price: "
                + build.calculateTotalPrice() + " KZT");
        System.out.println("Final Price: "
                + decoratedBuild.getTotalPrice() + " KZT");
        System.out.println(report);
    }
}
