
import java.sql.*;

public class Storage {
	
	public Storage(int x, String y,String z)
    {
		
       try{
    	   
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AOS","root","Winter2021");
            String sql = "insert into fileinfo(peerid,path,name) values(?,?,?)";
            PreparedStatement stmt=connection.prepareStatement(sql);
            stmt.setInt(1,x);  
            stmt.setString(2,y); 
            stmt.setString(3,z); 
            int i=stmt.executeUpdate();  
         }
            catch(ClassNotFoundException | SQLException e)
       		{
            	System.out.println("Connection Failed! Check output console");
                e.printStackTrace();           
             }
    
    }    
}


