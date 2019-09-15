import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class WrappedText {

    private String source;
    private ArrayList<String> lines = new ArrayList<>();
    private int width = 0;
    private int height = 0;
    private int lineHeight = 10;

    public static final Font FONT = new Font("Arial", Font.PLAIN, 25);
    public static final int LINE_SPACING = 5;

    public WrappedText(String text, Graphics g, int maxWidth){
        this.source = text;

        // maybe too long
        lines.add(source);

        FontMetrics fontMetrics = g.getFontMetrics(FONT);
        // lineHeight = fontMetrics.getHeight();

        // while last line is too long, split it at ' ' characters
        while(fontMetrics.stringWidth(lines.get(lines.size()-1)) > maxWidth){
            String string = lines.get(lines.size()-1);

            int cut = string.length();
            String before = string;
            while(fontMetrics.stringWidth(before) > maxWidth){

                // if not more spaces, ie need to cut word, find last character to cut
                if(!before.contains(" ")) cut--;

                // find next space to cut by
                else cut = before.lastIndexOf(' ', cut-1);

                // if(cut < 1) cut = 1;

                before = before.substring(0, cut);
            }

            String after = string.substring(cut+1); // omit ' ' itself

            lines.set(lines.size()-1, before);
            lines.add(after);

        }

        // compute size
        for(String line : lines){
            int w = fontMetrics.stringWidth(line);
            if(w > width)width = w;
        }
        height = lines.size() * (lineHeight + LINE_SPACING) - LINE_SPACING;

    }

    public ArrayList<String> getLines(){
        return lines;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getLineHeight(){
        return lineHeight;
    }

    public String toString(){
        return source;
    }

}
