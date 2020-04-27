import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PopulationDataParser {
    private static final int IDX_STATE = 0;
    private static final int IDX_COUNTY = 1;
    private static final int IDX_POPULATION = 2;
    private static final int IDX_LAT = 3;
    private static final int IDX_LON = 4;
    private static final int IDX_ZIPS = 5;

    /**
     * Load the US population data to {@code UnitedStates.current}.
     *
     * @param path path of US population data file
     * @param converter converter class to convert state name abbreviation to full name
     * @throws IOException thrown if file not found
     */
    public static void loadUsPopFile(String path, StateNameConverter converter) throws IOException {
        Map<String, List<County>> data = new HashMap<>();  // State abbr and list of counties

        Files.lines(Paths.get(path)).map(line -> line.split(",")).forEach(line -> {
            String stateAbbr = line[IDX_STATE];
            String countyName = line[IDX_COUNTY];
            int population = Integer.parseInt(line[IDX_POPULATION]);
            double latitude = Double.parseDouble(line[IDX_LAT]);
            double longitude = Double.parseDouble(line[IDX_LON]);

            String[] zipcodes;
            if (line.length > 5) {
                zipcodes = line[IDX_ZIPS].split(" ");
            } else {
                zipcodes = new String[] {};
            }

            List<Integer> zips = Arrays.stream(zipcodes)
                    .filter(x -> x.length() > 0)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            List<County> countyList = data.get(stateAbbr);

            if (!data.containsKey(stateAbbr)) {
                countyList = new ArrayList<>();
                data.put(stateAbbr, countyList);
            }

            try {
				countyList.add(new County(countyName, latitude, longitude, population, zips));
			} catch (InvalidCountyNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidLatitudeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidLongitudeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidPopulationCount e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });

        List<State> usData = new ArrayList<>();

        data.keySet().forEach(x -> {
            List<County> counties = data.get(x);
            usData.add(new State(x, converter.getFullName(x), counties));
        });

        UnitedStates.load(usData);
    }
}
