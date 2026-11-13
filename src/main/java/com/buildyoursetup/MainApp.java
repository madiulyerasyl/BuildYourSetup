package com.buildyoursetup;

import com.buildyoursetup.external.ExternalCatalog;
import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.adapter.*;
import com.buildyoursetup.patterns.facade.*;
import com.buildyoursetup.patterns.observer.PCBuildManager;

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

    private PCBuildManager buildManager;

    @Override
    public void start(Stage stage) {

        // Adapter: load components from external catalog
        ComponentCatalog catalog =
                new ExternalCatalogAdapter(new ExternalCatalog());

        List<Component> components = catalog.getComponents();

        for (Component component : components) {

            if (component instanceof CPU cpu) {
                cpuBox.getItems().add(cpu);
            } else if (component instanceof GPU gpu) {
                gpuBox.getItems().add(gpu);
            } else if (component instanceof RAM ram) {
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

        validateButton.setMaxWidth(Double.MAX_VALUE);

        validateButton.setStyle(
                "-fx-background-color: #2563eb;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12px;"
        );

        resultArea.setEditable(false);
        resultArea.setPrefRowCount(6);
        resultArea.setPromptText(
                "Validation results will appear here..."
        );

        // Create a mutable PC build for the Observer manager
        PCBuild build = new PCBuild("My Gaming PC");
        buildManager = new PCBuildManager(build);

        // Observer: refresh the displayed price
        buildManager.addObserver(changedBuild ->
                priceLabel.setText(
                        String.format(
                                "%,.0f KZT",
                                changedBuild.calculateTotalPrice()
                        )
                )
        );

        // Observer: notify the user about configuration changes
        buildManager.addObserver(changedBuild ->
                resultArea.setText(
                        "Configuration updated.\n"
                                + "Click Validate Build to check it."
                )
        );

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

        root.getChildren().addAll(
                title,
                subtitle,
                new Label("Build Name:"),
                buildNameField,
                cpuSection,
                gpuSection,
                ramSection,
                priceRow,
                validateButton,
                new Label("Validation Result:"),
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
