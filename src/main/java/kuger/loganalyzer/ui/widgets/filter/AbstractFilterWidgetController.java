package kuger.loganalyzer.ui.widgets.filter;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import kuger.loganalyzer.core.api.Filter;
import kuger.loganalyzer.ui.widgets.ControllerRepository;

public abstract class AbstractFilterWidgetController {

    private String identifier = Long.toString(System.currentTimeMillis());

    protected AbstractFilterWidgetController() {
        ControllerRepository.INSTANCE.addFilterWidgetController(this);
    }

    public abstract Filter createFilter();

    public abstract FilterDto createDto();

    protected abstract TextField getTxName();

    public void bindNamePropertyTo(Property propToBindTo) {
        StringProperty nameProperty = getTxName().textProperty();
        propToBindTo.bind(nameProperty);
    }

    protected String getFiltername() {
       return  getTxName().getText();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void onDragDetected(MouseEvent event) {
        System.out.println("onDragDetected");
        Dragboard dragboard = ((Pane) event.getSource()).startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(getIdentifier());
        dragboard.setContent(content);
        event.consume();
    }
}
