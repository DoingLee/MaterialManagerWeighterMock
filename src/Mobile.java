import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Doing on 2017/1/13 0013.
 */
public class Mobile extends Thread {

    private static final String TAG = "Mobile =============  ";

    Socket mobileSocket;

    public Mobile() {
    }

    @Override
    public void run() {
        try {
            log("移动端开始连接");
//            mobileSocket = new Socket("127.0.0.1", 10002, null, 6002);
            mobileSocket = new Socket();
            mobileSocket.setReuseAddress(true);
            mobileSocket.setSoTimeout(3600 * 1000);
            mobileSocket.connect(new InetSocketAddress("127.0.0.1", 10002));
            weight();
            mobileSocket.close();
        } catch (IOException e) {
            try {
                e.printStackTrace();
                log("mobileSocket 异常 close 1");
                mobileSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void weight() {

        try {
            log("移动端开始发送称重请求信息");
            String requireString = "{weight:100,weighterName:\"weighter1\"}";
            OutputStream outputStream = mobileSocket.getOutputStream();
            PrintStream out = new PrintStream(outputStream);
            out.println(requireString);
            out.println("end");
            out.flush();

            log("移动端阻塞等待称重完成");
            InputStream inputStream = mobileSocket.getInputStream();  //阻塞
            String s = inputStreamToString(inputStream);
            log("称重结果：" + s);

            JSONObject jsonObject = new JSONObject(s);
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                int realWeight = jsonObject.getInt("realWeight");
            }else {
                String msg = jsonObject.getString("msg");
            }
        } catch (IOException e) {
            try {
                e.printStackTrace();
                log("mobileSocket 异常 close");
                mobileSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private String inputStreamToString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            boolean flag = true;
            while(flag){
                //接收从客户端发送过来的数据
                String str =  bufferedReader.readLine();
                if(str == null || "".equals(str)){
                    flag = false;
                }else{
                    if("end".equals(str)){
                        flag = false;
                    }else{
                        sb.append(str);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void log(String s) {
        System.out.println(TAG + s);
    }

}
