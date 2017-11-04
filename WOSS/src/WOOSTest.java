import com.briup.woss.server.Server;

public class WOOSTest {

    public static void main(String[] args) throws Exception {

        Thread t1 = new Thread(() -> {
            try {
                new ServerStart().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                new ClientStart().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }

}
