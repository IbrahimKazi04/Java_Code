// Using method overloading to calculate different shape areas

class Shapes {
    
    public double area(double r) {
        double result = 3.14159 * r * r;
        return result;
    }

    public double area(int len, int wid) {
        return len * wid;
    }

    public double area(float side) {
        return side * side;
    }
 
    public double area(double b, double h, String shapeType) {
        if(shapeType.equals("triangle")) {
            return (b * h) / 2.0;
        }
        return 0.0;
    }
}

public class Version2_Shapes {
    public static void main(String[] args) {
        Shapes s = new Shapes();
        
        System.out.println("*** Shape Area Calculator ***");
        System.out.println();

        System.out.println("Circle with radius 5.5: " + s.area(5.5) + " sq.units");

        System.out.println("Rectangle 12 x 8: " + s.area(12, 8) + " sq.units");

        System.out.println("Square with side 6.5: " + s.area(6.5f) + " sq.units");

        System.out.println("Triangle base=10, height=7: " + s.area(10.0, 7.0, "triangle") + " sq.units");
    }
}
