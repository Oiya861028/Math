import java.lang.Math;
public class Euler_Approx {
    static double t;
    static double y;
    public static void main(String[] args) {
        
        t = 0;
        y = 1;
        double h = 0.1;
        int n = 10;
        System.out.println("t = " + t + " y = " + y);
        for (int i = 0; i < n; i++) {
            y = y + h * f_prime(t, y);
            t = t + h;
            double exact = -Math.pow(Math.E, -2 * t)/2 + Math.pow(Math.E, -4 * t)/2 + 1;
            System.out.println("Exact = " + exact);
            System.out.println("t = " + t + " y = " + y);
            System.out.println("Percent Error = " + Math.abs((exact - y) / exact) * 100 + "%");
            System.out.println(" ");
        }

    }
    public static double f_prime(double t, double y) {
        return 2 - Math.pow(Math.E, -4 * t) - 2 * y;
    }
}