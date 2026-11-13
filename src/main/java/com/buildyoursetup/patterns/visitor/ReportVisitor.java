package com.buildyoursetup.patterns.visitor;

import com.buildyoursetup.model.CPU;
import com.buildyoursetup.model.GPU;
import com.buildyoursetup.model.RAM;

public class ReportVisitor implements ComponentVisitor {

    private final StringBuilder report = new StringBuilder();

    @Override
    public void visit(CPU cpu) {

        report.append("CPU: ")
                .append(cpu.getBrand()).append(" ")
                .append(cpu.getName())
                .append("\nSocket: ")
                .append(cpu.getSocket())
                .append("\nCores: ")
                .append(cpu.getCores())
                .append("\nPrice: ")
                .append(cpu.getPrice())
                .append(" KZT\n\n");
    }

    @Override
    public void visit(GPU gpu) {

        report.append("GPU: ")
                .append(gpu.getBrand()).append(" ")
                .append(gpu.getName())
                .append("\nVRAM: ")
                .append(gpu.getVram())
                .append(" GB")
                .append("\nPrice: ")
                .append(gpu.getPrice())
                .append(" KZT\n\n");
    }

    @Override
    public void visit(RAM ram) {

        report.append("RAM: ")
                .append(ram.getBrand()).append(" ")
                .append(ram.getName())
                .append("\nCapacity: ")
                .append(ram.getCapacity())
                .append(" GB")
                .append("\nType: ")
                .append(ram.getMemoryType())
                .append("\nPrice: ")
                .append(ram.getPrice())
                .append(" KZT\n\n");
    }

    public String getReport() {
        return report.toString();
    }
}
