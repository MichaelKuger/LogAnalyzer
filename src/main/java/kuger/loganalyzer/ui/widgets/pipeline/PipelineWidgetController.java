package kuger.loganalyzer.ui.widgets.pipeline;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import kuger.loganalyzer.core.api.FileInputContainer;
import kuger.loganalyzer.core.api.Filter;
import kuger.loganalyzer.core.api.InputIdentifier;
import kuger.loganalyzer.core.api.PipelineDefinition;
import kuger.loganalyzer.ui.FileChooserBuilder;
import kuger.loganalyzer.ui.config.ApplicationPreferences;
import kuger.loganalyzer.ui.widgets.ControllerRepository;
import kuger.loganalyzer.ui.widgets.filter.AbstractFilterWidgetController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PipelineWidgetController {

    @FXML
    public TextField txTimestampPattern;

    @FXML
    private VBox filterContainer;

    @FXML
    private TextField txFilename;

    private FileChooser fileChooser;
    private File selectedFile;

    private List<String> filtersOnPipeline = new ArrayList<>();

    public void onDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.LINK);
        event.consume();
    }

    public void onDragEntered(DragEvent event) {
        System.out.println("onDragEntered");
        event.consume();
    }

    public void onDragExited(DragEvent event) {
        System.out.println("onDragExited");
        event.consume();
    }

    public void selectFileAction(ActionEvent action) {
        FileChooser chooser = getFileChooser();
        File selectedFile = chooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = getFilePath(selectedFile);
            txFilename.setText(filePath);
            this.selectedFile = selectedFile;
            ApplicationPreferences.INSTANCE.setPipelineFileDefaultPath(filePath);
            action.consume();
        }
    }

    private String getFilePath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    public void onDragDropped(DragEvent event) {
        System.out.println("onDragDropped");
        Dragboard dragboard = event.getDragboard();
        String identifier = dragboard.getString();
        if (filterAlreadyOnPipeline(identifier)) {
            System.out.println("Ignoring identifier \"" + identifier + "\". Already present.");
        } else {
            AbstractFilterWidgetController filterWidgetController = ControllerRepository.INSTANCE.getFilterWidgetController(identifier);
            Label filterLabel = new Label();
            StringProperty nameProperty = filterLabel.textProperty();
            filterWidgetController.bindNamePropertyTo(nameProperty);

            Separator separator = new Separator(Orientation.HORIZONTAL);
            filterContainer.getChildren().addAll(separator, filterLabel);
            filtersOnPipeline.add(identifier);
        }
        event.setDropCompleted(true);
        event.consume();
    }

    public PipelineDefinition createPipeline() {
        List<Filter> filters = filtersOnPipeline.stream()
                .map(ControllerRepository.INSTANCE::getFilterWidgetController)
                .map(AbstractFilterWidgetController::createFilter)
                .collect(Collectors.toList());

        FileInputContainer inputContainer = new FileInputContainer(selectedFile, txTimestampPattern.getText(), new InputIdentifier());
        return new PipelineDefinition(inputContainer, filters.toArray(new Filter[filters.size()]));
    }

    private FileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new FileChooserBuilder().withTitle("Select input file")
                    .withDefaultPath(ApplicationPreferences.PIPELINE_FILE_DEFAULT_PATH)
                    .build();
        }
        return fileChooser;
    }

    private boolean filterAlreadyOnPipeline(String identifier) {
        return filtersOnPipeline.contains(identifier);
    }

    public PipelineWidgetControllerDto createDto() {
        PipelineWidgetControllerDto result = new PipelineWidgetControllerDto();

        filtersOnPipeline.stream()
                .map(ControllerRepository.INSTANCE::getFilterWidgetController)
                .map(AbstractFilterWidgetController::getIdentifier)
                .forEach(result::addFilter);

        if (selectedFile != null) {
            result.setInputFileName(selectedFile.getAbsolutePath());
        }
        result.setTimestampPattern(txTimestampPattern.getText());
        return result;
    }
}
