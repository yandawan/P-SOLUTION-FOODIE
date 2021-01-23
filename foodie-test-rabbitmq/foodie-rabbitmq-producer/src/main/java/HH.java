import java.util.Locale;
import java.util.ResourceBundle;

public class HH {
public static void main(String[] args) {
    Runnable r = () -> System.out.println("HI");
    new Thread(r).start();
}
}