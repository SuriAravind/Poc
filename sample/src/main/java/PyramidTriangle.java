import java.util.Scanner;

/**
 * Created by Suriyanarayanan K
 * on 20/04/20 1:14 PM.
 */
public class PyramidTriangle {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        int count=1;
        for (int i=0; i<n; i++)
        {
            for (int j=n-i; j>1; j--)
            {
                System.out.print(" ");
            }
            for (int j=0; j<=i; j++ )
            {
                System.out.print(count+++" ");
            }
            System.out.println();
        }
    }
}
