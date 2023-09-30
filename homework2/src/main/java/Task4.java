import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Task4 {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введите строку (непустую!): ");
        try{
            String userString = reader.readLine();
            if (userString.equals("")) throw new RuntimeException("Нельзя вводить пустую строку");
            System.out.println(userString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
