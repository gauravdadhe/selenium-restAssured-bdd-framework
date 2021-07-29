package candidatex.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

public class TestProperties {
    private TestProperties() {
    }

    static final Logger logger = Logger.getLogger(TestProperties.class);

    public static Properties loadProperty() {
        ClassPathResource resource = new ClassPathResource("test.properties");
        Properties p = new Properties();
        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
            p.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            BasicConfigurator.configure();
            logger.debug(e.getMessage());
        }
        return p;
    }
}
