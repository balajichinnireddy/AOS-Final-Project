import java.net.*;
import java.io.*;
import java.util.*;


public class NapsterServer{
	ServerSocket serversocket;
	Socket socket;
	Scanner sc = new Scanner(System.in);
	int sp;

	public NapsterServer()
	{
		System.out.println("Enter the port number to start the server");
		sp = sc.nextInt();
		ArrayList<FileInfo> files = new ArrayList<FileInfo>();
		try
		{
			serversocket = new ServerSocket(sp);
		}
		catch (IOException x)
		{
			System.out.println(" "+ x.getMessage());
			x.printStackTrace();
		}
		System.out.println("Napster-Server started at port "+ sp);
		while(true)
		{
			System.out.println("Waiting for peers to establish connection");
			try
			{
				socket = serversocket.accept();
			}
			catch (IOException x)
			{
				System.out.println(" "+ x.getMessage());
				x.printStackTrace();
			}
			new NserverThread(socket,files,sp).start();
		}
	}
}


class NserverThread extends Thread {
	Socket nserver=null;
	ArrayList<FileInfo> files = new ArrayList<FileInfo>();
	int selection = 1;
	Indexing ind = new Indexing();
	int sp;

	public NserverThread(Socket nserver,ArrayList<FileInfo> files, int sp)
	{
		this.nserver = nserver;
		this.files = files;
		this.sp = sp;
	}
	public void run()
	{
		try
		{
			OutputStream ous = nserver.getOutputStream();
			InputStream ins = nserver.getInputStream();
			System.out.println("Connection Established successfully on the port no:" + nserver.getRemoteSocketAddress()+" ");
			while(selection == 1)
			{
				ObjectInputStream oins = new ObjectInputStream(ins);
				ind = (Indexing) oins.readObject();
				selection = ind.choice;
				if (selection == 2)
					break;
				else
				{
					FileInfo f1 = new FileInfo();
					f1.peerid = ind.peerid;
					f1.path = ind.path;
					f1.fname = ind.fname;
					files.add(f1);
					new Storage(f1.peerid, f1.path, f1.fname);
					System.out.println(" File "+ind.fname+" from peer "+ind.peerid+" is registered");
				}
			}
			if(selection == 2)
			{
				int count = 0;
				for (int i=0;i<files.size();i++)
				{
					FileInfo f1 = files.get(i);
					if ((f1.fname).equals(ind.fname))
					{
						ind.files[count++]=f1.peerid;
						ind.path=f1.path;
					}
				}
				
					if(ind.files[0] == 0) {
					ind = Checking.check(ind.fname);
					}

				ObjectOutputStream oous = new ObjectOutputStream(ous);
				oous.writeObject(ind);
				oous.flush();
				oous.close();
			}

		}
		catch (ClassNotFoundException x) 
		{
			System.out.println("Class not found");
			x.printStackTrace();
		}
		catch (IOException x)
		{
			System.out.println("Client Disconnected");
			x.printStackTrace();
		}
	}
}