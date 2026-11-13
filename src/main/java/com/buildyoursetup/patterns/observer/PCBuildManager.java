package com.buildyoursetup.patterns.observer;

import com.buildyoursetup.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PCBuildManager {

    private final PCBuild build;

    private final List<BuildObserver> observers =
            new ArrayList<>();

    public PCBuildManager(PCBuild build) {
        this.build = Objects.requireNonNull(
                build, "Build cannot be null"
        );
    }

    public void addObserver(BuildObserver observer) {
        observers.add(Objects.requireNonNull(observer));
    }

    public void removeObserver(BuildObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (BuildObserver observer : List.copyOf(observers)) {
            observer.onBuildChanged(build);
        }
    }

    public void changeCPU(CPU cpu) {
        build.setCpu(Objects.requireNonNull(cpu));
        notifyObservers();
    }

    public void changeGPU(GPU gpu) {
        build.setGpu(Objects.requireNonNull(gpu));
        notifyObservers();
    }

    public void changeRAM(RAM ram) {
        build.setRam(Objects.requireNonNull(ram));
        notifyObservers();
    }

    public PCBuild getBuild() {
        return build;
    }
}
