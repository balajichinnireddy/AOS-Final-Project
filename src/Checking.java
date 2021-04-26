import java.sql.*;
public class Checking {

	public static Indexing check(String x)
	{
		Indexing i = new Indexing();
		//System.out.println(x);

		try{

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AOS","root","Winter2021");
			String sql = "select * from fileinfo where name = ?";
			PreparedStatement stmt=connection.prepareStatement(sql);
			stmt.setString(1,x);  
			ResultSet rs = stmt.executeQuery();
			int count =0;
			while (rs.next())
			{
				i.fname = rs.getString("name");
				i.path =  rs.getString("path");
				i.files[count++] = rs.getInt("peerid");
			} 
			
		}
		catch(ClassNotFoundException | SQLException e)
		{
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();           
		}

		return i;
	}

}
