package org.example.bio;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class BlockingIO {
    @lombok.SneakyThrows
    public static void main(String[] args) {
        ByteOutputStream byteOutputStream = new ByteOutputStream("123".length());
        byteOutputStream.write("123".getBytes());
        ByteInputStream byteInputStream = byteOutputStream.newInputStream();
        byte[] bytes = byteInputStream.getBytes();
        String s = new String(bytes);
        System.out.println(s);
        //jdk1.0 自带的ServerSocket 监听30065端口
//        ServerSocket serverSocket = new ServerSocket(30065);
//        Socket clientSocket = serverSocket.accept();
//        BufferedReader in = new BufferedReader(                     //3
//                new InputStreamReader(clientSocket.getInputStream()));
//        PrintWriter out =
//                new PrintWriter(clientSocket.getOutputStream(), true);
//        String request, response;
//        while ((request = in.readLine()) != null) {                 //4
//            if ("Done".equals(request)) {                         //5
//                break;
//            }
//        }
        //6
//        out.println(request);
    }
}
