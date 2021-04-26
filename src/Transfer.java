import java.net.*;
import java.io.*;
import java.util.*;

public class Transfer extends Thread
{
    String filepath;
    int portno;
    Socket socket;
    public Transfer(int port_no,String file_path)
    {
        this.filepath=file_path;
        this.portno=port_no;
    }
    public void run()
    {
        try
        {
            ServerSocket ss = new ServerSocket(portno);
            System.out.println("Peer Server Created");
             socket = ss.accept();
        }
        catch(IOException x)
	    {
			x.printStackTrace();
	    }
new Download(socket,filepath).start();
    }
}

class Download extends Thread
{
    Socket s;
    String filepath;
    String fname;
    public Download(Socket s,String filepath)
    {
        this.s = s;
        this.filepath = filepath;
    }
    public void run()
    {
        try
        {
        OutputStream ous=s.getOutputStream();
		ObjectOutputStream oous=new ObjectOutputStream(ous);
		InputStream ins=s.getInputStream();		    
		ObjectInputStream oins=new ObjectInputStream(ins);
		
		int x =(int)oins.readObject();
		boolean boo = true;
		if (x ==1)
		{
			System.out.println("<--------------------------------------------->");
			while (boo)
			{
				String msg = (String)oins.readObject();
				if (msg.equals("exit"))
				{
					x =2;
					boo=false;
				}
				else
				{
					
					System.out.println("Received Message :: "+msg);
					String mem=Chat.message();
					System.out.println(mem);
					oous.writeObject(mem);
					System.out.println("<--------------------------------------------->");
				}
			}
		}
        
        
        if (x==2)
        {
        fname=(String)oins.readObject();
       while(true)
			{
               File file = new File(filepath+"//"+fname); 
               long length = file.length();
	           byte [] mybytearray = new byte[(int)length];
               oous.writeObject((int)file.length());
	           oous.flush();
               FileInputStream fileinputstream=new FileInputStream(file);
	           BufferedInputStream objBufInStream = new BufferedInputStream(fileinputstream); 
               objBufInStream.read(mybytearray,0,(int)file.length());
	           System.out.println("sending file of " +mybytearray.length+ " bytes");
	           oous.write(mybytearray,0,mybytearray.length);
	           oous.flush(); 
			} }
        
        }
        
        catch(Exception x)
		{
			x.printStackTrace();	
		}
    }
}