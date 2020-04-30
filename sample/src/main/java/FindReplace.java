import java.util.Scanner;

/**
 * Created by Suriyanarayanan K
 * on 20/04/20 1:22 PM.
 */
public class FindReplace {


    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        //HELLO THERE, WELCOME TO HELL ON EARTH IN THE WELL OF LIFE
        String str=scanner.nextLine();
        String search=scanner.nextLine();
        String replace=scanner.nextLine();
        StringBuffer originalStrng=new StringBuffer(str);

        for (int i = 1; i < originalStrng.length(); i++)
        {
            if(searchIf(search,originalStrng.toString().toCharArray(),i)){
                String tempString=new String(replaceFunc(search,replace,originalStrng.toString().toCharArray(),i));
                originalStrng=new StringBuffer(tempString);
                originalStrng.append(str.toCharArray()[str.length()-1]);

            }
        }
        System.out.println("Transformed String :"+originalStrng);
    }

    private static char[] replaceFunc(String search, String replace, char[] changedString, int i) {
        StringBuffer stringBuffer=new StringBuffer();
        for(int k=0;k<i-1;k++){
            stringBuffer.append(changedString[k]);
        }
        stringBuffer.append(replace);
        for(int k=i+search.length()-1;k<changedString.length-1;k++){
                stringBuffer.append(changedString[k]);
        }
        return stringBuffer.toString().toCharArray();
    }

    private static boolean searchIf(String search, char[] changedString, int i) {
        String temp="";
        temp+=changedString[i-1];
        for(int j=0;j<search.length()-1;j++){
            if(j<changedString.length-1 && i + j < changedString.length-1 ) {
                temp += changedString[i + j];
            }
        }
        return (temp.equals(search));
    }
}
