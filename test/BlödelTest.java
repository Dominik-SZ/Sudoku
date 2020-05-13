import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class BlödelTest {

    public static void main(String[] args) {
        BooleanProperty prop1 = new SimpleBooleanProperty(false);
        BooleanProperty prop2 = new SimpleBooleanProperty(true);
        prop2.bindBidirectional(prop1);

        System.out.println("Property 1: " + prop1.get());
        System.out.println("Property 2: " + prop2.get());
    }
}
