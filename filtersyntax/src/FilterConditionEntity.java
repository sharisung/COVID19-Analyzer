/**
 * A class represents a single filter condition.
 */
public class FilterConditionEntity {
    private final FilterParameter parameter;
    private final FilterComparator comparator;
    private final String val;

    /**
     * @param parameter parameter to be compared
     * @param comparator comparing relationship
     * @param val value of the comparing base
     * @throws FilterSyntaxError thrown if the value is considered invalid for the parameter
     */
    public FilterConditionEntity(
            FilterParameter parameter, FilterComparator comparator, String val) throws FilterSyntaxError {
        this.parameter = parameter;
        this.comparator = comparator;
        this.val = val;

        try {
            this.parameter.cast(this.val);
        } catch (Exception e) {
            throw new FilterSyntaxError(
                    FilterSyntaxErrorReason.PARAMETER_VALUE_UNCASTABLE,
                    String.format("Parameter: %s (%s)", this.parameter, this.val));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterConditionEntity that = (FilterConditionEntity) o;

        if (parameter != that.parameter) return false;
        if (comparator != that.comparator) return false;
        return val.equals(that.val);
    }

    @Override
    public int hashCode() {
        int result = parameter.hashCode();
        result += 31 * result + comparator.hashCode();
        result += 31 * result + val.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.parameter.getSyntax(), this.comparator.getSyntax(), this.val);
    }
}
