import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class Program {
//    Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке,
//    разделенные пробелом: Фамилия Имя Отчество, дата_рождения, номер_телефона, пол
//    Форматы данных:фамилия,имя, отчество - строки;
//    дата_рождения- строка формата dd.mm.yyyy
//    номер_телефона - целое беззнаковое число без форматирования
//    пол - символ латиницей f или m.
//    Критерии:
//             Приложение должно проверить введенные данные по количеству. Если количествоне совпадает с требуемым,
//    вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше
//    данных, чем требуется.
//             Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры.
//    Если форматы данных не совпадают,нужно бросить исключение, соответствующее типу проблемы. Можно
//    использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано,
//    пользователю выведено сообщение с информацией, что именно неверно.
//             Если всё введено и обработано верно, должен создаться файл с названием,равным фамилии, в него в одну
//    строку должны записаться полученные данные, вида
//    <Фамилия><Имя><Отчество><дата рождения><номер телефона><пол>
//    Однофамильцы должны записывать в один и тот же файл, в отдельные строки


    public static void main(String[] args){

        // Получаем информацию от пользователя
        String infoAboutUserInFormat = getInfoFromUser();

        // Убираем запятые для более удобной последующей проверки
        String infoAboutUserWithoutCommas = removeCommas(infoAboutUserInFormat);

        // Проверяем корректность количества введенных параметров пользователем
        int resOfCor1 = checkCorrectnessOfNumberOfInputParameters(infoAboutUserWithoutCommas);

        // Обработка кода ошибки для пользователя
        if (resOfCor1 == -1) {
            System.out.println("Ошибка: количество данных меньше 6.");
            return;
        } else if (resOfCor1 == -2) {
            System.out.println("Ошибка: количество данных больше 6.");
            return;
        } else {
            // Создаем массив строк для более удобной обработки
            String[] infoInArray = new String[6];
            // Создаем временный StringBuilder для формирования строк
            StringBuilder temp = new StringBuilder();
            // Создаем индекс для массива строк
            int indexOfArrayString = 0;
            // Проходимся по всей строке от пользователя для вычленения параметров
            for (int i = 0; i < infoAboutUserWithoutCommas.length(); i++) {
                char currentChar = infoAboutUserWithoutCommas.charAt(i);

                if (currentChar != ' ') {
                    temp.append(currentChar);
                } else {
                    infoInArray[indexOfArrayString] = temp.toString();
                    // Очищаем временную переменную после добавления параметра в массив
                    // и увеличиваем индекс массива строк
                    temp.setLength(0);
                    indexOfArrayString++;
                }
                // Добавляем последний параметр в массив
                if (i == infoAboutUserWithoutCommas.length() - 1) {
                    infoInArray[indexOfArrayString] = temp.toString();
                    indexOfArrayString++;
                }
            }
            String surName = infoInArray[0];
            String name = infoInArray[1];
            String patronymic = infoInArray[2];
            String dateOfBirth = infoInArray[3];
            String phoneNumberString = infoInArray[4];
            String sex = infoInArray[5];
            try {
                // Проверка фамилии
                if (!surName.matches("^[а-яА-Яa-zA-Z]+$")) {
                    throw new SurNameException();
                }

                // Проверка имени
                if (!name.matches("^[а-яА-Яa-zA-Z]+$")) {
                    throw new NameException();
                }

                // Проверка отчества
                if (!patronymic.matches("^[а-яА-Яa-zA-Z]+$")) {
                    throw new PatronymicException();
                }

                // Проверка и парсинг даты рождения
                if (dateOfBirth.length() != 10) {
                    throw new LengthOfDateOfBirthException();
                } else {
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        simpleDateFormat.setLenient(false); // Отключаем мягкий режим для выявления недопустимых дат
                        simpleDateFormat.parse(dateOfBirth);
                        } catch (ParseException e) {
                            throw new IncorrectDateOfBirthException(dateOfBirth, e.getErrorOffset());
                        }

                    // Проверка и парсинг номера телефона
                    if (!phoneNumberString.matches("\\d+")) {
                        throw new PhoneNumberException();
                    }

                    // Проверка пола
                    if (!sex.equals("m") && !sex.equals("f")) {
                        throw new SexException();
                    }

                    // Формируем строку для записи в файл
                    String dataForFile = String.format("<%s><%s><%s><%s><%s><%s>", surName, name, patronymic, dateOfBirth, phoneNumberString, sex);
                    String fileName = surName + ".txt";
                    File file = new File(fileName);
                    if (file.exists()) {
                        try {
                            Files.write(file.toPath(), (System.lineSeparator() + dataForFile).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                            System.out.println("Данные добавлены в файл: " + fileName + ".");
                        } catch (IOException e) {
                            throw new MyIOExceptionInExistedFile();
                        }
                    } else {
                        try {
                            FileWriter fileWriter = new FileWriter(fileName);
                            fileWriter.write(dataForFile);
                            fileWriter.close();
                            System.out.println("Файл: " + fileName + "создан и записан.");
                        } catch (IOException e) {
                            throw new MYIOExceptionNewFile();
                        }
                        System.out.println(dataForFile);

//
                    }
                }} catch(SurNameException | NameException | PatronymicException | LengthOfDateOfBirthException |
                         PhoneNumberException | SexException | MyIOExceptionInExistedFile | MYIOExceptionNewFile |
                         IncorrectDateOfBirthException e){
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }


    // Метод для получения данных от пользователя в виде одной строки
    public static String getInfoFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные в формате 'Фамилия Имя Отчество, дата_рождения, номер_телефона, пол': ");
        String userInput = scanner.nextLine();
        return userInput;
    }

    // Метод для удаления запятых (предварительная обработка)
    public static String removeCommas(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ',') {
                result.append(text.charAt(i));
            }
        }
        return result.toString();
    }

    // Метод для проверки количества веденных параметров пользователем
    public static int checkCorrectnessOfNumberOfInputParameters(String userInput) {
        String[] parts = userInput.split(" ");
        if (parts.length < 6) {
            return -1;
        }
        if (parts.length > 6) {
            return -2;
        }
        return 0;
    }
}

//region MyExceptions

// Исключение для обработки некорректного ввода фамилии
class SurNameException extends Exception{
    public SurNameException(){
        super("Фамилия должна включать только буквы!");
    }
}

// Исключение для обработки некорректного ввода имени
class NameException extends Exception{
    public NameException(){
        super("Имя должно включать только буквы!");
    }
}

// Исключение для обработки некорректного ввода отчества
class PatronymicException extends Exception{
    public PatronymicException(){
        super("Отчество должно включать только буквы!");
    }
}

// Исключение для обработки некорректной длины символов даты рождения
class LengthOfDateOfBirthException extends Exception{
    public LengthOfDateOfBirthException(){
        super("Длина даты не соответствует заданной!");
    }
}

// Исключение для обработки некорректной даты рождения
class IncorrectDateOfBirthException extends ParseException{
    public IncorrectDateOfBirthException(String dateString, int getErrorOffset ){
        super("Такой даты не существует: " + dateString, getErrorOffset);

    }
}

// Исключение для обработки некорректного номера телефона
class PhoneNumberException extends NumberFormatException{
    public PhoneNumberException(){
        super("Номер телефона должен содержать только цифры!");
    }
}

// Исключение для некорректного пола
class SexException extends RuntimeException{
    public SexException(){
        super("Пол должен быть записан в формате 'f' или 'm'");
    }
}

// Исключение для обработки некорректного добавления данных в файл
class MyIOExceptionInExistedFile extends IOException{
    public MyIOExceptionInExistedFile(){
        super("Возникла ошибка при добавлении данных в файл.");
    }
}

// Исключение для обработки некорректного создания и записи файла
class MYIOExceptionNewFile extends IOException{
    public MYIOExceptionNewFile(){
        super("Возникла ошибка при создании и записи файла.");
    }
}
//endregion