/**
 * Created by Doing on 2017/1/13 0013.
 */
public class MockManager {

    private static Weighter weighter;
    private static Mobile mobile;

    public static void main(String[] args) {

        weighter = new Weighter();
        weighter.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mobile = new Mobile();
        mobile.start();

    }
}
