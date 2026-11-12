package com.buildyoursetup.model;

public class RAM extends Component {

    private final int capacity;
    private final String memoryType;

    public RAM(String name, String brand, double price,
               int capacity, String memoryType) {

        super(name, brand, price);

        if (capacity <= 0) {
            throw new IllegalArgumentException("Invalid RAM capacity");
        }

        if (memoryType == null || memoryType.isBlank()) {
            throw new IllegalArgumentException("Invalid memory type");
        }

        this.capacity = capacity;
        this.memoryType = memoryType;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getMemoryType() {
        return memoryType;
    }
}
