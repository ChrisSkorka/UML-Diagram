import java.awt.*;
import java.util.ArrayList;

public class WrappedText {

    private String source = "";
    private ArrayList<String> lines = new ArrayList<>();
    private int width = 0;
    private int height = 0;
    private int lineHeight = 0;
    private Font font;

    public static final Font FONT_NORMAL = new Font("Arial", Font.PLAIN, 15);
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 15);
    public static final int LINE_SPACING = 5;

    public WrappedText(String[] strings, Graphics g, int maxWidth, Font font){

        this.font = font;

        FontMetrics fontMetrics = g.getFontMetrics(font);
        lineHeight = fontMetrics.getHeight();

        // add each string below one another
        for(String multiLineText : strings) {

            // maybe too long
            if(!source.isEmpty())
                source += "\n";
            source += multiLineText;
            lines.add(multiLineText);

            // while last line is too long, split it at ' ' characters
            while (fontMetrics.stringWidth(lines.get(lines.size() - 1)) > maxWidth) {
                String string = lines.get(lines.size() - 1);

                int cut = string.length();
                String before = string;
                while (fontMetrics.stringWidth(before) > maxWidth) {

                    // if not more spaces, ie need to cut word, find last character to cut
                    if (!before.contains(" ")) cut--;

                        // find next space to cut by
                    else cut = before.lastIndexOf(' ', cut - 1);

                    // if(cut < 1) cut = 1;

                    before = before.substring(0, cut);
                }

                String after = "    " + string.substring(cut + 1); // omit ' ' itself

                lines.set(lines.size() - 1, before);
                lines.add(after);

            }
        }

        // compute size
        for(String line : lines){
            int w = fontMetrics.stringWidth(line);
            if(w > width)width = w;
        }
        height = lines.isEmpty() ? 0 : lines.size() * (lineHeight + LINE_SPACING) - LINE_SPACING;
    }

    public WrappedText(String text, Graphics g, int maxWidth, Font font){
        this(new String[]{text}, g, maxWidth, font);
    }

    public void draw(Graphics graphics){

        graphics.setFont(font);

        int y = lineHeight;
        for(String line : lines){
            graphics.drawString(line, 0, y);
            y += lineHeight + LINE_SPACING;
        }

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
