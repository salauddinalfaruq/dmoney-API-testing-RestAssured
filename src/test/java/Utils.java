import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Utils {

    public static void setCollectionVariable(String key,String value) throws ConfigurationException {
        PropertiesConfiguration configuration = new PropertiesConfiguration("./src/test/resources/config.properties");
        configuration.setProperty(key , value);
        configuration.save();
    }
}
