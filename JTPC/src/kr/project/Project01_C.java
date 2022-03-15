package kr.project;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Project01_C {

	public static void main(String[] args) {
		String src="info.json";
		//IO->Stream
		InputStream is=Project01_C.class.getResourceAsStream(src);
		if(is==null) {
			throw new NullPointerException("Cannot find resource file");
		}
		
		JSONTokener tokener=new JSONTokener(is); //문자열(json)이 JSON 객체로 변환
		JSONObject object=new JSONObject(tokener); //JSON 객체를 Object로 변환
		JSONArray students=object.getJSONArray("students") ;//Array JSON
		
		for(int i=0;i<students.length();i++) {
			JSONObject student=(JSONObject)students.get(i);
			System.out.print(student.get("name")+"\t");
			System.out.print(student.get("address")+"\t");
			System.out.print(student.get("phone"));
			System.out.println();
		}
	}
}
