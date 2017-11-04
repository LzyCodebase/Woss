import com.briup.util.BIDR;
import com.briup.util.Logger;

import java.util.Collection;

public class ServerStart {

    private WossConfiguration wossConfiguration = null;


    public void start() throws Exception {

        System.out.println("server端开启");

        wossConfiguration = new WossConfiguration();
        wossConfiguration.init();
        Logger logger = wossConfiguration.getLogger();
        logger.info("server端配置模块启动");
        System.out.println("server端配置模块启动");
        WossServer wossServer = (WossServer) wossConfiguration.getServer();
        //wossServer.setConfiguration(wossConfiguration);
        Collection<BIDR> revicer = wossServer.revicer();
        WossDBStore wossDBStore = (WossDBStore) wossConfiguration.getDBStore();
        //wossDBStore.setConfiguration(wossConfiguration);

        logger.info("server端接收到数据");

        System.out.println("server端接收到数据");
        wossDBStore.saveToDB(revicer);
        logger.info("数据入库完成");
        System.out.println("数据入库完成");

    }

}
