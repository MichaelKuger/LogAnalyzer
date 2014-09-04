package kuger.loganalyzer.ui.widgets.filter;

import kuger.loganalyzer.core.api.RegexType;

public class RegexFilterWidgetControllerDto extends FilterDto {

    private String pattern;
    private RegexType regexType;

    protected RegexFilterWidgetControllerDto(String identifier, String filtername) {
        super(FilterType.REGEX, identifier, filtername);
    }

    protected void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setRegexType(RegexType regexType) {
        this.regexType = regexType;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RegexFilterWidgetControllerDto that = (RegexFilterWidgetControllerDto) o;

        if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) return false;
        if (regexType != that.regexType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        result = 31 * result + (regexType != null ? regexType.hashCode() : 0);
        return result;
    }
}
