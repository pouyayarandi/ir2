import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Created by Pouya on 5/3/20.
 */
public class DataManager {
    public static News[] provideData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("dataset.json"), News[].class);
    }
}
