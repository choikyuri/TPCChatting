package kr.project;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Project01_E {	
	//지도 이미지 생성 메서드
	public static void map_service(String point_x, String point_y,  String address) {
		    String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
	        try {
	        //위도와 경도, 공백처리를 utf-8로
	        String pos=URLEncoder.encode(point_x + " " + point_y, "UTF-8");
	        String url = URL_STATICMAP;
	        url += "center=" + point_x + "," + point_y; //찾고자하는 위치가 중앙에 표시되도록
	        url += "&level=16&w=700&h=500"; //줌 레벨이며(범위 : 0~20)
	        //풍선도움말
	        url += "&markers=type:t|size:mid|pos:"+pos+"|label:"+URLEncoder.encode(address, "UTF-8");
	        URL u = new URL(url);
	        HttpURLConnection con = (HttpURLConnection)u.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "159r6z3668");
	        con.setRequestProperty("X-NCP-APIGW-API-KEY", "fDVW2gM3o7lUnWzT4POjEbfLK0ApFSGRlQ1CPMNP");
	        int responseCode = con.getResponseCode();
	        BufferedReader br;
	        if(responseCode==200) { // 정상 호출
	                InputStream is = con.getInputStream();
	                int read = 0;
	                byte[] bytes = new byte[1024]; //이미지는 byte단위로 받아옴
	                // 랜덤한 이름으로 파일 생성
	                String tempname = Long.valueOf(new Date().getTime()).toString();
	                File f = new File(tempname + ".jpg");   
	                f.createNewFile(); //파일생성
	                OutputStream outputStream = new FileOutputStream(f); //출력스트림
	                while ((read =is.read(bytes)) != -1) { //-1: 끝이 아니면
	                    outputStream.write(bytes, 0, read); //0부터 읽어들인만큼 바이트로 저장
	                }
	                is.close();
	        } else {  // 에러 발생
	            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();
	            while ((inputLine = br.readLine()) != null) {
	                response.append(inputLine);
	            }
	            br.close();
	            System.out.println(response.toString());
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	
	public static void main(String[] args) {
        
		//String apiURL="https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?query=";
		String client_id = "159r6z3668";
        String client_secret = "fDVW2gM3o7lUnWzT4POjEbfLK0ApFSGRlQ1CPMNP";
		BufferedReader io=new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("주소를 입력하세요:");
			String address=io.readLine();
			String addr=URLEncoder.encode(address, "UTF-8");
			String reqUrl="https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+addr;
			
			URL url=new URL(reqUrl);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", client_id);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", client_secret);
			BufferedReader br;
			int responseCode=con.getResponseCode(); // 200
			if(responseCode==200) {
                br=new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));				
			}else {
				br=new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			
			String line;
			StringBuffer response=new StringBuffer(); // JSON
			// 추가된 부분
			String x=""; 
			String y=""; 
			String z="";
			while((line=br.readLine())!=null) {
				    response.append(line); 				
			}
			br.close();
			
			JSONTokener tokener=new JSONTokener(response.toString());
			JSONObject object=new JSONObject(tokener);
			System.out.println(object.toString());
			
			JSONArray arr=object.getJSONArray("addresses");
			for(int i=0;i<arr.length();i++) {
				  JSONObject temp=(JSONObject) arr.get(i);
				  System.out.println("address:" + temp.get("roadAddress"));
				  System.out.println("jibunAddress:" + temp.get("jibunAddress"));
				  System.out.println("경도:" + temp.get("x"));
				  System.out.println("위도:" + temp.get("y"));
				  //추가된 부분
				  x=(String) temp.get("x");
				  y=(String) temp.get("y");
				  z=(String) temp.get("roadAddress");
			}
			// 추가된 부분
			map_service(x,y,z);			
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}

}
