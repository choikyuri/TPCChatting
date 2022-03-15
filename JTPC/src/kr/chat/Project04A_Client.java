package kr.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Project04A_Client {

	public static void main(String[] args) {
		
		try {
			
			Socket socket=new Socket("172.16.10.32", 9999);
			System.out.println("Connection Success");
			Scanner scan=new Scanner(System.in);
			String message=scan.nextLine();
			//서버에게 정보보내기 위한 소켓
			OutputStream out=socket.getOutputStream();
			//출력스트림 : 서버에게 정보 보냄
			DataOutputStream dos=new DataOutputStream(out);
			
			dos.writeUTF(message); //메세지 보냄
			
			//입력소켓
			InputStream in=socket.getInputStream();
			//입력스트림
			DataInputStream dis=new DataInputStream(in);
			System.out.println("Receive : "+dis.readUTF());
			
			dis.close();
			dos.close();
			socket.close();
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}//main

}//class
