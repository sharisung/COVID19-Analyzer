import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainLayout extends LayoutBase {
    private static final String DEFAULT_STATUS_MSG = "Ready";

    // region Data
    private final DataHolder holder;
    // endregion

    // region GUI-related elements
    private final Region growRegion;

    private final CaseSection summaryOverall;
    private final CaseSection summary7dDiff;
    private final CaseSection filteredOverall;
    private final CaseSection filteredPer100K;
    // endregion

    /**
     * Flag to indicate if the export dialog has been called
     * to display to prevent the user from opening multiple export dialogs.
     */
    private boolean exportDialogOpened;

    public MainLayout(Stage stage, String title, int width, int height, DataHolder holder) {
        super(stage, title, width, height, true);

        // Store data
        this.holder = holder;

        // Store & set layout helping GUI elements
        this.growRegion = new Region();
        VBox.setVgrow(growRegion, Priority.ALWAYS);
        HBox.setHgrow(growRegion, Priority.ALWAYS);

        // Data layout
        DailyCaseCounts latestCounts = holder.getDailyCaseStats().getLatest();

        this.summaryOverall = new CaseSection(
                width, "Overall",
                String.valueOf(latestCounts.getConfirmed()), String.valueOf(latestCounts.getFatal()));
        this.summary7dDiff = new CaseSection(width, "7 Days Difference");
        this.filteredOverall = new CaseSection(width, "Overall");
        this.filteredPer100K = new CaseSection(width, "Per 100K residents");
    }

    /**
     * Generate a {@code GridPane} which can fit horizontally to the window
     * with the {@code items} equally distributed in terms of width.
     *
     * @param items GUI elements to be used
     * @return processed {@code GridPane}
     */
    private GridPane generateHGridPane(Node... items) {
        return Utils.generateHGridPane(width, items);
    }

    /**
     * Function to be called when the export button is clicked.
     */
    private void onExportClicked() {
        if (!exportDialogOpened) {
            Stage stage = new Stage();
            stage.setOnHiding(event -> exportDialogOpened = false);

            LayoutBase layoutBase = new ExportPreviewLayout(
                    stage, "Export Preview", 1000, 600, holder.summaryString());

            layoutBase.applyAndShow();
            exportDialogOpened = true;
        }
    }

    /**
     * Bottom part of the main layout.
     *
     * @return GUI element of the layout
     */
    public Pane bottomPart() {
        HBox hBox = new HBox() {{
            getStyleClass().add("section");
        }};

        Label status = new Label(DEFAULT_STATUS_MSG) {{
            setId("status");
        }};
        Button b1_doc = new Button("Filter Syntax Manual") {{
            setId("doc");
            setOnAction(e -> FilterSyntaxDocGUI.documentationPopup(stage).show());
        }};
        Button b2_export = new Button("Export Result") {{
            setId("export");
            setOnAction(e -> onExportClicked());
        }};

        hBox.getChildren().addAll(status, growRegion, b1_doc, b2_export);

        return hBox;
    }

    /**
     * Creates a label for the section title.
     *
     * @param titleText text of the title
     * @return section title label
     */
    private Label sectionTitle(String titleText) {
        Label title = new Label(titleText);
        title.getStyleClass().add("section-title");

        return title;
    }

    /**
     * {@code HBox} containing the data in the summary section.
     */
    public HBox summaryDataSection() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(
                this.summaryOverall.getGuiElement(),
                this.summary7dDiff.getGuiElement()
        );
        hBox.getStyleClass().add("section");

        return hBox;
    }

    /**
     * Summary section.
     */
    public Pane summarySection() {
        VBox main = new VBox() {{
            getStyleClass().add("section");
        }};

        main.getChildren().addAll(
                sectionTitle("Summary"),
                summaryDataSection()
        );

        return main;
    }

    /**
     * {@code HBox} containing the data in the filter section.
     */
    public HBox filterDataSection() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(
                this.filteredOverall.getGuiElement(),
                this.filteredPer100K.getGuiElement()
        );
        hBox.getStyleClass().add("section");

        return hBox;
    }

    /**
     * A {@code Pane} containing the input section of the filter section.
     */
    public Pane filterPrompt() {
        GridPane pane = generateHGridPane(new TextField() {{
            setPromptText("Input filter query...");
        }});
        pane.getStyleClass().add("section");

        return pane;
    }

    /**
     * Filter section.
     *
     * @param data a {@code DataHolder} which holds
     */
    public Pane filterSection(DataHolder data) {
        GridPane gp = generateHGridPane(
                new VBox() {{
                    getStyleClass().add("section");
                    getChildren().addAll(
                            filterDataSection(),
                            ChartMaker.sampleChart()
                    );
                }},
                TableMaker.makeTable(data)
        );
        gp.getStyleClass().add("section");

        return new VBox() {{
            getStyleClass().add("section");
            getChildren().addAll(
                    sectionTitle("Filtered"),
                    filterPrompt(),
                    gp
            );
        }};
    }

    /**
     * Constructed GUI layout.
     */
    public BorderPane layout() {
        BorderPane main = new BorderPane();

        // Main part
        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                summarySection(),
                filterSection(holder)
        );

        // Status bar
        main.setCenter(vBox);
        main.setBottom(bottomPart());

        return main;
    }
}
