package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.patterns.facade.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FacadeTest {

    @Test
    void validBuildPassesValidation() {

        PCBuild pc = new PCBuildBuilder("Test PC")
                .withCPU(new CPU(
                        "Ryzen 5", "AMD", 95000, "AM5", 6
                ))
                .withGPU(new GPU(
                        "RTX 4060", "NVIDIA", 180000, 8
                ))
                .withRAM(new RAM(
                        "32GB", "Corsair", 45000, 32, "DDR5"
                ))
                .build();

        PCValidationFacade facade = new PCValidationFacade();

        ValidationResult result = facade.validateBuild(pc);

        assertTrue(result.isComplete());
        assertTrue(result.isCompatible());
        assertTrue(result.isValid());
        assertEquals(320000, result.getTotalPrice());
    }

    @Test
    void unsupportedMemoryFailsValidation() {

        PCBuild pc = new PCBuildBuilder("Invalid PC")
                .withCPU(new CPU(
                        "Ryzen 5", "AMD", 95000, "AM5", 6
                ))
                .withGPU(new GPU(
                        "RTX 4060", "NVIDIA", 180000, 8
                ))
                .withRAM(new RAM(
                        "Old RAM", "Generic", 10000, 8, "DDR3"
                ))
                .build();

        PCValidationFacade facade = new PCValidationFacade();

        ValidationResult result = facade.validateBuild(pc);

        assertTrue(result.isComplete());
        assertFalse(result.isCompatible());
        assertFalse(result.isValid());
    }
}
