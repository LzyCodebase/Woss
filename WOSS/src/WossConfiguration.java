import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.Properties;

public class WossConfiguration implements Configuration {
    private Properties properties = null;
    private WossBackUP wossBackUP  =null;
    private WossLogger wossLogger =null;
    private WossGather wossGather  =null;
    private WossClient wossClient =null;
    private WossServer wossServer =null;
    private WossDBStore wossDBStore =null;

    public void init(){
        properties = new Properties();
        try {
            properties.load(WossConfiguration.class.getResourceAsStream("woss.properties"));
        } catch (IOException e) {
            System.out.println("woss.properties配置文件加载失败，程序终止");
            e.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public Logger getLogger() throws Exception {
        wossLogger = new WossLogger();
        wossLogger.init(properties);
        wossLogger.setConfiguration(this);

        return wossLogger;
    }

    @Override
    public BackUP getBackup() throws Exception {
        wossBackUP = new WossBackUP();
        wossBackUP.init(properties);
        wossBackUP.setConfiguration(this);
        return wossBackUP;
    }

    @Override
    public Gather getGather() throws Exception {
        wossGather = new WossGather();
        wossGather.init(properties);
        wossGather.setConfiguration(this);

        return wossGather;
    }

    @Override
    public Client getClient() throws Exception {
        wossClient = new WossClient();
        wossClient.init(properties);
        wossClient.setConfiguration(this);

        return wossClient;
    }

    @Override
    public Server getServer() throws Exception {
        wossServer = new WossServer();
        wossServer.init(properties);
        wossServer.setConfiguration(this);

        return wossServer;

    }

    @Override
    public DBStore getDBStore()  {
        wossDBStore = new WossDBStore();
        wossDBStore.init(properties);
        wossDBStore.setConfiguration(this);

        return wossDBStore;
    }
}
