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

    private static JFrame frame;
    private static ArrayList<Class> roots;

    /**
     * create a visual UML of a class tree structure
     * @param args
     */
    public static void main(String args[]){

        if(args.length < 1){
            System.out.println("No file path given");
            return;
        }
        String file = args[0];

//        String file = "X:\\GitHub\\3802ICT-Modelling-and-Visualisation-Assignments\\UML\\out\\production\\UML\\classes1.txt";
//        String file = "C:\\Users\\chris\\GitHub\\3802ICT-Modelling-and-Visualisation-Assignments\\UML\\out\\production\\UML\\classes1.txt";

        roots = parseClassFile(file);

        UML UMLDiagram = new UML();

        frame = new JFrame("UML Diagram");
        frame.setContentPane(UMLDiagram);
        frame.setSize(100, 100);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * parse a classes definition file into a class tree structure
     * @param file
     * @return
     */
    private static ArrayList<Class> parseClassFile(String file){

        // class className extends superClassName
        Pattern subclassPattern = Pattern.compile("class (.+) extends (.+)");

        // class className
        Pattern classPattern = Pattern.compile("class (.+)");

        // +|- methodName([arguments]): methodType
        Pattern methodPattern = Pattern.compile(".+\\(.*\\):.+");

        // +|- propertyName: propertyType
        Pattern propertyPattern = Pattern.compile(".+:.+");

        ArrayList<Class> roots = new ArrayList<>();
        Class last = new Class("",""); // initial dummy class

        // create class table with object as initial entry
        HashMap<String, Class> classes = new HashMap<>();

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

                    last = new Class(className, null);
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
            return roots;
        }catch (IOException e){
            System.err.println("File could not be opened");
            return roots;
        }

        // build tree structure
        for(Map.Entry<String, Class> entry : classes.entrySet()){
            Class subclass = entry.getValue();
            String superClassName = subclass.getSuperClassName();

            if(superClassName == null)
                roots.add(subclass);
            else
                classes.get(superClassName).addSubclass(subclass);

        }

        return roots;
    }

    /**
     * this window
     */
    public UML(){
        super();
        setPreferredSize(new Dimension(1,1));
        setFocusable(true);
    }

    /**
     * layout and draws all root classes which draw their subclasses
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {

        // calculate overall size of UML diagram
        int width = -Class.PADDING;
        int height = 0;

        // layout UML and compute sizes
        for(Class root : roots) {
            root.layout(g);
            width += root.getWidth() + Class.PADDING;
            height = Math.max(height, root.getHeight());
        }

        // set window size with padding
        width += 2*Class.PADDING + 16;
        height += 2*Class.PADDING + 40;
        frame.setSize(width, height);

        // create image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // define colours
        Color white = Color.WHITE;
        Color black = Color.BLACK;

        // background
        graphics.setColor(white);
        graphics.fillRect(0, 0, width, height);

        graphics.setColor(black);

        // draw each root class and this subclasses
        graphics.translate(Class.PADDING, Class.PADDING);
        for(Class root : roots){
            root.draw(graphics);
            graphics.translate(root.getWidth() + Class.PADDING, 0);
        }

        // update image to screen
        g.drawImage(image, 0, 0, null);
    }

}
