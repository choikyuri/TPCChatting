package kr.chat;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient_01 {
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		
		Socket s=new Socket("172.16.10.32", 3000);
		
	}

}
