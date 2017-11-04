import com.briup.util.BIDR;
import com.briup.util.Logger;
import com.briup.woss.client.Client;

import java.util.Collection;

public class ClientStart {

    private  WossConfiguration wossConfiguration = null;

    public void start() throws Exception {
        System.out.println("client端开启");

        wossConfiguration = new WossConfiguration();
        wossConfiguration.init();
        System.out.println("client端配置模块启动");
        WossBackUP wossBackUP = (WossBackUP) wossConfiguration.getBackup();
        //wossBackUP.setConfiguration(wossConfiguration);
        Logger logger = wossConfiguration.getLogger();
        WossClient wossClient = (WossClient) wossConfiguration.getClient();
        //wossClient.setConfiguration(wossConfiguration);
        WossGather wossGather = (WossGather) wossConfiguration.getGather();
        //wossGather.setConfiguration(wossConfiguration);

        logger.info("采集模块启动");
        System.out.println("采集模块启动");
        Collection<BIDR> bidrCollection = wossGather.gather();
        logger.info("开始发送数据");
        wossClient.send(bidrCollection);
        System.out.println("client端发送完数据");

    }

}
