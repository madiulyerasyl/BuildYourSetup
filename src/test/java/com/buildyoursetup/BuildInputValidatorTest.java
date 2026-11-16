package com.buildyoursetup;

import com.buildyoursetup.model.CPU;
import com.buildyoursetup.model.GPU;
import com.buildyoursetup.model.RAM;
import com.buildyoursetup.service.validation.BuildInputValidator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BuildInputValidatorTest {

    private final CPU cpu =
            new CPU("Ryzen 5 7600", "AMD",
                    95000, "AM5", 6);

    private final GPU gpu =
            new GPU("RTX 4060", "NVIDIA",
                    180000, 8);

    private final RAM ram =
            new RAM("Vengeance 32GB", "Corsair",
                    45000, 32, "DDR5");

    @Test
    void acceptsValidBuildInput() {

        assertDoesNotThrow(() ->
                BuildInputValidator.validate(
                        "Gaming PC", cpu, gpu, ram
                )
        );
    }

    @Test
    void rejectsEmptyBuildName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> BuildInputValidator.validate(
                        "   ", cpu, gpu, ram
                )
        );
    }

    @Test
    void rejectsTooLongBuildName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> BuildInputValidator.validate(
                        "A".repeat(61), cpu, gpu, ram
                )
        );
    }

    @Test
    void rejectsMissingCPU() {

        assertThrows(
                IllegalArgumentException.class,
                () -> BuildInputValidator.validate(
                        "Gaming PC", null, gpu, ram
                )
        );
    }

    @Test
    void rejectsMissingGPU() {

        assertThrows(
                IllegalArgumentException.class,
                () -> BuildInputValidator.validate(
                        "Gaming PC", cpu, null, ram
                )
        );
    }

    @Test
    void rejectsMissingRAM() {

        assertThrows(
                IllegalArgumentException.class,
                () -> BuildInputValidator.validate(
                        "Gaming PC", cpu, gpu, null
                )
        );
    }
}
