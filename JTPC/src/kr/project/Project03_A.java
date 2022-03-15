package kr.project;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import kr.soldesk.ExcelVO;

public class Project03_A {
	
	public static void main(String[] args) {
		
		String fileName="bookList.xls";
		
		List<ExcelVO> data=new ArrayList<ExcelVO>();
		
		try(FileInputStream fis=new FileInputStream(fileName)) {
			HSSFWorkbook workbook=new HSSFWorkbook(fis);//bookList.xls파일은 메모리에 적재시킴 
			HSSFSheet sheet=workbook.getSheetAt(0); //메모리에 sheet적재
			Iterator<Row> rows=sheet.rowIterator(); //시트에 있는 행 순회하여 갯수 반환
			rows.next(); //첫번째줄 지나가기
			String[] imsi=new String[5];//컬럼안에 데이터를 가져와서 저장할 변수방
			while(rows.hasNext()) {
				HSSFRow row=(HSSFRow)rows.next();//줄 가져오기
				Iterator<Cell> cells=row.cellIterator();//행의 컬럼 순회하여 갯수 반환
				int i=0;
				//셀의 데이터 가져오기
				while(cells.hasNext()) {
					HSSFCell cell=(HSSFCell)cells.next();//셀 읽기
					imsi[i]=cell.toString();
					i++;
				}
				//VO클래에 묶어
				ExcelVO vo=new ExcelVO(imsi[0], imsi[1], imsi[2], imsi[3], imsi[4]);
				//컬렉션에 담기
				data.add(vo);
			}
			
			showExcelData(data);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	public static void showExcelData(List<ExcelVO> data) {
		for(ExcelVO vo:data) {
			System.out.println(vo);
		}
	}


}
