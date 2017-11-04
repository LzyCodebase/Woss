import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

public class WossLogger implements Logger, ConfigurationAWare {

    private org.apache.log4j.Logger logger4j = null;
    @Override
    public void debug(String s) {
        logger4j.debug(s);
    }

    @Override
    public void info(String s) {
        logger4j.info(s);
    }

    @Override
    public void warn(String s) {
        logger4j.warn(s);
    }

    @Override
    public void error(String s) {
        logger4j.error(s);
    }

    @Override
    public void fatal(String s) {
        logger4j.fatal(s);
    }

    @Override
    public void setConfiguration(Configuration configuration) {

    }

    @Override
    public void init(Properties properties) {

        PropertyConfigurator.configure(properties);
        logger4j = org.apache.log4j.Logger.getRootLogger();

    }
}
