package kuger.loganalyzer.ui.widgets.filter;

import kuger.loganalyzer.core.api.TemporalType;

import java.time.LocalDateTime;

public class TemporalFilterWidgetControllerDto extends FilterDto {

    private TemporalType temporalType;
    private LocalDateTime dateTime;

    protected TemporalFilterWidgetControllerDto(String identifier, String filtername) {
        super(FilterType.TEMPORAL, identifier, filtername);
    }

    protected void setTemporalType(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    public TemporalType getTemporalType() {
        return temporalType;
    }

    protected void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "TemporalFilterWidgetControllerDto{" +
                "temporalType=" + temporalType +
                ", dateTime=" + dateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TemporalFilterWidgetControllerDto that = (TemporalFilterWidgetControllerDto) o;

        if (dateTime != null ? !dateTime.equals(that.dateTime) : that.dateTime != null) return false;
        if (temporalType != that.temporalType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (temporalType != null ? temporalType.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }
}
