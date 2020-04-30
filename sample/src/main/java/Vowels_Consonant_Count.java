
import java.util.*;
public class Vowels_Consonant_Count {
    public static void main(String args[] ) throws Exception {
      Scanner scanner=new Scanner(System.in);
      int n=scanner.nextInt();
      String[] values=new String[n];
      for(int i=0;i<n;i++){
          values[i]=scanner.next();
      }
       for(int i=0;i<n;i++){
            int vowelscount=print(values[i]);
            int consonant=values[i].length()-vowelscount;
            System.out.print(vowelscount+" "+consonant+" "+vowelscount*consonant);
            System.out.print("\n");
       }
    }
    public static int print(String value){
        int count=0;
        for(int i=0;i<value.length();i++){
          char ch = value.charAt(i);
           if(ch == 'a'|| ch == 'e'|| ch == 'i' ||ch == 'o' ||ch == 'u'||ch == ' '){
            count ++;
         }
        }
        return count;
    }
}
