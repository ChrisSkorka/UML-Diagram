import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Class {

    private static final int MAX_TEXT_WIDTH = 300;
    public static final int PADDING = 10;
    private static final int ARROW_SIZE = 5;
    private static final int CHILD_PARENT_SPACING = 20;

    // class definition
    private String className;
    private String superClassName;
    private ArrayList<String> properties = new ArrayList<>();
    private ArrayList<String> methods = new ArrayList<>();
    private ArrayList<Class> subclasses = new ArrayList<>();

    // texts
    WrappedText classNameWrappedText;
    WrappedText propertiesWrappedText;
    WrappedText methodsWrappedText;

    // layout
    private int classNameHeight;
    private int propertiesHeight;
    private int methodsHeight;
    private int blockWidth;
    private int blockHeight;
    private int subclassesWidth;
    private int subclassesHeight;
    private int subclassesVerticalSpacing = 0;

    private int width;
    private int height;

    public Class(String className, String superClassName){
        this.className = className;
        this.superClassName = superClassName;
    }

    void addSubclass(Class subclass){
        subclasses.add(subclass);
    }

    void addMethod(String method){
        methods.add(method);
    }

    void addProperty(String property){
        properties.add(property);
    }

    void layout(Graphics graphics){

        classNameWrappedText = new WrappedText(className, graphics, MAX_TEXT_WIDTH, WrappedText.FONT_TITLE);
        propertiesWrappedText = new WrappedText(properties.toArray(new String[]{}), graphics, MAX_TEXT_WIDTH, WrappedText.FONT_NORMAL);
        methodsWrappedText = new WrappedText(methods.toArray(new String[]{}), graphics, MAX_TEXT_WIDTH, WrappedText.FONT_NORMAL);

        classNameHeight = classNameWrappedText.getHeight() + 2* PADDING;
        propertiesHeight = properties.isEmpty() ? 0 : propertiesWrappedText.getHeight() + 2* PADDING;
        methodsHeight = methods.isEmpty() ? 0 : methodsWrappedText.getHeight() + 2* PADDING;
        blockHeight = classNameHeight + propertiesHeight + methodsHeight;

        int classNameWidth = classNameWrappedText.getWidth() + 2* PADDING;
        int propertiesWidth = propertiesWrappedText.getWidth() + 2* PADDING;
        int methodsWidth = methodsWrappedText.getWidth() + 2* PADDING;
        blockWidth = Math.max(Math.max(classNameWidth, propertiesWidth), methodsWidth);


        if(!subclasses.isEmpty()){

            subclassesWidth = -PADDING;
            subclassesHeight = 0;
            subclassesVerticalSpacing = 2*CHILD_PARENT_SPACING;

            for(Class subclass : subclasses){
                subclass.layout(graphics);
                subclassesWidth += subclass.getWidth() + PADDING;
                subclassesHeight = Math.max(subclassesHeight, subclass.getHeight());
            }

        }

        width  = Math.max(subclassesWidth, blockWidth);
        height = blockHeight + subclassesVerticalSpacing + subclassesHeight;
    }

    void draw(Graphics2D graphics){

        AffineTransform relativeOrigin = graphics.getTransform();
        int blockX = (width - blockWidth) / 2;
        int titleX = (blockWidth - classNameWrappedText.getWidth()) / 2;

        // draw class name block and translate to bottom of it
        graphics.translate(blockX, 0);
        graphics.drawRect(0, 0, blockWidth, classNameHeight);
        graphics.translate(titleX, PADDING);
        classNameWrappedText.draw(graphics);
        graphics.translate(-titleX, classNameHeight - PADDING);

        // draw properties block and translate to bottom of it
        if(properties.size() > 0) {
            graphics.drawRect(0, 0, blockWidth, propertiesHeight);
            graphics.translate(PADDING, PADDING);
            propertiesWrappedText.draw(graphics);
            graphics.translate(-PADDING, propertiesHeight - PADDING);
        }

        // draw methods block and translate to bottom of it
        if(methods.size() > 0) {
            graphics.drawRect(0, 0, blockWidth, methodsHeight);
            graphics.translate(PADDING, PADDING);
            methodsWrappedText.draw(graphics);
            graphics.translate(-PADDING, methodsHeight - PADDING);
        }

        if(!subclasses.isEmpty()){
            int center = width / 2;

            graphics.setTransform(relativeOrigin);

            // arrow into this block
            graphics.fillPolygon(new int[]{center, center+ARROW_SIZE, center-ARROW_SIZE}, new int[]{blockHeight, blockHeight+ARROW_SIZE, blockHeight+ARROW_SIZE}, 3);
            graphics.drawLine(center, blockHeight+ARROW_SIZE, center, blockHeight + CHILD_PARENT_SPACING);

            // horizontal line
            int leftConnection = (width - subclassesWidth) / 2 + subclasses.get(0).getWidth() / 2;
            int rightConnection = width - subclasses.get(subclasses.size()-1).getWidth() / 2 - (width - subclassesWidth) / 2;
            graphics.drawLine(leftConnection, blockHeight + CHILD_PARENT_SPACING, rightConnection, blockHeight + CHILD_PARENT_SPACING);

            graphics.translate((width - subclassesWidth) / 2, blockHeight + 2*CHILD_PARENT_SPACING);

            for(Class subclass : subclasses){
                graphics.drawLine(subclass.getWidth()/2, 0, subclass.getWidth()/2, -CHILD_PARENT_SPACING);
                subclass.draw(graphics);
                graphics.translate(subclass.getWidth() + PADDING, 0);
            }

        }

        graphics.setTransform(relativeOrigin);

    }

    public int getWidth(){
        return width;
    }

    public  int getHeight(){
        return height;
    }

    public String getClassName(){
        return className;
    }

    public String getSuperClassName(){
        return superClassName;
    }

    public String toString(){
        String string = "class " + className + " extends " + superClassName;

        for(String property : properties)
            string += "\n" + property;

        for(String method : methods)
            string += "\n" + method;

        if(!subclasses.isEmpty()){
            string += "\n(";
            for(Class subclass : subclasses)
                string += "\n" + subclass;
            string += "\n)";
        }

        return string;
    }

}
