package com.buildyoursetup;

import com.buildyoursetup.external.ExternalCatalog;
import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.adapter.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdapterTest {

    @Test
    void adapterConvertsExternalProducts() {

        ComponentCatalog catalog =
                new ExternalCatalogAdapter(new ExternalCatalog());

        List<Component> components = catalog.getComponents();

        assertEquals(6, components.size());

        assertInstanceOf(CPU.class, components.get(0));
        assertInstanceOf(GPU.class, components.get(2));
        assertInstanceOf(RAM.class, components.get(4));

        assertEquals(
                "Ryzen 5 7600",
                components.get(0).getName()
        );

        assertEquals(
                95000,
                components.get(0).getPrice()
        );
    }
}
