package kr.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Project04F_MultiChatClient {

	public static void main(String[] args) {
		
		try {
			Socket socket=new Socket("172.16.10.32", 9100); //서버에 접속하기 위한 서버정보
			Scanner scan=new Scanner(System.in);
			System.out.print("name : ");
			String name=scan.nextLine();
			Thread sender=new Thread(new ClientSender(socket, name));//보내는 소켓
			Thread receiver=new Thread(new ClientReceiver(socket));//받는 소켓
			sender.start();//ClientSender run()
			receiver.start();//ClientReceiver run()
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Inner Class(보내는 소켓)
	static class ClientSender extends Thread{
		
		Socket socket;
		DataOutputStream out;
		String name;
		
		public ClientSender(Socket socket, String name) {
			this.socket=socket;
			this.name=name;
			
			try {
				//보내는 스트림 생성
				out=new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//메세지 보내기
		@Override
		public void run() {
			Scanner scan=new Scanner(System.in);
			
			try {
				if(out != null)
					out.writeUTF(name);
				while(out != null) {
					String message=scan.nextLine();
					if (message.equals("quit"))
						break;
					out.writeUTF("[" +name+"]"+message);
				}
				out.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//받는 소켓
	//Inner Class
	static class ClientReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		
		ClientReceiver(Socket socket){
			this.socket=socket;
			try {
				//받는 스트림 생성
				in=new DataInputStream(socket.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//받는 보내기
		@Override
		public void run() {
			while(in != null) {//메세지가 있다면
			try {
				System.out.println(in.readUTF());//출력되는 메세지
				
			} catch (Exception e) {
				e.printStackTrace();	
			}
		}
			//닫기
			try {
				in.close();
				socket.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
	
	}//ClientReciver
}//class
