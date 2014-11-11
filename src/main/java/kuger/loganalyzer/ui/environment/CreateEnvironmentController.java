package kuger.loganalyzer.ui.environment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import kuger.loganalyzer.LogAnalyzerException;
import kuger.loganalyzer.core.api.LogStatement;
import kuger.loganalyzer.core.api.Pipeline;
import kuger.loganalyzer.core.api.PipelineDefinition;
import kuger.loganalyzer.core.api.Sink;
import kuger.loganalyzer.external.FileInputFactory;
import kuger.loganalyzer.ui.FileChooserBuilder;
import kuger.loganalyzer.ui.config.ApplicationPreferences;
import kuger.loganalyzer.ui.widgets.filter.AbstractFilterWidgetController;
import kuger.loganalyzer.ui.widgets.pipeline.PipelineWidgetController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CreateEnvironmentController {

    public HBox boxContainer;
    public VBox elementsContainer;
    private FileChooser outputFileChooser;
    private FileChooser setupFileChooser;
    private File outputFile;

    @FXML
    private TextField txOutputFile;

    private final List<PipelineWidgetController> pipelineControllers = new ArrayList<>();
    private final List<AbstractFilterWidgetController> filterControllers = new ArrayList<>();

    public void btAddPipeline() {
        System.out.println("addPipeline");
        Node newBox = createPipelineContainer();
        boxContainer.getChildren().add(newBox);
    }

    private Node createPipelineContainer() {
        try {
            VBox sourcePanel = new VBox();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/widgets/pipeline_widget.fxml"));
            fxmlLoader.setRoot(sourcePanel);
            VBox node = fxmlLoader.load();
            elementsContainer.getChildren().add(node);
            pipelineControllers.add(fxmlLoader.getController());
            return node;
        } catch (IOException e) {
            throw new RuntimeException("Could not load widget.", e);
        }

    }

    public void menuAddTemporalFilterAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/widgets/temporalFilterPanel.fxml"));
            VBox vbox = new VBox();
            fxmlLoader.setRoot(vbox);
            Pane node = fxmlLoader.load();
            elementsContainer.getChildren().add(node);
            filterControllers.add(fxmlLoader.getController());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void menuAddContentFilterAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/widgets/regexFilterPanel.fxml"));
            VBox vbox = new VBox();
            fxmlLoader.setRoot(vbox);
            Pane node = fxmlLoader.load();
            elementsContainer.getChildren().add(node);
            filterControllers.add(fxmlLoader.getController());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Collection<PipelineDefinition> getPipelines() {
        return pipelineControllers.stream()
                .map(PipelineWidgetController::createPipeline)
                .collect(Collectors.toList());
    }

    private void filterAction() {
//        final ConsoleSink consoleSink = new ConsoleSink();
        final FileSink fileSink = new FileSink(outputFile);
        Collection<PipelineDefinition> pipelineDefinitions = getPipelines();
//        final Pipeline pipeline = FileInputFactory.fillSink(pipelineDefinitions, consoleSink, fileSink);
        final Pipeline pipeline = FileInputFactory.fillSink(pipelineDefinitions, fileSink);
        pipeline.start();
    }

    public void btStart() {
        filterAction();
    }

    public void btSetOutputFile() {
        FileChooser chooser = getSinkFileChooser();
        File selectedFile = chooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = getFilePath(selectedFile);
            txOutputFile.setText(filePath);
            this.outputFile = selectedFile;
            ApplicationPreferences.INSTANCE.setPipelineFileDefaultOutputPath(filePath);
        }
    }

    private String getFilePath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    private FileChooser getSinkFileChooser() {
        if (outputFileChooser == null) {
            outputFileChooser = new FileChooserBuilder().withTitle("Select output file").withDefaultPath(ApplicationPreferences.PIPELINE_FILE_DEFAULT_OUTPUT_PATH).build();
        }
        return outputFileChooser;
    }

    private FileChooser getSetupFileChooser() {
        if (setupFileChooser == null) {
            setupFileChooser = new FileChooserBuilder().withTitle("Select file").withDefaultPath(ApplicationPreferences.SETUP_CONFIG_DEFAULT_PATH).build();
        }
        return setupFileChooser;
    }

    public void saveSetup() {
        FileChooser chooser = getSetupFileChooser();
        File configFile = chooser.showOpenDialog(null);
        if (configFile != null) {
            String filePath = getFilePath(configFile);
            ApplicationPreferences.INSTANCE.setSetupConfigDefaultPath(filePath);
            CreateEnvironmentControllerDto environmentDto = new CreateEnvironmentControllerDto();
            pipelineControllers.stream()
                    .map(PipelineWidgetController::createDto)
                    .forEach(environmentDto::addPipeline);
            filterControllers.stream()
                    .map(AbstractFilterWidgetController::createDto)
                    .forEach(environmentDto::addFilter);
            SinkDto sinkDto = new SinkDto();
            sinkDto.setFile(outputFile);
            environmentDto.setSink(sinkDto);
           ConfigFileHandler.save(environmentDto, configFile);
        }
    }

    public void loadSetup() throws LogAnalyzerException {
        FileChooser chooser = getSetupFileChooser();
        File configFile = chooser.showOpenDialog(null);
        if (configFile != null) {
            String filePath = getFilePath(configFile);
            ApplicationPreferences.INSTANCE.setSetupConfigDefaultPath(filePath);
            CreateEnvironmentControllerDto dto = ConfigFileHandler.load(configFile);
            System.out.println(dto);
        }
    }

    protected class ConsoleSink implements Sink {
        private TreeSet<LogStatement> statements = new TreeSet<>();

        @Override
        public synchronized void add(LogStatement logStatement) {
            statements.add(logStatement);
        }

        @Override
        public void finished() {
            printAll();
        }

        public synchronized void printAll() {
            statements.forEach(System.out::println);
        }
    }

    protected class FileSink implements Sink {

        private final File targetFile;
        private List<LogStatement> statements = new LinkedList<>();

        public FileSink(File targetFile) {
            this.targetFile = targetFile;
        }

        @Override
        public synchronized void add(LogStatement statement) {
            statements.add(statement);
        }

        @Override
        public void finished() {
            try {
                System.out.println("Sink finished. Dumping content to " + targetFile);
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFile));
                long start = System.currentTimeMillis();
                Collections.sort(statements);
                long duration = System.currentTimeMillis() - start;
                System.out.println("Collection sort duration: " + duration);
                for (LogStatement statement : statements) {
                    bufferedWriter.write(statement.getMessage());
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
