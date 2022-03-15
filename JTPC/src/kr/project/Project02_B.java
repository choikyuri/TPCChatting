package kr.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kr.soldesk.DownloadBroker;



public class Project02_B {

	public static void main(String[] args) {
		String url="https://sum.su.or.kr:8888/bible/today/Ajax/Bible/BosyMatter?qt_ty=QT1";
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print("[입력->년(yyyy)-월(mm)-일(ddd)] : ");
			String bible=br.readLine();
			url += "&Base_de="+bible+"&bibleType=1"; 
			System.out.println("=========================================");
			Document doc=Jsoup.connect(url).post();
			Element bible_text=doc.select(".bible_text").first(); //주제목
			System.out.println(bible_text.text());
			System.out.println("--------------------------------------------------------------------");
			Element bibleinfo_box =doc.select(".bibleinfo_box").first(); //소제목
			System.out.println(bibleinfo_box.text());
			System.out.println("---------------------------------------------------------------------");
			//내용
			Elements liList =doc.select(".body_list > li"); //소제목
			for(Element li:liList) {
				System.out.print(li.select(".num").first().text()+" : ");//내용번호
				System.out.println(li.select(".info").first().text());//말씀	
			}
			
			//리소스 다운로드(mp3, image)
			/*
			 //오디오
			Element tag =doc.select("source").first(); //오디오
			String dPath=tag.attr("src").trim(); //속성을 붙음
			System.out.println(dPath);
			String filename=dPath.substring(dPath.lastIndexOf("/")+1);
			System.out.println(filename);
			*/
			//이미지
			Element tag =doc.select(".img>img").first(); //오디오
			String dPath="https://sum.su.or.kr:8888"+tag.attr("src").trim(); //속성을 붙음
			String filename=dPath.substring(dPath.lastIndexOf("/")+1);
			System.out.println(filename);
			
			Runnable r=new DownloadBroker(dPath, filename);
			Thread dLoad=new Thread(r);//스레드 구현
			dLoad.start();//다운로드 시작
			for(int i=0;i<10;i++) {
				try {
					Thread.sleep(1000);//1초
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.print(""+(i+1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
