package toDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
public class TestMultiNaming1 implements Runnable {
	XSSFSheet sheet1;
	private int start,end;
	public void setIndex(int s,int e)
	{
		start=s;
		end=e;
	}
	TestMultiNaming1(XSSFSheet sheet)
	{
		sheet1=sheet;
	}
	public void run(){  
		   Connection con=C3P0DataSource.getInstance().getConnection();
		   for(int i=start;i<=end;i++)
		   {
			   try  {
				    System.out.println("/****************************************Processing  " +i+ "th Record********************************************************************************/");
				    PreparedStatement ps=con.prepareStatement("insert into CU values(?,?,?,?)");
					ps.setInt(1,Integer.parseInt(sheet1.getRow(i).getCell(0).getRawValue()));
					ps.setString(2,sheet1.getRow(i).getCell(1).toString());
					ps.setString(3,sheet1.getRow(i).getCell(2).toString());
					ps.setString(4,sheet1.getRow(i).getCell(3).toString());
					ps.executeUpdate();
					}
				 catch (SQLException e1) {
					 // TODO Auto-generated catch block
					 e1.printStackTrace(); 
				  }
		   }
		   try{
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
