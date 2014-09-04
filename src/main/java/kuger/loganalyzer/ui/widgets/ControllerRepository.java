package kuger.loganalyzer.ui.widgets;

import kuger.loganalyzer.ui.widgets.filter.AbstractFilterWidgetController;

import java.util.HashMap;
import java.util.Map;

public class ControllerRepository {

    public static final ControllerRepository INSTANCE = new ControllerRepository();

    private ControllerRepository() {
    }

    private final Map<String, AbstractFilterWidgetController> filterWidgetControllerMap = new HashMap<>();

    public void addFilterWidgetController(AbstractFilterWidgetController controller) {
        filterWidgetControllerMap.put(controller.getIdentifier(), controller);
    }

    public AbstractFilterWidgetController getFilterWidgetController(String identifier) {
        return filterWidgetControllerMap.get(identifier);
    }
}
