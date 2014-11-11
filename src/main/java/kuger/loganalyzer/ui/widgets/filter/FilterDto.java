package kuger.loganalyzer.ui.widgets.filter;

public abstract class FilterDto {

    private String identifier;
    private String filtername;
    private FilterType type;

    protected FilterDto(FilterType type, String identifier, String filtername) {
        this.identifier = identifier;
        this.filtername = filtername;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterDto filterDto = (FilterDto) o;

        if (identifier != null ? !identifier.equals(filterDto.identifier) : filterDto.identifier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    public abstract String toString();
}
