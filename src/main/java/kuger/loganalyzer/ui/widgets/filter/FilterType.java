package kuger.loganalyzer.ui.widgets.filter;

public enum FilterType {
    REGEX(RegexFilterWidgetControllerDto.class), TEMPORAL(TemporalFilterWidgetControllerDto.class);

    private final Class<? extends FilterDto> implementation;

    private FilterType(Class<? extends FilterDto> implementation) {
        this.implementation = implementation;
    }

    public Class<? extends FilterDto> getDtoType() {
        return implementation;
    }
}
