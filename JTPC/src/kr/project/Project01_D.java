package kr.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;




public class Project01_D {
	
	public static void main(String[] args) {
		//https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode
		//query=
		String client_id="159r6z3668";
		String client_secret="fDVW2gM3o7lUnWzT4POjEbfLK0ApFSGRlQ1CPMNP";
		//new InputStreamReader(System.in) : 키보드로 입력한 데이터를 문자로 변환
		BufferedReader io=new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print("주소를 입력하세요 : ");
			String address=io.readLine();
			String addr=URLEncoder.encode(address, "UTF-8");
			String reqUrl="https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+addr;
			
			URL url=new URL(reqUrl);
			//HttpURLConnection : URL연동해주는 API클래스
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", client_id); //API정보
			con.setRequestProperty("X-NCP-APIGW-API-KEY", client_secret);
			BufferedReader br; //라인단위로 읽어들이기 위한 객체 생성
			int responseCode=con.getResponseCode();//200
			if(responseCode == 200) {
				//웹에서 넘어온 데이터
				br=new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			}else {
				br=new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			
			String line;
			StringBuffer response=new StringBuffer(); 
			while((line=br.readLine()) != null) {
				response.append(line); //스트림버퍼에 담기
			}
			br.close();
			
			JSONTokener tokener=new JSONTokener(response.toString()); //JSON객체
			JSONObject object=new JSONObject(tokener); //Object로 변환
			System.out.println(object.toString());
			
			JSONArray arr=object.getJSONArray("addresses"); //주소가 여러개일때 반복문으로 출력하기위한 컬렉션
			for(int i=0;i<arr.length();i++) {
				JSONObject temp=(JSONObject)arr.get(i);
				System.out.println("address : "+ temp.get("roadAddress"));
				System.out.println("jibunAddress : "+ temp.get("jibunAddress"));
				System.out.println("경도 : "+ temp.get("x"));
				System.out.println("위도 : "+ temp.get("y"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
