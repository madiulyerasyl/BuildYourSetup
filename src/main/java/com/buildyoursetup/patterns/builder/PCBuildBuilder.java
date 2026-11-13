package com.buildyoursetup.patterns.builder;

import com.buildyoursetup.model.CPU;
import com.buildyoursetup.model.GPU;
import com.buildyoursetup.model.RAM;
import com.buildyoursetup.model.PCBuild;

public class PCBuildBuilder {

    private final PCBuild build;

    public PCBuildBuilder(String name) {
        this.build = new PCBuild(name);
    }

    public PCBuildBuilder withCPU(CPU cpu) {
        build.setCpu(cpu);
        return this;
    }

    public PCBuildBuilder withGPU(GPU gpu) {
        build.setGpu(gpu);
        return this;
    }

    public PCBuildBuilder withRAM(RAM ram) {
        build.setRam(ram);
        return this;
    }

    public PCBuild build() {
        if (!build.isComplete()) {
            throw new IllegalStateException(
                    "PC Build must contain CPU, GPU and RAM"
            );
        }

        return build;
    }
}
