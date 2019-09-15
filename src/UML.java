import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

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

        String file = "X:\\GitHub\\3802ICT-Modelling-and-Visualisation-Assignments\\UML\\out\\production\\UML\\classes6.txt"; // args[0];
        ArrayList<Class> objects = getClassesFromFile(file);
        if(objects == null)
            return;

        object = sortObjects(objects);

        size = object.getSize();
        size.width += 2*padding;
        size.height += 2*padding;

        UML treeDiagram = new UML();

        JFrame frame = new JFrame("Tree Diagram");
        frame.setContentPane(treeDiagram);
        frame.setSize(size.width, size.height);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static ArrayList<Class> getClassesFromFile(String file){

        ArrayList<Class> objects = new ArrayList<Class>();

        try(
                BufferedReader br = new BufferedReader(
                        new FileReader(file)
                )
        ){
            String line;
            while((line = br.readLine()) != null){

                String[] words = line.split(" ");
                String className = "";
                String superClass = "Object";

                if(words.length >= 2 && words[0].equals("class")){
                    className = words[1];

                    if(words.length == 4 && words[2].equals("extends")){
                        superClass = words[3];
                    }

                    Class newObject = new Class(className, superClass);
                    objects.add(newObject);

                }else{
                    System.out.println("'"+line+"' could not be interpreteded");
                }
            }
        }catch(Exception e){
            System.out.println("File could not be opened");
            return null;
        }

        return objects;
    }

    private static Class sortObjects(ArrayList<Class> objects){

        Class object = new Class("Object", "");

        boolean progressed = true;
        while(progressed){

            progressed = false;

            for(int i = 0; i < objects.size(); i++){
                Class newObject = objects.get(i);

                if(object.add(newObject)){
                    progressed = true;
                    objects.remove(i);
                }

            }
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
        object.draw(graphics, padding, padding);

        g.drawImage(image, 0, 0, null);
    }

}
