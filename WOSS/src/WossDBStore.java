import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.server.DBStore;
import com.briup.woss.ConfigurationAWare;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

public class WossDBStore implements DBStore, ConfigurationAWare {

    Connection connection = null;
    PreparedStatement preparedStatement = null;

    //private WossConfiguration wossConfiguration = null;

    String url= null;
    Logger logger = null;
    WossBackUP wossBackUP = null;


    @Override
    public void setConfiguration(Configuration configuration) {
        try {
            logger = configuration.getLogger();
            wossBackUP = (WossBackUP) configuration.getBackup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveToDB(Collection<BIDR> collection) throws Exception {
        logger.info("数据入库前备份");
        wossBackUP.store(null,collection,true);
        logger.info(url);
        System.out.println("服务端入库开始");
        logger.info("服务端入库开始");

        String sql = "INSERT INTO t_detail VALUES (?,?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(sql);

        for (BIDR bidr:collection){
            preparedStatement.setString(1,bidr.getAAA_login_name());
            preparedStatement.setString(2,bidr.getLogin_ip());
            preparedStatement.setTimestamp(3,bidr.getLogin_date());
            preparedStatement.setTimestamp(4,bidr.getLogout_date());
            preparedStatement.setString(5,bidr.getNAS_ip());
            preparedStatement.setInt(6,bidr.getTime_deration());
            preparedStatement.execute();
        }

    }

    @Override
    public void init(Properties properties) {

        try {
            Class.forName(properties.getProperty("driver"));

            url = properties.getProperty("protocol")+
                    properties.getProperty("ip")+":"+
                    properties.getProperty("port")+"/"+
                    properties.getProperty("schema")+"?useSSL=true";

            System.out.println(url);
            //System.out.println(properties);
            connection = DriverManager.getConnection(url,properties);

        } catch (ClassNotFoundException e) {
            System.err.println("获取驱动失败");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("获取配置信息失败");
            e.printStackTrace();
        }

    }
}
