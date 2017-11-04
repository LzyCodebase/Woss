import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.client.Gather;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.LongPredicate;


public class WossGather implements Gather, ConfigurationAWare {

    private BufferedReader bufferedReader = null;
    private PrintWriter printWriter = null;
    private BIDR bidr = null;
    private Map map = null;
    private List<BIDR> list = null;
    private String radwtmp_test = null;
    private String rubbish = null;
    private Logger logger = null;
    private WossBackUP wossBackUP =null;
    String NO78 = null;

    @Override
    public void setConfiguration(Configuration configuration) {
        try {
            logger =  configuration.getLogger();
            wossBackUP = (WossBackUP) configuration.getBackup();
        } catch (Exception e) {
            logger.error("采集模块加载其他模块失败");
            e.printStackTrace();
        }
    }

    @Override
    public Collection<BIDR> gather() throws Exception {
        bufferedReader = new BufferedReader(new FileReader(radwtmp_test));
        logger.info("从"+radwtmp_test+"采集数据");
        printWriter = new PrintWriter(rubbish);
        logger.info("未处理数据保存到"+rubbish);
        list = new ArrayList<>();
        map = new HashMap();
        String str = null;
        while ((str=bufferedReader.readLine())!=null){
            String[] split = str.split("[|]");
            if (split.length!=5){
                //格式不对垃圾数据
                printWriter.println(str);
                printWriter.flush();
                continue;
            }

            String AAA_Login_name = split[0].substring(1);
            String Login_ip = split[1];
            String s = split[2];
            String time = split[3];
            String NAS_ip = split[4];
            String keys = Login_ip+NAS_ip;
            if (s.equals("7")){
                //bidr = new BIDR(AAA_Login_name, Login_ip, time, Timestamp logout_date, NAS_ip, Integer time_deration)
                bidr = new BIDR();
                bidr.setAAA_login_name(AAA_Login_name);
                bidr.setLogin_date(new Timestamp(Long.parseLong(time)));
                bidr.setLogin_ip(Login_ip);
                bidr.setNAS_ip(NAS_ip);
                map.put(keys,bidr);
            }else if (s.equals("8")){
                boolean containsKey = map.containsKey(keys);
                if (containsKey){

                    BIDR bidr1 = (BIDR) map.get(keys);
                    Timestamp login_date = bidr1.getLogin_date();
                    Timestamp logout_date = new Timestamp(Long.parseLong(time));
                    long l = logout_date.getTime() - login_date.getTime();
                    bidr1.setLogout_date(logout_date);
                    bidr1.setTime_deration((int) l);

                    list.add(bidr1);
                    map.remove(keys);
                }
            }else {
                //不是7或8，垃圾数据
                printWriter.println(str);
                printWriter.flush();
            }
            //格式符合，但上线下线异常

        }
        logger.info("非正常格式数据备份到"+NO78);
        wossBackUP.store(NO78,map,true);
        logger.info("数据处理完毕");
        System.out.println("数据采集完毕,有"+list.size()+"条");
        return list;

    }



    @Override
    public void init(Properties properties) {
        radwtmp_test = properties.getProperty("radwtmp_test");
        rubbish = properties.getProperty("rubbish");
        NO78 = properties.getProperty("NO78");

    }
}
