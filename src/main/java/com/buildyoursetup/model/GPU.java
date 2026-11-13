package com.buildyoursetup.model;

import com.buildyoursetup.patterns.visitor.ComponentVisitor;

public class GPU extends Component {

    private final int vram;

    public GPU(String name, String brand, double price, int vram) {
        super(name, brand, price);

        if (vram <= 0) {
            throw new IllegalArgumentException("VRAM must be positive");
        }

        this.vram = vram;
    }

    public int getVram() {
        return vram;
    }

    @Override
    public void accept(ComponentVisitor visitor) {
        visitor.visit(this);
    }
}
