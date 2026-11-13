package com.buildyoursetup.patterns.visitor;

import com.buildyoursetup.model.CPU;
import com.buildyoursetup.model.GPU;
import com.buildyoursetup.model.RAM;

public interface ComponentVisitor {

    void visit(CPU cpu);

    void visit(GPU gpu);

    void visit(RAM ram);
}
