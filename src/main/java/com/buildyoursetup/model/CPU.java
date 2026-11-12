package com.buildyoursetup.model;

public class CPU extends Component {

    private final String socket;
    private final int cores;

    public CPU(String name, String brand, double price,
               String socket, int cores) {

        super(name, brand, price);

        if (socket == null || socket.isBlank() || cores <= 0) {
            throw new IllegalArgumentException("Invalid CPU specifications");
        }

        this.socket = socket;
        this.cores = cores;
    }

    public String getSocket() {
        return socket;
    }

    public int getCores() {
        return cores;
    }
}
