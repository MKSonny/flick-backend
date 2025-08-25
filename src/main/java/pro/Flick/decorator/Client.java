package pro.Flick.decorator;

public class Client {

    private Component component;

    public Client(Component component) {
        this.component = component;
    }

    public void execute() {
        String result = component.operation();
        System.out.println("result = " + result);
    }
}
