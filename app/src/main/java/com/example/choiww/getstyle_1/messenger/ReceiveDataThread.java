package com.example.choiww.getstyle_1.messenger;

import com.example.choiww.getstyle_1.DataClass.Messages_dataClass;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ReceiveDataThread {
    Socket client;
    BufferedReader ois;
    String receiveData;

    public ReceiveDataThread(Socket s, BufferedReader ois){
        client = s;
        this.ois = ois;
    }

    public void run(){
        try{
            while( ( receiveData = ois.readLine() ) != null ) System.out.println( receiveData );
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                ois.close();
                client.close();
            }catch(IOException e2){
                e2.printStackTrace();
            }
        }
    }

}
