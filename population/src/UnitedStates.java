import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A class which holds all population data of the United States.
 *
 * Access this through the static class variable {@code current} for Singleton.
 */
public class UnitedStates implements IPopulation, IPopulationCondition<State> {
	/**
	 * Class static variable to be accessed in the application for Singleton.
	 */
	public static UnitedStates current = new UnitedStates(new ArrayList<>());

	private final List<State> states;

	/**p
	 * Construct an object which holds all population data of the United States.
	 *
	 * Should update {@code UnitedStates.current} after this is called immediately.
	 *
	 * @param states list of the states of the United States
	 */
	private UnitedStates(List<State> states) {
		this.states = states;
	}

	/**
	 * Load the population data to {@code UnitedStates.current} for future use.
	 *
	 * @param usData list of the states of the United States
	 */
	public static void load(List<State> usData) {
		UnitedStates.current = new UnitedStates(usData);
	}

	/**
	 * Get the state with the given abbreviation or the full name.
	 *
	 * @param stateAbbrName state abbreviation or the full name
	 * @return {@code State} which matches the condition. {@code null} if not found.
	 */
	public State getState(String stateAbbrName) {
		return states.stream()
				.filter(state -> state.getAbbr().equalsIgnoreCase(stateAbbrName) || state.getName().equalsIgnoreCase(stateAbbrName))
				.findFirst()
				.orElse(null);
	}

	public List<State> getStates() {
		return states;
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
		if (countyState == null) {
			return null;
		}
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
		State stateObj = getState(state);
		if (stateObj == null) {
			return null;
		}

		return stateObj.getCounties()
				.stream()
				.filter(countyObj -> countyObj.getName().equalsIgnoreCase(county))
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
		return states.stream()				
				.mapToInt(State::getPopulation)
				.sum();	
	}

	/**
	 * Get the total population of the filtered states using {@code predicate}.
	 *
	 * @param predicate predicate to filter the states
	 * @return total population of the filtered states
	 */
	@Override
	public int getPopulation(Predicate<? super State> predicate) {
		return states.stream()
				.filter(predicate)
				.mapToInt(State::getPopulation)
				.sum();
	}
}

