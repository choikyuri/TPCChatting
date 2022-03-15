package kr.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class CopyClient extends Thread{

	Socket s;
	BufferedReader in;
	PrintWriter out;
	ChatServer server;
	String ip;
	
	public CopyClient(Socket s, ChatServer server) {
		
		this.s=s;
		this.server=server;
		
		try {
			out=new PrintWriter(s.getOutputStream(),true);
			in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			ip=s.getInetAddress().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		 //접속자를 기다림(대기)
		while(true) {
			
			try {
				String msg = in.readLine();
				if(msg.equals("xx:~~X")){
					out.println("xx:~~X");
					server.removeClient(this);
					break;
				}
				server.sendMessage(ip+" : "+msg);
			} catch (Exception e) {
				e.printStackTrace();
			}//try
			
		}//while
	}//run()

}

