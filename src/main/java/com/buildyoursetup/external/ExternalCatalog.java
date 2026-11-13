package com.buildyoursetup.external;

import java.util.List;

public class ExternalCatalog {

    public List<ExternalProduct> fetchProducts() {

        return List.of(
                new ExternalProduct(
                        "CPU-001",
                        "Ryzen 5 7600",
                        "AMD",
                        95000,
                        "PROCESSOR"
                ),
                new ExternalProduct(
                        "CPU-002",
                        "Ryzen 7 7700",
                        "AMD",
                        145000,
                        "PROCESSOR"
                ),
                new ExternalProduct(
                        "GPU-001",
                        "RTX 4060",
                        "NVIDIA",
                        180000,
                        "GRAPHICS_CARD"
                ),
                new ExternalProduct(
                        "GPU-002",
                        "RTX 4070",
                        "NVIDIA",
                        300000,
                        "GRAPHICS_CARD"
                ),
                new ExternalProduct(
                        "RAM-001",
                        "Vengeance 32GB",
                        "Corsair",
                        45000,
                        "MEMORY"
                ),
                new ExternalProduct(
                        "RAM-002",
                        "Vengeance 64GB",
                        "Corsair",
                        90000,
                        "MEMORY"
                )
        );
    }
}
