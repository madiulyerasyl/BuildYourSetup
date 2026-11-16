package com.buildyoursetup.service.validation;

import com.buildyoursetup.model.CPU;
import com.buildyoursetup.model.GPU;
import com.buildyoursetup.model.RAM;

public class BuildInputValidator {

    public static void validate(
            String name,
            CPU cpu,
            GPU gpu,
            RAM ram
    ) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Build name cannot be empty."
            );
        }

        if (name.trim().length() > 60) {
            throw new IllegalArgumentException(
                    "Build name must be 60 characters or less."
            );
        }

        if (cpu == null) {
            throw new IllegalArgumentException(
                    "Please select a CPU."
            );
        }

        if (gpu == null) {
            throw new IllegalArgumentException(
                    "Please select a GPU."
            );
        }

        if (ram == null) {
            throw new IllegalArgumentException(
                    "Please select RAM."
            );
        }
    }
}
