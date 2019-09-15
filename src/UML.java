import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UML extends JPanel {

    private static Class object;
    private static Dimension size;
    private static final int padding = 10;

    public static void main(String args[]){

//        if(args.length < 1){
//            System.out.println("No file path given");
//            return;
//        }
//        String file = args[0];

        String file = "X:\\GitHub\\3802ICT-Modelling-and-Visualisation-Assignments\\UML\\out\\production\\UML\\classes1.txt"; // args[0];
        object = parseClassFile(file);

        size = object.getSize();
        size.width += 2*padding;
        size.height += 2*padding;

        size.setSize(500,500);

        UML UMLDiagram = new UML();

        JFrame frame = new JFrame("UML Diagram");
        frame.setContentPane(UMLDiagram);
        frame.setSize(size.width, size.height);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static Class parseClassFile(String file){

        // class className extends superClassName
        Pattern subclassPattern = Pattern.compile("class (.+) extends (.+)");

        // class className
        Pattern classPattern = Pattern.compile("class (.+)");

        // +|- methodName([arguments]): methodType
        Pattern methodPattern = Pattern.compile(".+\\(.*\\):.+");

        // +|- propertyName: propertyType
        Pattern propertyPattern = Pattern.compile(".+:.+");

        final String objectName = "Object";
        Class object = new Class(objectName, null);
        Class last = object;

        // create class table with object as initial entry
        HashMap<String, Class> classes = new HashMap<>();
        classes.put(objectName, object);

        // ready classes from file into table
        try(
            BufferedReader br = new BufferedReader(new FileReader(file))
        ){
            String line;
            while((line = br.readLine()) != null){

                // class className extends superClassName
                Matcher matchSubclass = subclassPattern.matcher(line);
                if(matchSubclass.find()){

                    String className = matchSubclass.group(1);
                    String superClassName = matchSubclass.group(2);

                    last = new Class(className, superClassName);
                    classes.put(className, last);

                    continue;
                }

                // class className
                Matcher matchClass = classPattern.matcher(line);
                if(matchClass.find()){

                    String className = matchClass.group(1);

                    last = new Class(className, objectName);
                    classes.put(className, last);

                    continue;
                }

                // +|- methodName([arguments]): methodType
                Matcher matchMethod = methodPattern.matcher(line);
                if(matchMethod.find()){

                    last.addMethod(matchMethod.group(0));

                    continue;
                }

                // +|- propertyName: propertyType
                Matcher matchProperty = propertyPattern.matcher(line);
                if(matchProperty.find()){

                    last.addProperty(matchProperty.group(0));

                    continue;
                }

                // System.out.println("Uninterpreted line: '"+line+"'");
            }
        }catch(FileNotFoundException e){
            System.err.println("File not found");
            return object;
        }catch (IOException e){
            System.err.println("File could not be opened");
            return object;
        }

        // build tree structure
        for(Map.Entry<String, Class> entry : classes.entrySet()){
            Class subclass = entry.getValue();
            String superClassName = subclass.getSuperClassName();

            if(superClassName != null)
                classes.get(superClassName).addSubclass(subclass);

        }

        return object;
    }

    public UML(){
        super();
        setPreferredSize(size);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color white = Color.decode("0xFEFEFE");
        Color black = Color.decode("0x555555");

        graphics.setColor(white);
        graphics.fillRect(0, 0, size.width, size.height);

        graphics.setColor(black);
        // object.draw(graphics, padding, padding);

        WrappedText wrappedText = new WrappedText("Hello here is my super long text that is hopefully gonna be split/wrapped properly and shows that looooooooooooooong words are wrapped around properly and very loooooooooooooooooooooooooooooon words are cut", g, 300);

        int y = wrappedText.getLineHeight();
        for(String line : wrappedText.getLines()){
            graphics.drawString(line, 0, y);
            y += wrappedText.getLineHeight() + WrappedText.LINE_SPACING;
        }
        System.out.println(wrappedText.getLineHeight());

        g.drawImage(image, 0, 0, null);
    }

}
