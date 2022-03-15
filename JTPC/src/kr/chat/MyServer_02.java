package kr.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer_02  extends Thread{

	 ServerSocket ss;
	 
	 public MyServer_02() {
		try {
			ss=new ServerSocket(3001);
			System.out.println("서버 시작");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new MyServer_02().start();

	}

	@Override
	public void run() {
		
		while(true) {
			
			try {
				
				Socket s=ss.accept();
				//클라이언트 접속해서 문자열을 보내기 때문에 받아야함
				BufferedReader reader= new BufferedReader(new InputStreamReader(s.getInputStream()));
				
				String msg=reader.readLine();//접속자가 보낸 메세지
				String ip=s.getInetAddress().getHostAddress();
				
				System.out.println(ip + " : "+msg);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
