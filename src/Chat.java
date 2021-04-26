import java.util.Random;
public class Chat {
	
	
	
	public static String message()

	{
		Random random = new Random();
		String[] m= new String [] {"Hi","Hello","How are you","Whats Up","haha","Cool","Chill Man","Anything Intersting","Come On"};
		System.out.println("<--Enter your message-->");
		int n=random.nextInt(9);		
		return m[n];
		
	}

}
