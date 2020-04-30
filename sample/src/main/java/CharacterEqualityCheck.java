/**
 * Created by Suriyanarayanan K
 * on 22/04/20 11:20 AM.
 */
import java.util.*;
public class CharacterEqualityCheck {
    public static void main(String args[] ) throws Exception {
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        String[] values=new String[n];
        for(int i=0;i<n;i++){
            values[i]=scanner.next();
        }
        for (String value:
             values) {
            print(value);
        }
    }

    private static void print(String str) {

        int upper = 0, lower = 0, number = 0, special = 0;
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if (ch >= 'A' && ch <= 'Z')
                upper++;
            else if (ch >= 'a' && ch <= 'z')
                lower++;
            else if (ch >= '0' && ch <= '9')
                number++;
            else
                special++;
        }
        if(upper%2== 0&&lower%2==0&&number%2==0&&special%2==0) {
            System.out.println("No Equality");
        }else{
            System.out.println("Equality For Everyone");
        }
    }
}
