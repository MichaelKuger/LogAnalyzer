package kuger.loganalyzer.ui.environment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import kuger.loganalyzer.LogAnalyzerException;
import kuger.loganalyzer.core.api.*;
import kuger.loganalyzer.external.FileInputFactory;
import kuger.loganalyzer.ssh.SshConfig;
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
        createPipelineContainer("/ui/widgets/pipeline_widget.fxml");

    }

    public void btAddSshPipeline() {
        createPipelineContainer("/ui/widgets/sshSourcePanel.fxml");
    }

    private void createPipelineContainer(String resourcePath) {
        try {
            VBox sourcePanel = new VBox();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourcePath));
            fxmlLoader.setRoot(sourcePanel);
            VBox node = fxmlLoader.load();
            pipelineControllers.add(fxmlLoader.getController());
            boxContainer.getChildren().add(node);
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
//        final ConsoleSink sink = new ConsoleSink();
        final FileSink sink = new FileSink(outputFile);
        Collection<PipelineDefinition> pipelineDefinitions = getPipelines();
//        pipelineDefinitions = new ArrayList<>();
//        SshConfig config = new SshConfig("root", PW!, "36216.vs.webtropia.com", 22);
//        pipelineDefinitions.add(new PipelineDefinition(new ScpInputContainer(config, "/root/config.txt", "yyyy-MM-dd", new InputIdentifier())));
        final Pipeline pipeline = FileInputFactory.fillSink(pipelineDefinitions, sink);
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
        private List<LogStatement> statements = new LinkedList<>();

        @Override
        public void add(LogStatement logStatement) {
            statements.add(logStatement);
        }

        @Override
        public void finished() {
            printAll();
        }

        public synchronized void printAll() {
            Collections.sort(statements);
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
