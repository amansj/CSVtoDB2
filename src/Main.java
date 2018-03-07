

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	public static void pageCalculations(MutableInt numOfRecord,MutableInt numPages,MutableInt remainingRecord,int numOfThread,XSSFSheet sheet1) {
		numOfRecord.setValue(sheet1.getPhysicalNumberOfRows()-1);
		numPages.setValue((numOfRecord.intValue())/numOfThread);
		remainingRecord.setValue((numOfRecord.intValue())-numOfThread*(numPages.intValue()));	
	}
	private static final int NUMBER_OF_CORES=2; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("Enter Number of Thread:");
		Scanner sc=new Scanner(System.in);
		int numOfThread=sc.nextInt();
		Instant starttime = Instant.now();
		String fileName="doc/cu0.xlsx";
		File src=new File(fileName);
		try {
			FileInputStream fis=new FileInputStream(src);
			XSSFWorkbook wb=new XSSFWorkbook(fis);
			XSSFSheet sheet1=wb.getSheetAt(0);			
			MutableInt numRecords = new MutableInt();
			MutableInt numPages=new MutableInt();
			MutableInt remRecords=new MutableInt();
			pageCalculations(numRecords,numPages,remRecords,numOfThread,sheet1);
			ExecutorService execService=Executors.newFixedThreadPool(NUMBER_OF_CORES);
			int start,end;
			int i;
			int numOfRecords=numRecords.intValue();
			int numOfPages=numPages.intValue();
			int remainingRecords=remRecords.intValue();
			for(i=0;i<numOfThread;i++)
			{
				start=i*numOfPages+1;
				end=start+numOfPages-1;
				TestMultiNaming1 thread=new TestMultiNaming1(sheet1);
				thread.setIndex(start, end);
				execService.execute(thread);
			}
			while(remainingRecords!=0)
			{
				start=numOfRecords-remainingRecords+1;
				end=numOfRecords-remainingRecords+1;
				TestMultiNaming1 thread=new TestMultiNaming1(sheet1);
				thread.setIndex(start, end);
				execService.execute(thread);
				remainingRecords--;
			}
			execService.shutdown();  
		    while (!execService.isTerminated()) {   }  
			wb.close();
			sc.close();
			Instant endtime = Instant.now();
			System.out.println(Duration.between(starttime, endtime));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
