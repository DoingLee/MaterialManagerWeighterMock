import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Doing on 2017/1/13 0013.
 */
public class Weighter extends Thread {

    private static final String TAG = "Weighter =============  ";

    Socket weighterSocket;

    public Weighter() {
    }

    @Override
    public void run() {
        try {
            log("称重器开始连接");
//            weighterSocket = new Socket("127.0.0.1", 10001, null, 6001);
            weighterSocket = new Socket();
            weighterSocket.setReuseAddress(true);
            weighterSocket.setSoTimeout(3600 * 1000);
            weighterSocket.connect(new InetSocketAddress("127.0.0.1", 10001));

            register();
            while (true) {
                weight();  //循环等待称重
            }
//            weighterSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register() {
        try {
            log("称重器开始发送注册信息");
            String registerString = "{weighterName:\"weighter1\"}";
            OutputStream outputStream = weighterSocket.getOutputStream();
            PrintStream out = new PrintStream(outputStream);
            out.println(registerString);
            out.println("end");
            out.flush();
//            out.close();

        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    private void weight() {
//        while (true) {
            try {
                log("称重器阻塞等待称重请求");
                InputStream inputStream = weighterSocket.getInputStream();  //阻塞
                String s = inputStreamToString(inputStream);
                log("称重器接收到称重信息：" + s);

//                JSONObject jsonObject = new JSONObject(s);
//                int requiredWeight = jsonObject.getInt("weight");

                //模拟称重
                System.out.println("输入称重质量浮点数（g）：");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                String realWeight = input.readLine(); //阻塞，知道遇到换行符才成功输入
                String result = "{realWeight : " +
                        realWeight +
                        "}";
                OutputStream outputStream = weighterSocket.getOutputStream();
                PrintStream out = new PrintStream(outputStream);
                out.println(result);
                out.println("end");
                out.flush();
//                outputStream.write(result.getBytes(Charset.forName("UTF-8")));
//                outputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
    }

    private String inputStreamToString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String str = bufferedReader.readLine();
//                while (str != null && !str.equals("")) {
//                    sb.append(str);
//                    str = bufferedReader.readLine();
//                }
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
