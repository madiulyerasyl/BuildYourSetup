package com.buildyoursetup.patterns.visitor;

import com.buildyoursetup.model.CPU;
import com.buildyoursetup.model.GPU;
import com.buildyoursetup.model.RAM;

public class PriceSummaryVisitor implements ComponentVisitor {

    private double totalPrice = 0;

    @Override
    public void visit(CPU cpu) {
        totalPrice += cpu.getPrice();
    }

    @Override
    public void visit(GPU gpu) {
        totalPrice += gpu.getPrice();
    }

    @Override
    public void visit(RAM ram) {
        totalPrice += ram.getPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
