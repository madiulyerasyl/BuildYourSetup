package com.buildyoursetup.model;

public class PCBuild {

    private String name;
    private CPU cpu;
    private GPU gpu;
    private RAM ram;

    public PCBuild(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Build name cannot be empty");
        }

        this.name = name;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public GPU getGpu() {
        return gpu;
    }

    public void setGpu(GPU gpu) {
        this.gpu = gpu;
    }

    public RAM getRam() {
        return ram;
    }

    public void setRam(RAM ram) {
        this.ram = ram;
    }

    public double calculateTotalPrice() {
        double total = 0;

        if (cpu != null) {
            total += cpu.getPrice();
        }

        if (gpu != null) {
            total += gpu.getPrice();
        }

        if (ram != null) {
            total += ram.getPrice();
        }

        return total;
    }

    public boolean isComplete() {
        return cpu != null && gpu != null && ram != null;
    }
}
