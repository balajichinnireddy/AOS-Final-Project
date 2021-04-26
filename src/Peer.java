import java.net.*;
import java.io.*;
import java.util.*;

public class Peer
{
	public Peer()
	{
		String localhost = "127.0.0.1";
		Indexing ind = new Indexing();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the port number of server to connect ");
		int sp=sc.nextInt();
		try
		{
			System.out.println("Connecting to Napster-Server at 127.0.0.1 over the port "+sp);
			Socket peer = new Socket(localhost,sp);
			System.out.println("Connection Established successfully on the port no:" + peer.getRemoteSocketAddress()+" ");
			OutputStream ous = peer.getOutputStream();
			InputStream ins = peer.getInputStream();
			System.out.println("Enter the ID of this peer");
			int peerid = sc.nextInt();
			System.out.println("Enter the Shared Directory path");
			sc.nextLine();
			String path = sc.nextLine();
			System.out.println("Enter the port number of this peer to act as server during file transfer");
			int port = sc.nextInt();
			Transfer t = new Transfer(port,path);
			t.start();
			int selection = 1;
			if (selection == 1)
			{
				File finfo = new File(path);
				String [] filelist = finfo.list();
				for (int i=0;i<filelist.length;i++)
				{
					File f = new File(filelist[i]);
					ObjectOutputStream oous = new ObjectOutputStream(ous);
					ind.fname = f.getName();
					ind.choice = 1;
					ind.path = path;
					ind.peerid = peerid;
					oous.writeObject(ind);
					System.out.println("Registering File "+f.getName()+ " from " +path+" to the Napster-Server ");

				}
			}
			System.out.println("Hit 1 to seacrh for file");
			System.out.println("Hit 2 to Quit");
			selection = sc.nextInt();
			if (selection == 1)
			{
				try
				{
					System.out.println("Enter the name of file you want to download");
					sc.nextLine();
					String filename = sc.nextLine();
					ind.fname = filename;
					ind.choice = 2;
					ObjectOutputStream oous = new ObjectOutputStream(ous);
					oous.writeObject(ind);
					oous.flush();

					ObjectInputStream oins = new ObjectInputStream(ins);
					Indexing ind1 = (Indexing)oins.readObject();
					int check = 0;
					if(ind1.files.length!=0)
					{
						if(ind1.files[0]!=0)
							System.out.println(ind.fname + " is located at ");
						for(int i=0;i<ind1.files.length;i++)
						{
							if(ind1.files[i]!=0)
							{
								check=1;
								System.out.println("Peer "+ind1.files[i]);
							}
						}
						if(check==0)
						{
							System.out.println("File not found in any peer");	
						}
						else
						{
							System.out.println("Enter the peer number");
							System.out.print("");
							int pe = sc.nextInt();
							int verify=0;
							for(int i=0;i<ind1.files.length;i++)
							{
								if(ind1.files[i]!=0)
								{
									if(pe == ind1.files[i])
									{
										verify=1;
										break;
									}
								}
							}
							if(verify == 1)
							{
								System.out.println("Enter the port number of selected peer which is acting as a server");
								System.out.print("");
								int sport=sc.nextInt();
								PeerAsServer(pe,sport,ind1.fname,ind1.path,path);
								System.out.println(ind.fname+ " Downloaded from peer "+ pe);
							}
							else
							{
								System.out.println("Invalied Peer");
								peer.close();
							}

						}
					}
					else 
					{
						System.out.println("File does not exist");

					}
					peer.close();
					System.out.println("peer Disconnected from server.");

				}
				catch (IOException x)
				{
					System.out.println(" "+x.getMessage());
					x.printStackTrace();
				} 
			}
			else
			{
				System.out.println("Closing");
				System.exit(1);
			} 
		}
		catch (UnknownHostException x) 
		{
			System.out.println(" "+x.getMessage());
			x.printStackTrace();
		} 
		catch (ClassNotFoundException x) 
		{
			System.out.println(" "+x.getMessage());
			x.printStackTrace();
		}
		catch (IOException x) 
		{
			System.out.println("Start the server first");	
		}
	}

	public static void PeerAsServer(int peerid,int portno,String fname,String path1,String path2)
	{
		try
		{
			Socket peerasserversocket = new Socket("localhost",portno);
			ObjectOutputStream ooos=new ObjectOutputStream(peerasserversocket.getOutputStream());
			ooos.flush();
			ObjectInputStream oois=new ObjectInputStream(peerasserversocket.getInputStream());


			System.out.println("Hit 1 to chat and then download");
			System.out.println("Hit 2 to download");
			Scanner sc = new Scanner(System.in);
			int ch = sc.nextInt();
			ooos.writeObject(ch);
			sc.nextLine();
			boolean boo = true;
			if (ch == 1)
			{
				System.out.println("<--------------------------------------------->");
				while(boo)
				{
					
					System.out.println("To exit chat and start Download type 'exit'");
					System.out.println("<--Enter your Message-->");
					String msg = sc.nextLine();
					if (msg.equals("exit"))
					{
						ch = 2;
						ooos.writeObject(msg);
						boo = false;
					}
					else {
						ooos.writeObject(msg);
						String m =(String)oois.readObject();
						System.out.println("");
						System.out.println("Received Message :: "+ m);
						System.out.println("<--------------------------------------------->");
					}
				}
			}


			if (ch == 2) 
			{
				ooos.writeObject(fname);
				int readbytes=(int)oois.readObject();
				System.out.println("bytes transferred: "+readbytes);
				byte[] b=new byte[readbytes];
				oois.readFully(b);
				OutputStream fileoutputstream=new FileOutputStream(path2+"//"+fname);
				BufferedOutputStream bos=new BufferedOutputStream(fileoutputstream);
				bos.write(b, 0,(int) readbytes);
				System.out.println(fname+" file has be downloaded "+path1);
				bos.flush();  
			}

		}
		catch(Exception x)
		{
			System.out.println(x.getMessage());
			x.printStackTrace();
		}
	}

}