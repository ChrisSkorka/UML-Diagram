import java.awt.*;
import java.util.ArrayList;

public class Class {


    private static final int width = 100;
    private static final int height = 30;
    private static final int spacingV = 50;
    private static final int spacingH = 10;

    public String className;
    public String superClass;
    private ArrayList<Class> children;

    public Class(String className, String superClass){
        this.className = className;
        this.superClass = superClass;
        children = new ArrayList<>();
    }

    boolean add(Class newObject){

        if(newObject.superClass.equals(className)){
            children.add(newObject);
            return true;
        }

        for(int i = 0; i < children.size(); i++){
            Class child = children.get(i);

            if(child.add(newObject))
                return true;
        }

        return false;
    }

    void tree(int x){

        for(int i = 0; i < x; i++)
            System.out.print("|   ");
        System.out.print("|-");

        System.out.println(className +" extends "+ superClass);

        for(int i = 0; i < children.size(); i++){
            Class child = children.get(i);
            child.tree(x+1);
        }
    }

    void draw(Graphics graphics, int x, int y){

        graphics.drawRoundRect(x, y, width, height, 8, 8);
        graphics.drawString(className, x+10, y+20);

        if(children.size() > 0){

            int triangleConnectorX = x + width/2;
            int triangleConnectorY = y + height;

            int treeConnectorX = triangleConnectorX;
            int treeConnectorY = triangleConnectorY + spacingV/2;

            int[] triangleX = {triangleConnectorX, triangleConnectorX+7, triangleConnectorX+6, triangleConnectorX-6, triangleConnectorX-7};
            int[] triangleY = {triangleConnectorY, triangleConnectorY+9, triangleConnectorY+10, triangleConnectorY+10, triangleConnectorY+9};

            //int[] triangleX = {triangleConnectorX, triangleConnectorX+7, triangleConnectorX-7};
            //int[] triangleY = {triangleConnectorY, triangleConnectorY+10, triangleConnectorY+10};

            graphics.drawPolygon(triangleX, triangleY, triangleX.length);
            graphics.drawLine(triangleConnectorX, triangleConnectorY+10, treeConnectorX, treeConnectorY);


            y += height + spacingV;
            int treeConnectorEnd = treeConnectorX;

            for(int i = 0; i < children.size(); i++){
                Class child = children.get(i);

                if(i == 0)
                    graphics.drawLine(x + width/2, treeConnectorY, x + width/2, y);
                else
                    graphics.drawArc(x + width/2 - 10, treeConnectorY, 10, 10, 0, 90);

                graphics.drawLine(x + width/2, treeConnectorY+5, x + width/2, y);
                treeConnectorEnd = Math.max(treeConnectorEnd, x + width/2);

                child.draw(graphics, x, y);
                x += child.getSize().width + spacingH;
            }

            if(children.size() > 1){

                graphics.drawLine(treeConnectorX + 5, treeConnectorY, treeConnectorEnd - 5, treeConnectorY);
                graphics.drawArc(treeConnectorX, treeConnectorY - 10, 10, 10, 180, 90);
            }
        }
    }

    public Dimension getSize(){
        Dimension size = new Dimension(0, 0);

        size.width = width + spacingH;
        size.height = height;

        int childHeight = 0;
        int x = 0;
        for(int i = 0; i < children.size(); i++){
            Class child = children.get(i);

            Dimension childSize = child.getSize();

            childHeight = Math.max(childHeight, childSize.height);
            size.width = Math.max(size.width, x + childSize.width);

            x += childSize.width + spacingH;
        }

        if(children.size() > 0){
            size.height += spacingV + childHeight;
        }

        return size;
    }

}
