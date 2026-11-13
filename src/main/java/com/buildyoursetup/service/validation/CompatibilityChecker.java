package com.buildyoursetup.service.validation;

import com.buildyoursetup.model.PCBuild;

public class CompatibilityChecker {

    public boolean isCompatible(PCBuild build) {

        if (build == null || !build.isComplete()) {
            return false;
        }

        String socket = build.getCpu().getSocket();
        String memoryType = build.getRam().getMemoryType();

        boolean supportedSocket =
                socket.equalsIgnoreCase("AM5")
                        || socket.equalsIgnoreCase("LGA1700");

        boolean supportedMemory =
                memoryType.equalsIgnoreCase("DDR4")
                        || memoryType.equalsIgnoreCase("DDR5");

        return supportedSocket && supportedMemory;
    }
}
