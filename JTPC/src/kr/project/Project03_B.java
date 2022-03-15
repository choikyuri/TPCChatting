package kr.project;
import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
//메모리에 가상의 이미지를 만들어 저장하고 불러오는 프로젝트
public class Project03_B {
	public static void main(String[] args) {
		
		try {
			Workbook wb=new HSSFWorkbook();//메모리에 가상의 워크북 만들기
			Sheet sheet=wb.createSheet("My Sample Excel"); //시트 만들기
			InputStream is=new FileInputStream("pic.jpg"); //이미지파일 읽어오기
			byte[] bytes=IOUtils.toByteArray(is); //바이트 단위로 읽기위해 바이트 배열로 바꾸기
			int pictureIdx=wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);  //워크북안 메모리위에 이미지 추가
			is.close();
			
			CreationHelper helper=wb.getCreationHelper(); //실제로 이미지 드로윙을 도와주는 메소드
			Drawing drawing=sheet.createDrawingPatriarch(); // 시트에 이미지 드로윙객체 생성
			ClientAnchor anchor=helper.createClientAnchor(); // 위치지정
			anchor.setCol1(1); //1번째 컬럼에 2번째줄
			anchor.setRow1(2);			
			anchor.setCol2(2); //2번재 컬럼에 3번째줄
			anchor.setRow2(3);
		
			//지정된 위치에 이미지 생성
			Picture pict=drawing.createPicture(anchor, pictureIdx);
			
			Cell cell=sheet.createRow(2).createCell(1);
			//set width to n character width=count charecters * 256
			int w=20*256; //(폭 하나당 256분의 1)
			sheet.setColumnWidth(1, w);
			
			short h=120*20;
			//set height to n point in twips 20 (폭 하나당 20)
			cell.getRow().setHeight(h);
			
			FileOutputStream fileOut=new FileOutputStream("myFile.xls"); //Excel 파일 생성
			wb.write(fileOut); //파일에 이미지 저장하기
			fileOut.close();
			System.out.println("이미지 생성 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
