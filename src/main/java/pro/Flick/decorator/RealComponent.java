package pro.Flick.decorator;

public class RealComponent implements Component {

    @Override
    public String operation() {
        System.out.println("RealComponent 실행");
        return "데이터";
    }
}
