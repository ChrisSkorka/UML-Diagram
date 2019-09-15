import java.util.ArrayList;

public class WrappedText {

    private String source;
    private ArrayList<String> strings = new ArrayList<>();
    private int width = 0;
    private int height = 0;

    public WrappedText(String text, int maxWidth){
        this.source = text;
    }

    public String toString(){
        return source;
    }

}
