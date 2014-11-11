package kuger.loganalyzer.ui.widgets.filter;

import filter.temporal.TemporalFilter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import kuger.loganalyzer.core.api.Filter;
import kuger.loganalyzer.core.api.TemporalType;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static java.time.ZoneOffset.UTC;

public class TemporalFilterWidgetController extends AbstractFilterWidgetController implements Initializable {

    @FXML
    private Label timeLabel;

    @FXML
    private Slider hourSlider;

    @FXML
    private Slider minuteSlider;

    @FXML
    private TextField txName;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox temporalTypeChoiceBox;

    private SimpleIntegerProperty roundedHourProperty;
    private SimpleIntegerProperty roundedMinuteProperty;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DoubleProperty hourProperty = hourSlider.valueProperty();
        DoubleProperty minuteProperty = minuteSlider.valueProperty();

        LocalDateTime now = LocalDateTime.now();
        datePicker.setValue(now.toLocalDate());
        hourProperty.set(now.getHour());
        minuteProperty.set(now.getMinute());

        roundedHourProperty = new SimpleIntegerProperty();
        roundedHourProperty.bind(hourProperty);
        roundedMinuteProperty = new SimpleIntegerProperty();
        roundedMinuteProperty.bind(minuteProperty);

        StringExpression concatProperty = Bindings.concat(roundedHourProperty, ":", roundedMinuteProperty);
        timeLabel.textProperty().bind(concatProperty);
        temporalTypeChoiceBox.getItems().addAll(TemporalType.BEFORE, TemporalType.AFTER);
        temporalTypeChoiceBox.setValue(TemporalType.AFTER);
    }

    @Override
    public Filter createFilter() {
        LocalDateTime dateTime = getDateTime();
        TemporalType temporalType = (TemporalType) temporalTypeChoiceBox.getValue();
        return new TemporalFilter(dateTime.toInstant(UTC), temporalType);
    }

    @Override
    public FilterDto createDto() {
        String identifier = getIdentifier();
        String filtername = getFiltername();
        TemporalFilterWidgetControllerDto result = new TemporalFilterWidgetControllerDto(identifier, filtername);
        result.setTemporalType((TemporalType) temporalTypeChoiceBox.getValue());
        result.setDateTime(getDateTime());
        return result;
    }

    private LocalDateTime getDateTime() {
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.of(roundedHourProperty.getValue(), roundedMinuteProperty.getValue());
        return LocalDateTime.of(date, time);
    }

    @Override
    protected TextField getTxName() {
        return txName;
    }
}
