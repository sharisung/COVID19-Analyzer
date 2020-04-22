import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UnitedStates implements IPopulation, IPopulationCondition<State> {
    public static UnitedStates current = new UnitedStates(new ArrayList<>());

    private List<State> states;

    public UnitedStates(List<State> states) {
        this.states = states;
    }

    /**
     * Get the state with the given abbreviation or the full name.
     *
     * @param state state abbreviation or the full name
     * @return {@code State} which matches the condition. {@code null} if not found.
     */
    public State getState(String state) {
        return states.stream()
                .filter(x -> x.getAbbr().equalsIgnoreCase(state) || x.getName().equalsIgnoreCase(state))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the county using county name and state. State name can be either abbreviation of full name.
     *
     * Example: Dane, WI
     *
     * Returns {@code null} if not found or the string is malformed.
     *
     * @param countyState string to get the county (Example: Dane, WI)
     * @return county of the given state and name. {@code null} if not found or {@code countyState} is malformed.
     */
    public County getCounty(String countyState) {
        // TODO: Handle malformed string / not exists
        String[] str = countyState.split(",");
        if (str.length != 2) {
            return null;
        }

        String countyStr = str[0].strip();
        String stateStr = str[1].strip();

        return getCounty(countyStr, stateStr);
    }

    /**
     * Get the county with the given county and state. State name can be either abbreviation of full name.
     *
     * @param county county name
     * @param state state abbreviation or full name
     * @return county of the given state and name. {@code null} if not found.
     */
    public County getCounty(String county, String state) {
        // TODO: Handle malformed string
        State stateObj = getState(state);
        if (stateObj == null) {
            return null;
        }

        return stateObj.getCounties()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(county))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the total population of the United States.
     *
     * @return total population of the United States
     */
    @Override
    public int getPopulation() {
        // TODO: Sum of all population of all states
        return 0;
    }

    /**
     * Get the total population of the filtered states using {@code predicate}.
     *
     * @param predicate predicate to filter the states
     * @return total population of the filtered states
     */
    @Override
    public int getPopulation(Predicate<? super State> predicate) {
        // TODO: Sum of all population of filtered states
        return 0;
    }
}
