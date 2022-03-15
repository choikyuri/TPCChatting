package kr.soldesk;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
public class ExcelDAO {
	private List<ExcelVO> list;
	private HSSFWorkbook wb;
	
	public ExcelDAO() {
		list=new ArrayList<ExcelVO>(); //책의 정보가 들어갈 Bean
		wb=new HSSFWorkbook(); //워크북 메모리 생성
	}
    public void excel_input() {
    	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    	try {
            HSSFSheet firstSheet = wb.createSheet("BOOK SHEET"); //시트 생성
            HSSFRow rowA = firstSheet.createRow(0); //첫번째 행에 속성명입력
            HSSFCell cellA = rowA.createCell(0);
            cellA.setCellValue(new HSSFRichTextString("책제목"));
            HSSFCell cellB = rowA.createCell(1);
            cellB.setCellValue(new HSSFRichTextString("저자"));
            HSSFCell cellC = rowA.createCell(2);
            cellC.setCellValue(new HSSFRichTextString("출판사"));
            HSSFCell cellD = rowA.createCell(3);
            cellD.setCellValue(new HSSFRichTextString("isbn"));
            HSSFCell cellE = rowA.createCell(4);
            cellE.setCellValue(new HSSFRichTextString("이미지이름"));            
            HSSFCell cellF = rowA.createCell(5);
            cellF.setCellValue(new HSSFRichTextString("이미지"));

            int i=1;
            while(true) {
            	//가져올 데이터명 입력
	        	System.out.print("책제목:"); 
	    		String title=br.readLine();
	    		System.out.print("책저자:");
	    		String author=br.readLine();
	    		System.out.print("출판사:");
	    		String company=br.readLine();
	    		//두번째 행에 가져올 데이터 타입 입력
	    		HSSFRow rowRal = firstSheet.createRow(i);	            
	            HSSFCell cellTitle = rowRal.createCell(0);            
	            cellTitle.setCellValue(new HSSFRichTextString(title));	            
	            HSSFCell cellAuthor = rowRal.createCell(1);
	            cellAuthor.setCellValue(new HSSFRichTextString(author));	            
	            HSSFCell cellCompany = rowRal.createCell(2);
	            cellCompany.setCellValue(new HSSFRichTextString(company));	            
	            i++;
	            
	            //Bean에 저장
	            ExcelVO vo=new ExcelVO(title, author, company);
	            //  API통해서 실제 데이터 isbn, image 등 검색
	            ExcelVO data=naver_search(vo);
	            list.add(data);//저장하기
	            System.out.print("계속입력 하시면 Y / 입력종료 N:");
	            String key=br.readLine();
	            if(key.equals("N")) break;
	        }
            System.out.println("데이터 추출중...........");
            excel_save();
		} catch (Exception e) {
            e.printStackTrace();
		}
    }		
	//상세 검색은 책 제목(d_titl), 저자명(d_auth), 목차(d_cont), ISBN(d_isbn), 출판사(d_publ) 5개 항목 중에서 1개 이상 값을 입력해야함.
public ExcelVO naver_search(ExcelVO vo) {
		try {
			String URL_STATICMAP = "https://openapi.naver.com/v1/search/book_adv.xml?d_titl="+URLEncoder.encode(vo.getTitle(), "UTF-8")+"&d_auth="+URLEncoder.encode(vo.getAuthor(), "UTF-8")+"&d_publ="+URLEncoder.encode(vo.getCompany(), "UTF-8");
	        URL url = new URL(URL_STATICMAP);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", "R3GgXvVEJ6c0E6FCb1gz");
            con.setRequestProperty("X-Naver-Client-Secret", "QJ4_VkuzeI");
            int responseCode = con.getResponseCode();
            BufferedReader br;            
            if(responseCode==200) { 
                br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            } else {  
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer(); //문자열 추가 변경시 사용            
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            Document doc = Jsoup.parse(response.toString());
            //System.out.println(doc.toString());
            Element isbn = doc.select("isbn").first();
            System.out.println("isbn: "+ isbn.text());
            
            String img=doc.toString();
            String imgTag=img.substring(img.indexOf("<img>")+5);
            img=imgTag.substring(0, imgTag.indexOf("?"));
            System.out.println(img);
            vo.setIsbn(isbn.text().split(" ")[1]);
            String fileName=img.substring(img.lastIndexOf("/")+1);
            vo.setImgurl(fileName);   
            // DownloadBroker            
            Runnable dl=new DownloadBroker(img, fileName);
            Thread t=new Thread(dl);
            t.start();
         } catch (Exception e) {
            System.out.println(e);
        }
		return vo;
	}//naver_search
	
 public void excel_save() {
		  try {
		  HSSFSheet sheet = wb.getSheetAt(0);
		   if(wb != null && sheet != null) {
			  Iterator rows = sheet.rowIterator();
	          rows.next();
	          int i=0; // list의 index
		      while (rows.hasNext()) {
		    	    HSSFRow row = (HSSFRow) rows.next();
	               	HSSFCell cell=row.createCell(3);	
	               	cell.setCellType(CellType.STRING);
					cell.setCellValue(list.get(i).getIsbn());				
					
					cell=row.createCell(4);	
					cell.setCellType(CellType.STRING);
					cell.setCellValue(list.get(i).getImgurl());
					
		    	    InputStream inputStream = new FileInputStream(list.get(i).getImgurl());
		    	    byte[] bytes = IOUtils.toByteArray(inputStream);
		    	    int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		    	    inputStream.close();
				        
					CreationHelper helper = wb.getCreationHelper();
					Drawing drawing = sheet.createDrawingPatriarch();
					ClientAnchor anchor = helper.createClientAnchor();
					   
				    anchor.setCol1(5); //Column B
			    	anchor.setRow1(i+1); //Row 3
			    	anchor.setCol2(6); //Column C
			    	anchor.setRow2(i+2); //Row 4

			    	Picture pict = drawing.createPicture(anchor, pictureIdx);
			    	Cell cellImg = row.createCell(5);
			    	int widthUnits = 20*256; 
			    	sheet.setColumnWidth(5, widthUnits);
			    	short heightUnits = 120*20; // 1/20
			    	cellImg.getRow().setHeight(heightUnits);
				    i++;
		        }		
				FileOutputStream fos = new FileOutputStream("isbn.xls");
				wb.write(fos);
				fos.close();
				System.out.println("ISBN,ImageURL 저장성공");
		   }
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
	}
}
