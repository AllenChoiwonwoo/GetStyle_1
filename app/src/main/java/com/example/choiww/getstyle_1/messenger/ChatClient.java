package com.example.choiww.getstyle_1.messenger;

import com.example.choiww.getstyle_1.DataClass.Messages_dataClass;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    String ipAddress;
    static final int port=6000;
    Socket client=null;
    BufferedReader read;
    PrintWriter oos;
    BufferedReader ois;
    String sendData;
    String receiveData;

    String user_id;
    ReceiveDataThread rt;
    boolean endflag=false;
//    Scanner sc = new Scanner(System.in);

    public ChatClient( String ip) {
//        user_id=id;
        ipAddress=ip;
        try{
            System.out.println("**** 클라이언트*****");
            client = new Socket(ipAddress, port);

            read= new BufferedReader(new InputStreamReader(System.in));
            ois = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
            oos = new PrintWriter( client.getOutputStream() );
            // 소켓을 생성해 서버와 연결한다.

            System.out.print("로그인한 userId : ");
//            user_id = sc.nextLine();
            user_id = "wonwoo";
            oos.println( user_id );
            // 이게 뭘까?
//            System.out.print("oos.println다음 은 작동하는가? ");
            oos.flush();

            rt= new ReceiveDataThread(client, ois);
            Thread t = new Thread((Runnable) rt);
            t.start();
//            System.out.print("t.start 후");

            while(true){
//            	System.out.print("회원 번호");
//                sendData = read.readLine();
//                System.out.println("sendData : "+sendData);
                //sendData가 서버에 보낼 데이터이다.
                // dateclass에 들어갈 모든 데이터를 입력받아 보내야한다.
                System.out.print("메시지 보낼 방번호 ");
                String roomNumb = "100";
                System.out.print("보내는 userid ");
                String userId = "wonwoo";
                System.out.print("메시지 내용 ");
                String message = "hi there";

//                Messages_dataClass mData = new Messages_dataClass(roomNumb, userId, message);
//                Gson gson = new Gson();
//                JSONParser parser = new JSONParser();
                JSONObject jObject = new JSONObject();
                jObject.put("roomNumb", roomNumb);
                jObject.put("userId", userId);
                jObject.put("message", message);
                String str_mData = jObject.toString();

                System.out.println("ChatClient 1");
                oos.println( str_mData );
                oos.flush();
                System.out.println("ChatClient 2");

                if(message.equals( "/quit") ) {
                    System.out.println("ChatClient 3");
                    endflag = true;
                    break;
                }
            }
            System.out.print("클라이트의 접속을 종료합니다. ");
            System.out.println("ChatClient 4");
            System.exit( 0 );
        } catch(Exception e){
            if(!endflag) e.printStackTrace();
        }
        finally{
            try{
                ois.close();
                oos.close();
                client.close();
                System.exit(0);
            }catch(IOException e2){
                e2.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        new ChatClient( "192.168.0.5");
//    }

}
