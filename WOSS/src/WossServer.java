import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.server.Server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class WossServer implements Server, ConfigurationAWare {

    private ServerSocket serverSocket = null;
    private InputStream inputStream = null;
    private ObjectInputStream objectInputStream = null;
    private List<BIDR> list = null;
    private String socketport = null;
    private Logger logger = null;

    @Override
    public Collection<BIDR> revicer() throws Exception {
        serverSocket = new ServerSocket(Integer.parseInt(socketport));
        logger.info("服务端开启连接");
        Socket accept = serverSocket.accept();
        logger.info("服务端客户端建立连接");
        inputStream = accept.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);

        list = new ArrayList<>();
        list = (List<BIDR>) objectInputStream.readObject();
        logger.info("服务端接收到数据");
        System.out.println("server端接收到数据"+list.size());

        return list;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void init(Properties properties) {
        socketport = properties.getProperty("socketport");
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        try {
            logger =  configuration.getLogger();
        } catch (Exception e) {
            logger.error("服务端加载日志模块失败");
            e.printStackTrace();
        }
    }
}
