package kr.chat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer extends Thread{

	ServerSocket ss;
	ArrayList<CopyClient> list;
	
	public ChatServer() {
		
		try {
			list=new ArrayList<CopyClient>(); //리스트 생성
			ss=new ServerSocket(3500);
			
			System.out.println("Start Server!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new ChatServer().start();

	}

	@Override
	public void run() {
		 //접속자를 기다림(대기)
		while(true) {
			
			try {
				Socket s=ss.accept();
				String ip=s.getInetAddress().getHostAddress();
				System.out.println(ip + "님 접속");
				
				CopyClient cc=new CopyClient(s, this);
				list.add(cc);
				cc.start(); //클라이언트 복사본의 스레드 시작
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	//==============================================================
	public void sendMessage(String msg) {
		
		try {
			
			for(CopyClient cc:list) {
				cc.out.println(msg);//메세지 전달
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//-----------------------------------------------------
	public void removeClient(CopyClient cc) {
		list.remove(cc);
		sendMessage("★☆★☆"+cc.ip+"님 퇴장!★☆★☆");
	}
	
	

}
