package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.patterns.decorator.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecoratorTest {

    @Test
    void decoratorAddsExtraCosts() {

        PCBuild pc = new PCBuildBuilder("Test PC")
                .withCPU(new CPU("CPU", "AMD", 95000, "AM5", 6))
                .withGPU(new GPU("GPU", "NVIDIA", 180000, 8))
                .withRAM(new RAM("RAM", "Corsair", 45000, 32, "DDR5"))
                .build();

        BuildOption decorated = new WarrantyDecorator(
                new RGBDecorator(new BasicBuild(pc))
        );

        assertEquals(360000, decorated.getTotalPrice());

        assertTrue(
                decorated.getDescription().contains("RGB Lighting")
        );

        assertTrue(
                decorated.getDescription().contains("Extended Warranty")
        );
    }
    @Test
    void basicBuildWithoutDecorators() {
        PCBuild pc = new PCBuildBuilder("Base PC")
                .withCPU(new CPU("CPU", "AMD", 95000, "AM5", 6))
                .withGPU(new GPU("GPU", "NVIDIA", 180000, 8))
                .withRAM(new RAM("RAM", "Corsair", 45000, 32, "DDR5"))
                .build();

        BuildOption basic = new BasicBuild(pc);

        assertEquals(320000, basic.getTotalPrice());
        assertFalse(basic.getDescription().contains("RGB Lighting"));
        assertFalse(basic.getDescription().contains("Extended Warranty"));
    }

    @Test
    void OrderDoesNotAffectTotalPrice() {
        PCBuild pc = new PCBuildBuilder("Order Test PC")
                .withCPU(new CPU("CPU", "AMD", 95000, "AM5", 6))
                .withGPU(new GPU("GPU", "NVIDIA", 180000, 8))
                .withRAM(new RAM("RAM", "Corsair", 45000, 32, "DDR5"))
                .build();

        BuildOption rgbFirst = new WarrantyDecorator(new RGBDecorator(new BasicBuild(pc)));
        BuildOption warrantyFirst = new RGBDecorator(new WarrantyDecorator(new BasicBuild(pc)));

        assertEquals(rgbFirst.getTotalPrice(), warrantyFirst.getTotalPrice());
        assertEquals(360000, warrantyFirst.getTotalPrice());
    }

}
