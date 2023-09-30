import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Task1 {
    public static void main(String[] args){
        boolean exitFromApp = false;
        while (!exitFromApp) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Введите дробное число: ");
            try {
                float floatNumber = Float.parseFloat(bufferedReader.readLine());
                System.out.printf("Введеное число: %f", floatNumber);
                exitFromApp = true;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Неверный ввод. Введите дробное число!\n");
            }
        }
    }
}

