package com.buildyoursetup;

import com.buildyoursetup.external.ExternalCatalog;
import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.adapter.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;

import java.util.List;

public class AdapterDemo {

    public static void main(String[] args) {

        ExternalCatalog externalCatalog = new ExternalCatalog();

        ComponentCatalog catalog =
                new ExternalCatalogAdapter(externalCatalog);

        List<Component> components = catalog.getComponents();

        System.out.println("=== ADAPTER PATTERN DEMO ===");

        for (Component component : components) {
            System.out.println(component);
        }

        CPU cpu = (CPU) components.get(0);
        GPU gpu = (GPU) components.get(2);
        RAM ram = (RAM) components.get(4);

        PCBuild pc = new PCBuildBuilder("Adapter Gaming PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        System.out.println("\nBuild: " + pc.getName());
        System.out.println(
                "Total: " + pc.calculateTotalPrice() + " KZT"
        );
    }
}
