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

    /**
     * create multiple text block with text wrapping.
     * words are wrapped at last space within maxWidth or the last character within maxWidth if the line contains no spaces.
     * each line after the first within a text block is indented.
     * @param strings
     * @param g
     * @param maxWidth
     * @param font
     */
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

                // add indent for each new line within a text block
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

    /**
     * create single text block with text wrapping.
     * words are wrapped at last space within maxWidth or the last character within maxWidth if the line contains no spaces.
     * each line after the first within this text block is indented.
     * @param text
     * @param g graphics
     * @param maxWidth
     * @param font
     */
    public WrappedText(String text, Graphics g, int maxWidth, Font font){
        this(new String[]{text}, g, maxWidth, font);
    }

    /**
     * render this text on multiple lines
     * @param graphics
     */
    public void draw(Graphics graphics){

        graphics.setFont(font);

        int y = lineHeight;
        for(String line : lines){
            graphics.drawString(line, 0, y);
            y += lineHeight + LINE_SPACING;
        }

    }

    /**
     * get actual width, smaller than maxWidth if possible, tight bounding box of text
     * @return
     */
    public int getWidth(){
        return width;
    }

    /**
     * get actual height, tight bounding box of text
     * @return
     */
    public int getHeight(){
        return height;
    }

    /**
     * get source text this is created from
     * @return
     */
    public String toString(){
        return source;
    }

}
