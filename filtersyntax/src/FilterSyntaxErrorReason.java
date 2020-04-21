public enum FilterSyntaxErrorReason {
    PARENTHESES_NOT_ALLOWED,

    INCOMPLETE_EXPRESSION,

    PARAMETER_VALUE_UNCASTABLE,
    
    CASE_NUMBER_UNCASTABLE,
    CASE_NUMBER_NEGATIVE,

    CASE_RATE_UNCASTABLE,
    CASE_RATE_NEGATIVE,
    CASE_RATE_INVALID,

    DEATH_RATE_UNCASTABLE,
    DEATH_RATE_NEGATIVE,
    DEATH_RATE_INVALID,

    ZIP_CODE_INVALID,
    ZIP_CODE_UNCASTABLE,

    LATITUDE_OVER_RANGE,
    LATITUDE_UNCASTABLE,

    LONGITUDE_OVER_RANGE,
    LONGITUDE_UNCASTABLE,

    DATE_UNPARSABLE
}
