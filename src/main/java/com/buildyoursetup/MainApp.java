package com.buildyoursetup;

import com.buildyoursetup.external.ExternalCatalog;
import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.adapter.*;
import com.buildyoursetup.patterns.facade.*;
import com.buildyoursetup.patterns.observer.PCBuildManager;
import com.buildyoursetup.patterns.strategy.*;
import com.buildyoursetup.service.PCRecommendationService;
import com.buildyoursetup.patterns.decorator.*;
import com.buildyoursetup.patterns.visitor.ReportVisitor;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;

import java.util.ArrayList;

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
                validateButton,
                reportButton,
                new Label("Validation Result / Report:"),
                resultArea
        );

        root.setStyle("-fx-background-color: #f8fafc;");

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 650, 750);

        stage.setTitle("Build Your Setup - PC Configurator");
        stage.setScene(scene);
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
