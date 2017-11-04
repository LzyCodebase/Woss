import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;

import java.io.*;
import java.util.Properties;

public class WossBackUP implements BackUP, ConfigurationAWare {

//    boolean STORE_APPEND = true;
//    boolean STORE_OVERRIDE = false;
//    boolean LOAD_REMOVE = true;
//    boolean LOAD_UNREMOVE = false;
    private boolean stroe = true;
    private boolean load = false;
    private Logger logger = null;
    private String filename = null;

    @Override
    public void store(String s, Object o, boolean b) throws Exception {
        System.out.println(logger);
        if(s==null){
            logger.info("备份到默认路径,是否追加"+stroe);
            s = filename;
        }else {
            logger.info("备份到"+s+",是否追加"+stroe);
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(s,stroe));
        objectOutputStream.writeObject(o);
        objectOutputStream.flush();
        objectOutputStream.close();
        logger.info("发送前备份成功");
    }

    @Override
    public Object load(String s, boolean b) throws Exception {
        File file = new File(s);
        if(file.exists()) {
            logger.info("导出备份数据"+s);
        }else {
            file = new File(filename);
            logger.info("导出备份数据"+filename);
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        Object o = objectInputStream.readObject();
        if (!load){
            file.delete();
            logger.warn("备份文件"+file.getName()+"已被删除");
        }
        return o;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        try {
            logger = configuration.getLogger();
        } catch (Exception e) {
            logger.error("在备份模块中加载日志模块失败");
            e.printStackTrace();
        }
    }

    @Override
    public void init(Properties properties) {
        filename = properties.getProperty("WossBackUPfilename");
        stroe = Boolean.parseBoolean(properties.getProperty("STORE_APPEND"));
        load = Boolean.parseBoolean(properties.getProperty("LOAD_REMOVE"));
    }
}
