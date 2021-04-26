import java.util.*;

public class Main   {

        public static void main(String []args)
        {
            Scanner sc = new Scanner(System.in);
            System.out.print("Please choose from below options");
            System.out.print("\n");
            System.out.print("Hit 1 to start the NAPSTER-SERVER");
            System.out.print("\n");
            System.out.print("Hit 2 to start the PEER\n");
            int option = sc.nextInt();
            if (option == 1)
            {
                NapsterServer s = new NapsterServer();
            }

            else if (option == 2)
            {
                Peer p = new Peer();
            }

            else
            {
                System.out.print("Invalid Choice!!!");
            }
        }



}