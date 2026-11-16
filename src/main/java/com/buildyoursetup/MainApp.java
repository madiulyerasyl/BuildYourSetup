package com.buildyoursetup;

import  com.buildyoursetup.external.ExternalCatalog;
import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.adapter.*;
import com.buildyoursetup.patterns.facade.*;
import com.buildyoursetup.patterns.observer.PCBuildManager;
import com.buildyoursetup.patterns.strategy.*;
import com.buildyoursetup.service.PCRecommendationService;
import com.buildyoursetup.patterns.decorator.*;
import com.buildyoursetup.patterns.visitor.ReportVisitor;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.external.ExternalProduct;
import com.buildyoursetup.persistence.BuildStorage;
import com.buildyoursetup.persistence.SavedBuild;
import java.util.Optional;
import com.buildyoursetup.service.validation.BuildInputValidator;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {

    private final PCValidationFacade facade =
            new PCValidationFacade();

    private final ComboBox<CPU> cpuBox = new ComboBox<>();
    private final ComboBox<GPU> gpuBox = new ComboBox<>();
    private final ComboBox<RAM> ramBox = new ComboBox<>();

    private final Label priceLabel = new Label("0 KZT");
    private final TextArea resultArea = new TextArea();

    private final TextField buildNameField =
            new TextField("My Gaming PC");

    private final CheckBox rgbCheckBox =
            new CheckBox("RGB Lighting (+15 000 KZT)");

    private final CheckBox warrantyCheckBox =
            new CheckBox("Extended Warranty (+25 000 KZT)");

    private PCBuildManager buildManager;
    private PCBuild createdBuild;
    private String loadedBuildId;

    private final BuildStorage storage =
            new BuildStorage(Path.of("data", "builds.json"));

    private final Path storagePath =
            Path.of("data", "builds.json").toAbsolutePath();


    private void loadSavedBuilds() {
        try {
            System.out.println("JSON path: " + storagePath);
            System.out.println(
                    "File exists: " + java.nio.file.Files.exists(storagePath)
            );

            List<SavedBuild> loaded = storage.loadBuilds();

            System.out.println("Loaded builds: " + loaded.size());

            savedBuilds.clear();
            savedBuilds.addAll(loaded);

            savedBuildsList.getItems().setAll(savedBuilds);

        } catch (Exception exception) {
            exception.printStackTrace();

            resultArea.setText(
                    "Loading error: " + exception.getMessage()
            );
        }
    }


    private void loadSelectedBuild() {

        try {
            SavedBuild selected =
                    savedBuildsList.getSelectionModel()
                            .getSelectedItem();

            if (selected == null) {
                resultArea.setText(
                        "Please select a saved build first."
                );
                return;
            }

            // Find saved components by their IDs
            CPU cpu = (CPU) findComponentById(
                    selected.getCpuId()
            );

            GPU gpu = (GPU) findComponentById(
                    selected.getGpuId()
            );

            RAM ram = (RAM) findComponentById(
                    selected.getRamId()
            );

            // Builder Pattern
            PCBuild loadedBuild =
                    new PCBuildBuilder(selected.getName())
                            .withCPU(cpu)
                            .withGPU(gpu)
                            .withRAM(ram)
                            .build();

            // Validate before changing the GUI
            ValidationResult validation =
                    facade.validateBuild(loadedBuild);

            if (!validation.isValid()) {
                resultArea.setText(
                        "Saved build is not valid:\n"
                                + validation
                );
                return;
            }

            // Restore the form
            buildNameField.setText(selected.getName());

            cpuBox.setValue(cpu);
            gpuBox.setValue(gpu);
            ramBox.setValue(ram);
            buildManager.changeCPU(cpu);
            buildManager.changeGPU(gpu);
            buildManager.changeRAM(ram);

            rgbCheckBox.setSelected(selected.isRgb());
            warrantyCheckBox.setSelected(
                    selected.isWarranty()
            );

            // Recalculate price including decorators
            updateTotalPrice();

            createdBuild = loadedBuild;
            loadedBuildId = selected.getId();

            resultArea.setText(
                    "=== BUILD LOADED SUCCESSFULLY ===\n"
                            + "Name: " + selected.getName() + "\n"
                            + "CPU: " + cpu.getName() + "\n"
                            + "GPU: " + gpu.getName() + "\n"
                            + "RAM: " + ram.getName() + "\n"
                            + "RGB Lighting: " + selected.isRgb() + "\n"
                            + "Extended Warranty: "
                            + selected.isWarranty() + "\n"
                            + "Status: LOADED"
            );

        } catch (Exception exception) {

            resultArea.setText(
                    "Error loading build: "
                            + exception.getMessage()
            );
        }
    }

    private void updateBuild() {

        try {
            // Check whether a saved build was loaded
            if (loadedBuildId == null
                    || loadedBuildId.isBlank()) {

                resultArea.setText(
                        "Please load a valid saved build before updating."
                );
                return;
            }

            // Find the original saved record
            SavedBuild original = savedBuilds.stream()
                    .filter(build ->
                            loadedBuildId.equals(build.getId())
                    )
                    .findFirst()
                    .orElse(null);

            if (original == null) {
                resultArea.setText(
                        "Error: Saved build was not found."
                );
                return;
            }

            // Read current GUI values
            String name = buildNameField.getText().trim();

            if (name.isEmpty()) {
                resultArea.setText(
                        "Error: Please enter a build name."
                );
                return;
            }

            CPU cpu = cpuBox.getValue();
            GPU gpu = gpuBox.getValue();
            RAM ram = ramBox.getValue();

            BuildInputValidator.validate(name, cpu, gpu, ram);

            if (cpu == null || gpu == null || ram == null) {
                resultArea.setText(
                        "Error: Please select CPU, GPU and RAM."
                );
                return;
            }

            // Builder Pattern
            PCBuild updatedBuild = new PCBuildBuilder(name)
                    .withCPU(cpu)
                    .withGPU(gpu)
                    .withRAM(ram)
                    .build();

            // Facade Pattern
            ValidationResult validation =
                    facade.validateBuild(updatedBuild);

            if (!validation.isValid()) {
                resultArea.setText(
                        "Cannot update build.\n" + validation
                );
                return;
            }

            // Find component IDs
            String cpuId = findComponentId(
                    "PROCESSOR", cpu.getName(), cpu.getBrand()
            );

            String gpuId = findComponentId(
                    "GRAPHICS_CARD", gpu.getName(), gpu.getBrand()
            );

            String ramId = findComponentId(
                    "MEMORY", ram.getName(), ram.getBrand()
            );

            // Create an updated record with the SAME ID
            SavedBuild updatedRecord = new SavedBuild(
                    original.getId(),
                    name,
                    cpuId,
                    gpuId,
                    ramId,
                    rgbCheckBox.isSelected(),
                    warrantyCheckBox.isSelected()
            );

            // Prepare a new list without modifying the original
            List<SavedBuild> updatedList =
                    new ArrayList<>(savedBuilds);

            int index = updatedList.indexOf(original);

            updatedList.set(index, updatedRecord);

            // Write JSON first
            storage.saveBuilds(updatedList);

            // Update GUI only after successful save
            savedBuilds.clear();
            savedBuilds.addAll(updatedList);

            savedBuildsList.getItems().setAll(savedBuilds);

            savedBuildsList.getSelectionModel()
                    .select(updatedRecord);

            createdBuild = updatedBuild;

            updateTotalPrice();

            resultArea.setText(
                    "=== BUILD UPDATED SUCCESSFULLY ===\n"
                            + "Name: " + name + "\n"
                            + "CPU: " + cpu.getName() + "\n"
                            + "GPU: " + gpu.getName() + "\n"
                            + "RAM: " + ram.getName() + "\n"
                            + "Final Price: "
                            + String.format(
                            "%,.0f",
                            updatedBuild.calculateTotalPrice()
                                    + (rgbCheckBox.isSelected() ? 15000 : 0)
                                    + (warrantyCheckBox.isSelected() ? 25000 : 0)
                    )
                            + " KZT\n"
                            + "Status: UPDATED"
            );

        } catch (Exception exception) {

            resultArea.setText(
                    "Error updating build: "
                            + exception.getMessage()
            );
        }
    }

    private void deleteBuild() {

        try {
            // Get the selected saved build
            SavedBuild selected =
                    savedBuildsList.getSelectionModel()
                            .getSelectedItem();

            if (selected == null) {
                resultArea.setText(
                        "Please select a saved build to delete."
                );
                return;
            }

            // Confirmation dialog
            Alert confirmation = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            confirmation.setTitle("Delete Build");
            confirmation.setHeaderText(
                    "Delete " + selected.getName() + "?"
            );

            confirmation.setContentText(
                    "This action cannot be undone."
            );

            ButtonType deleteButtonType = new ButtonType(
                    "Delete",
                    ButtonBar.ButtonData.OK_DONE
            );

            ButtonType cancelButtonType = new ButtonType(
                    "Cancel",
                    ButtonBar.ButtonData.CANCEL_CLOSE
            );

            confirmation.getButtonTypes().setAll(
                    deleteButtonType,
                    cancelButtonType
            );

            Optional<ButtonType> response =
                    confirmation.showAndWait();

            // Stop if the user cancels or closes the dialog
            if (response.isEmpty()
                    || response.get() != deleteButtonType) {
                return;
            }

            // Create a new list without the selected build
            List<SavedBuild> updatedList =
                    new ArrayList<>(savedBuilds);

            boolean removed = updatedList.removeIf(
                    build -> build == selected
            );

            if (!removed) {
                resultArea.setText(
                        "Error: Saved build was not found."
                );
                return;
            }

            // Save the updated JSON first
            storage.saveBuilds(updatedList);

            // Update the in-memory list
            savedBuilds.clear();
            savedBuilds.addAll(updatedList);

            // Refresh the JavaFX list
            savedBuildsList.getItems().setAll(savedBuilds);

            // Clear the editing reference if necessary
            if (java.util.Objects.equals(
                    selected.getId(), loadedBuildId
            )) {
                loadedBuildId = null;
                createdBuild = null;
            }

            resultArea.setText(
                    "=== BUILD DELETED SUCCESSFULLY ===\n"
                            + "Deleted build: " + selected.getName() + "\n"
                            + "Remaining builds: " + savedBuilds.size()
            );

        } catch (Exception exception) {

            resultArea.setText(
                    "Error deleting build: "
                            + exception.getMessage()
            );
        }
    }


    private void saveBuild() {

        try {
            // Use the current GUI selections, not a stale build
            String name = buildNameField.getText().trim();

            if (name.isEmpty()) {
                resultArea.setText(
                        "Error: Please enter a build name."
                );
                return;
            }

            CPU cpu = cpuBox.getValue();
            GPU gpu = gpuBox.getValue();
            RAM ram = ramBox.getValue();
            BuildInputValidator.validate(name, cpu, gpu, ram);

            if (cpu == null || gpu == null || ram == null) {
                resultArea.setText(
                        "Error: Please select CPU, GPU and RAM."
                );
                return;
            }

            // Builder Pattern
            PCBuild build = new PCBuildBuilder(name)
                    .withCPU(cpu)
                    .withGPU(gpu)
                    .withRAM(ram)
                    .build();

            // Facade Pattern
            ValidationResult validation =
                    facade.validateBuild(build);

            if (!validation.isValid()) {
                resultArea.setText(
                        "Cannot save build.\n" + validation
                );
                return;
            }

            // Find matching products in the external catalog
            String cpuId = findComponentId(
                    "PROCESSOR", cpu.getName(), cpu.getBrand()
            );

            String gpuId = findComponentId(
                    "GRAPHICS_CARD", gpu.getName(), gpu.getBrand()
            );

            String ramId = findComponentId(
                    "MEMORY", ram.getName(), ram.getBrand()
            );

            SavedBuild saved = new SavedBuild(
                    UUID.randomUUID().toString(),
                    name,
                    cpuId,
                    gpuId,
                    ramId,
                    rgbCheckBox.isSelected(),
                    warrantyCheckBox.isSelected()
            );

            // Save a new snapshot; only update the GUI after
            // the JSON write succeeds
            List<SavedBuild> updated =
                    new ArrayList<>(savedBuilds);

            updated.add(saved);

            storage.saveBuilds(updated);

            savedBuilds.clear();
            savedBuilds.addAll(updated);
            savedBuildsList.getItems().setAll(savedBuilds);
            savedBuildsList.getSelectionModel().select(saved);

            createdBuild = build;
            loadedBuildId = null;

            resultArea.setText(
                    "=== BUILD SAVED SUCCESSFULLY ===\n"
                            + "Name: " + name + "\n"
                            + "CPU: " + cpu.getName() + "\n"
                            + "GPU: " + gpu.getName() + "\n"
                            + "RAM: " + ram.getName() + "\n"
                            + "Saved to: data/builds.json"
            );

        } catch (Exception exception) {

            resultArea.setText(
                    "Error saving build: "
                            + exception.getMessage()
            );
        }
    }

    private String findComponentId(
            String category,
            String name,
            String brand
    ) {

        ExternalCatalog catalog = new ExternalCatalog();

        return catalog.fetchProducts()
                .stream()
                .filter(product ->
                        product.getCategory().equals(category)
                                && product.getProductTitle().equals(name)
                                && product.getManufacturer().equals(brand)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Component not found in catalog: "
                                        + brand + " " + name
                        )
                )
                .getProductId();
    }

    private Component findComponentById(String productId) {

        ExternalCatalog externalCatalog = new ExternalCatalog();

        ExternalProduct product = externalCatalog.fetchProducts()
                .stream()
                .filter(item ->
                        item.getProductId().equals(productId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found: " + productId
                        )
                );

        // Use the existing adapter to convert the product
        ComponentCatalog catalog =
                new ExternalCatalogAdapter(externalCatalog);

        return catalog.getComponents()
                .stream()
                .filter(component ->
                        component.getName().equals(
                                product.getProductTitle()
                        )
                                && component.getBrand().equals(
                                product.getManufacturer()
                        )
                                && component.getClass().getSimpleName()
                                .equals(
                                        switch (product.getCategory()) {
                                            case "PROCESSOR" -> "CPU";
                                            case "GRAPHICS_CARD" -> "GPU";
                                            case "MEMORY" -> "RAM";
                                            default -> "";
                                        }
                                )
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Component not found: " + productId
                        )
                );
    }


    private final List<SavedBuild> savedBuilds =
            new ArrayList<>();

    private final ListView<SavedBuild> savedBuildsList =
            new ListView<>();

    private final List<CPU> availableCPUs = new ArrayList<>();
    private final List<GPU> availableGPUs = new ArrayList<>();
    private final List<RAM> availableRAMs = new ArrayList<>();

    private final PCRecommendationService recommendationService =
            new PCRecommendationService(new BudgetStrategy());

    @Override
    public void start(Stage stage) {

        // Adapter: load components from external catalog
        ComponentCatalog catalog =
                new ExternalCatalogAdapter(new ExternalCatalog());

        List<Component> components = catalog.getComponents();

        for (Component component : components) {

            if (component instanceof CPU cpu) {
                availableCPUs.add(cpu);
                cpuBox.getItems().add(cpu);

            } else if (component instanceof GPU gpu) {
                availableGPUs.add(gpu);
                gpuBox.getItems().add(gpu);

            } else if (component instanceof RAM ram) {
                availableRAMs.add(ram);
                ramBox.getItems().add(ram);
            }
        }

        cpuBox.setPromptText("Select CPU");
        gpuBox.setPromptText("Select GPU");
        ramBox.setPromptText("Select RAM");

        cpuBox.setMaxWidth(Double.MAX_VALUE);
        gpuBox.setMaxWidth(Double.MAX_VALUE);
        ramBox.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("BUILD YOUR SETUP");
        title.setStyle(
                "-fx-font-size: 26px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #2563eb;"
        );

        Label subtitle =
                new Label("Configure your perfect PC");

        subtitle.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-text-fill: #64748b;"
        );

        buildNameField.setMaxWidth(Double.MAX_VALUE);

        VBox cpuSection = createSection("CPU", cpuBox);
        VBox gpuSection = createSection("GPU", gpuBox);
        VBox ramSection = createSection("RAM", ramBox);

        priceLabel.setStyle(
                "-fx-font-size: 22px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #16a34a;"
        );

        Label priceTitle = new Label("Total Price:");
        priceTitle.setStyle("-fx-font-weight: bold;");

        HBox priceRow = new HBox(15, priceTitle, priceLabel);

        Button validateButton =
                new Button("Validate Build");

        Button budgetButton = new Button("Budget Build");
        Button performanceButton = new Button("Performance Build");

        budgetButton.setMaxWidth(Double.MAX_VALUE);
        performanceButton.setMaxWidth(Double.MAX_VALUE);

        HBox strategyButtons = new HBox(
                10,
                budgetButton,
                performanceButton
        );

        HBox.setHgrow(budgetButton, Priority.ALWAYS);
        HBox.setHgrow(performanceButton, Priority.ALWAYS);

        validateButton.setMaxWidth(Double.MAX_VALUE);

        validateButton.setStyle(
                "-fx-background-color: #2563eb;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12px;"
        );

        resultArea.setEditable(false);
        resultArea.setPrefRowCount(15);
        resultArea.setWrapText(true);
        resultArea.setPromptText(
                "Validation results will appear here..."
        );

        // Create a mutable PC build for the Observer manager
        PCBuild build = new PCBuild("My Gaming PC");
        buildManager = new PCBuildManager(build);

        budgetButton.setOnAction(event -> {
            recommendationService.setStrategy(new BudgetStrategy());
            applyRecommendation();
        });

        performanceButton.setOnAction(event -> {
            recommendationService.setStrategy(new PerformanceStrategy());
            applyRecommendation();
        });

        // Observer: refresh the displayed price
        buildManager.addObserver(changedBuild ->
                updateTotalPrice()
        );

        // Observer: notify the user about configuration changes
        buildManager.addObserver(changedBuild ->
                resultArea.setText(
                        "Configuration updated.\n"
                                + "Click Validate Build to check it."
                )
        );

        rgbCheckBox.setOnAction(event -> updateTotalPrice());

        warrantyCheckBox.setOnAction(event -> updateTotalPrice());

        cpuBox.setOnAction(event ->
                buildManager.changeCPU(cpuBox.getValue())
        );

        gpuBox.setOnAction(event ->
                buildManager.changeGPU(gpuBox.getValue())
        );

        ramBox.setOnAction(event ->
                buildManager.changeRAM(ramBox.getValue())
        );

        validateButton.setOnAction(event -> {

            try {
                String name = buildNameField.getText().trim();

                if (name.isEmpty()) {
                    resultArea.setText(
                            "Please enter a build name."
                    );
                    return;
                }

                build.setName(name);

                ValidationResult result =
                        facade.validateBuild(build);

                resultArea.setText(
                        "=== VALIDATION RESULT ===\n"
                                + "Build: " + build.getName() + "\n\n"
                                + result
                );

            } catch (Exception exception) {

                resultArea.setText(
                        "Error: " + exception.getMessage()
                );
            }
        });

        VBox root = new VBox(15);

        root.setPadding(new Insets(25));

        Button reportButton = new Button("Generate Report");

        reportButton.setMaxWidth(Double.MAX_VALUE);

        reportButton.setStyle(
                "-fx-background-color: #16a34a;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12px;"
        );

        reportButton.setOnAction(event -> generateReport());



        Button createBuildButton = new Button("Create Build");

        createBuildButton.setMaxWidth(Double.MAX_VALUE);

        createBuildButton.setStyle(
                "-fx-background-color: #7c3aed;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12px;"
        );

        createBuildButton.setOnAction(event -> createBuild());

        Button saveBuildButton = new Button("Save Build");

        saveBuildButton.setMaxWidth(Double.MAX_VALUE);

        saveBuildButton.setStyle(
                "-fx-background-color: #0f766e;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12px;"
        );

        saveBuildButton.setOnAction(event -> saveBuild());

        savedBuildsList.setPrefHeight(150);

        savedBuildsList.setPlaceholder(
                new Label("No saved builds yet.")
        );

        Label savedBuildsTitle =
                new Label("My Saved PC Builds");

        savedBuildsTitle.setStyle(
                "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
        );

        Button loadBuildButton = new Button("Load Build");

        loadBuildButton.setMaxWidth(Double.MAX_VALUE);

        loadBuildButton.setStyle(
                "-fx-background-color: #2563eb;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12px;"
        );

        loadBuildButton.setOnAction(
                event -> loadSelectedBuild()
        );

        Button updateBuildButton = new Button("Update Build");

        updateBuildButton.setMaxWidth(Double.MAX_VALUE);

        updateBuildButton.setStyle(
                "-fx-background-color: #d97706;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12px;"
        );

        updateBuildButton.setOnAction(
                event -> updateBuild()
        );

        Button deleteBuildButton = new Button("Delete Build");

        deleteBuildButton.setMaxWidth(Double.MAX_VALUE);

        deleteBuildButton.setStyle(
                "-fx-background-color: #dc2626;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12px;"
        );

        deleteBuildButton.setOnAction(
                event -> deleteBuild()
        );

        root.getChildren().addAll(
                title,
                subtitle,
                new Label("Build Name:"),
                buildNameField,
                cpuSection,
                gpuSection,
                ramSection,
                new Label("Automatic Recommendation:"),
                strategyButtons,
                new Label("Additional Options:"),
                rgbCheckBox,
                warrantyCheckBox,
                priceRow,
                createBuildButton,
                saveBuildButton,
                validateButton,
                reportButton,
                savedBuildsTitle,
                savedBuildsList,
                loadBuildButton,
                updateBuildButton,
                deleteBuildButton,
                new Label("Validation Result / Report:"),
                resultArea
        );

        root.setStyle("-fx-background-color: #f8fafc;");

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 650, 750);

        stage.setTitle("Build Your Setup - PC Configurator");
        stage.setScene(scene);

        loadSavedBuilds();

        stage.show();
    }

    private void applyRecommendation() {

        try {
            String name = buildNameField.getText().trim();

            if (name.isEmpty()) {
                resultArea.setText("Please enter a build name.");
                return;
            }

            PCBuild recommended = recommendationService.recommend(
                    name,
                    availableCPUs,
                    availableGPUs,
                    availableRAMs
            );

            cpuBox.setValue(recommended.getCpu());
            gpuBox.setValue(recommended.getGpu());
            ramBox.setValue(recommended.getRam());

            resultArea.setText(
                    "Recommended using: "
                            + recommendationService.getCurrentStrategyName()
                            + "\nClick Validate Build to check the configuration."
            );

        } catch (Exception exception) {
            resultArea.setText("Error: " + exception.getMessage());
        }
    }

    private void updateTotalPrice() {

        if (buildManager == null) {
            return;
        }

        PCBuild build = buildManager.getBuild();

        BuildOption decoratedBuild = new BasicBuild(build);

        if (rgbCheckBox.isSelected()) {
            decoratedBuild = new RGBDecorator(decoratedBuild);
        }

        if (warrantyCheckBox.isSelected()) {
            decoratedBuild = new WarrantyDecorator(decoratedBuild);
        }

        priceLabel.setText(
                String.format(
                        "%,.0f KZT",
                        decoratedBuild.getTotalPrice()
                )
        );
    }


    private void generateReport() {

        try {
            String name = buildNameField.getText().trim();

            if (name.isEmpty()) {
                resultArea.setText("Please enter a build name.");
                return;
            }

            PCBuild build = buildManager.getBuild();

            if (!build.isComplete()) {
                resultArea.setText(
                        "Cannot generate report.\n"
                                + "Please select CPU, GPU and RAM."
                );
                return;
            }

            build.setName(name);

            ValidationResult validation =
                    facade.validateBuild(build);

            if (!validation.isValid()) {
                resultArea.setText(
                        "Cannot generate report.\n"
                                + "Please fix the configuration first.\n\n"
                                + validation
                );
                return;
            }

            // Visitor Pattern
            ReportVisitor visitor = new ReportVisitor();

            build.getCpu().accept(visitor);
            build.getGpu().accept(visitor);
            build.getRam().accept(visitor);

            // Decorator Pattern
            BuildOption finalBuild = new BasicBuild(build);

            StringBuilder selectedOptions = new StringBuilder();

            if (rgbCheckBox.isSelected()) {
                finalBuild = new RGBDecorator(finalBuild);
                selectedOptions.append(
                        "RGB Lighting: +15000 KZT\n"
                );
            }

            if (warrantyCheckBox.isSelected()) {
                finalBuild = new WarrantyDecorator(finalBuild);
                selectedOptions.append(
                        "Extended Warranty: +25000 KZT\n"
                );
            }

            if (selectedOptions.length() == 0) {
                selectedOptions.append("None\n");
            }

            String report =
                    "=== PC BUILD REPORT ===\n"
                            + "Build: " + build.getName() + "\n\n"
                            + visitor.getReport()
                            + "=== ADDITIONAL OPTIONS ===\n"
                            + selectedOptions
                            + "\nBase Price: "
                            + String.format(
                            "%,.0f",
                            build.calculateTotalPrice()
                    )
                            + " KZT\n"
                            + "Final Price: "
                            + String.format(
                            "%,.0f",
                            finalBuild.getTotalPrice()
                    )
                            + " KZT\n"
                            + "Validation: PASSED";

            resultArea.setText(report);

        } catch (Exception exception) {
            resultArea.setText(
                    "Error: " + exception.getMessage()
            );
        }
    }


    private void createBuild() {

        try {
            String name = buildNameField.getText().trim();

            if (name.isEmpty()) {
                resultArea.setText(
                        "Error: Please enter a build name."
                );
                return;
            }

            CPU selectedCPU = cpuBox.getValue();
            GPU selectedGPU = gpuBox.getValue();
            RAM selectedRAM = ramBox.getValue();

            if (selectedCPU == null ||
                    selectedGPU == null ||
                    selectedRAM == null) {

                resultArea.setText(
                        "Error: Please select CPU, GPU and RAM."
                );
                return;
            }

            // Builder Pattern
            PCBuild newBuild = new PCBuildBuilder(name)
                    .withCPU(selectedCPU)
                    .withGPU(selectedGPU)
                    .withRAM(selectedRAM)
                    .build();

            // Facade Pattern
            ValidationResult validation =
                    facade.validateBuild(newBuild);

            if (!validation.isValid()) {
                resultArea.setText(
                        "Cannot create build.\n"
                                + validation
                );
                return;
            }

            createdBuild = newBuild;

            resultArea.setText(
                    "=== BUILD CREATED SUCCESSFULLY ===\n"
                            + "Name: " + createdBuild.getName() + "\n"
                            + "CPU: " + createdBuild.getCpu().getName() + "\n"
                            + "GPU: " + createdBuild.getGpu().getName() + "\n"
                            + "RAM: " + createdBuild.getRam().getName() + "\n"
                            + "Base Price: "
                            + String.format(
                            "%,.0f",
                            createdBuild.calculateTotalPrice()
                    )
                            + " KZT\n\n"
                            + "Status: VALID"
            );

        } catch (Exception exception) {

            resultArea.setText(
                    "Error: " + exception.getMessage()
            );
        }
    }


    private VBox createSection(
            String title,
            ComboBox<?> comboBox
    ) {

        Label label = new Label(title);
        label.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
        );

        VBox section = new VBox(6, label, comboBox);
        section.setPadding(new Insets(5, 0, 5, 0));

        return section;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
