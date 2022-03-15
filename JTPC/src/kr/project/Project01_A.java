package kr.project;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.soldesk.BookDTO;

public class Project01_A {
	
	public static void main(String[] args) {
		//Object(BookDTO) => JSON(String)
		BookDTO dto=new BookDTO("JAVA", 25000, "한빛", 520);
		Gson g=new Gson();
		String json=g.toJson(dto);
		//System.out.println(json);
		
		//JSON(String) => Object(BookDTO) 
		BookDTO dto1=g.fromJson(json, BookDTO.class);
		//System.out.println(dto1);
		//============================================
		
		//Object(List<BookDTO>) => JSON(String) : [ {}, {}, {}....]
		List<BookDTO> lst=new ArrayList<BookDTO>();
		lst.add(new BookDTO("JAVA1", 25000, "한빛1", 520));
		lst.add(new BookDTO("JAVA2", 26000, "한빛2", 620));
		lst.add(new BookDTO("JAVA3", 27000, "한빛3", 720));
		
		String lstJson=g.toJson(lst);
		//System.out.println(lstJson);
		
		//JSON(String) => Object(List<BookDTO>)
		List<BookDTO> lst1=g.fromJson(lstJson, new TypeToken<List<BookDTO>>(){}.getType()); //reflect
		for(BookDTO vo:lst1) {
			System.out.println(vo);
		}
		
	}

}
