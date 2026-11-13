package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.visitor.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VisitorTest {

    @Test
    void reportVisitorGeneratesComponentReport() {

        CPU cpu = new CPU(
                "Ryzen 5", "AMD", 95000, "AM5", 6
        );

        GPU gpu = new GPU(
                "RTX 4060", "NVIDIA", 180000, 8
        );

        RAM ram = new RAM(
                "32GB", "Corsair", 45000, 32, "DDR5"
        );

        ReportVisitor visitor = new ReportVisitor();

        cpu.accept(visitor);
        gpu.accept(visitor);
        ram.accept(visitor);

        String report = visitor.getReport();

        assertTrue(report.contains("Ryzen 5"));
        assertTrue(report.contains("RTX 4060"));
        assertTrue(report.contains("DDR5"));
    }

    @Test
    void priceVisitorCalculatesCorrectTotal() {

        CPU cpu = new CPU(
                "Ryzen 5", "AMD", 95000, "AM5", 6
        );

        GPU gpu = new GPU(
                "RTX 4060", "NVIDIA", 180000, 8
        );

        RAM ram = new RAM(
                "32GB", "Corsair", 45000, 32, "DDR5"
        );

        PriceSummaryVisitor visitor =
                new PriceSummaryVisitor();

        cpu.accept(visitor);
        gpu.accept(visitor);
        ram.accept(visitor);

        assertEquals(320000, visitor.getTotalPrice());
    }
}
