package kuger.loganalyzer.ui.widgets.filter;

import filter.regex.RegexFilter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import kuger.loganalyzer.core.api.Filter;
import kuger.loganalyzer.core.api.RegexType;

import java.net.URL;
import java.util.ResourceBundle;

public class RegexFilterWidgetController extends AbstractFilterWidgetController implements Initializable {

    @FXML
    public ChoiceBox typeChoiceBox;

    @FXML
    private TextField txName;

    @FXML
    private TextField txPattern;

    @Override
    public Filter createFilter() {
        RegexType regexType = (RegexType) typeChoiceBox.getValue();
        String pattern = txPattern.getText();
        return new RegexFilter(pattern, regexType);
    }

    @Override
    public FilterDto createDto() {
        String identifier = getIdentifier();
        String filtername = getFiltername();
        RegexType regexType = (RegexType) typeChoiceBox.getValue();
        RegexFilterWidgetControllerDto result = new RegexFilterWidgetControllerDto(identifier, filtername);
        result.setPattern(txPattern.getText());
        result.setRegexType(regexType);
        return result;
    }

    @Override
    protected TextField getTxName() {
        return txName;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeChoiceBox.getItems().addAll(RegexType.INCLUDE, RegexType.EXCLUDE);
        typeChoiceBox.setValue(RegexType.INCLUDE);
    }
}