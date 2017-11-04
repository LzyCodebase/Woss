import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.client.Client;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class WossClient implements Client, ConfigurationAWare {

    private Socket socket = null;
    private ObjectOutputStream objectOutputStream = null;
    private OutputStream outputStream =null;
    private String socketip = null;
    private String socketport = null;
    private Logger logger = null;
    private WossBackUP wossBackUP = null;

    @Override
    public void send(Collection<BIDR> collection)  {
        try {

            logger.info("客户端正在获取连接...");
            socket = new Socket(socketip, Integer.parseInt(socketport));
            logger.info("客户端获取到连接");
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(collection);
            objectOutputStream.flush();
            logger.info("客户端写出完毕");
        }catch (Exception e){
            e.printStackTrace();
            //备份
            try {
                wossBackUP.store(null,collection,true);
            } catch (Exception e1) {
                logger.error("备份失败！！！");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void init(Properties properties) {
        socketip = properties.getProperty("socketip");
        socketport = properties.getProperty("socketport");

    }

    @Override
    public void setConfiguration(Configuration configuration) {
        try {
            logger =  configuration.getLogger();
        } catch (Exception e) {
            logger.error("客户端加载日志模块失败");
            e.printStackTrace();
        }
    }
}
