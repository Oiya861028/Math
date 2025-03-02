import java.lang.Math;
public class Euler_Approx {
    static double t;
    static double y;
    public static void main(String[] args) {
        //Initial Conditions
        t = 0;
        y = 1;
        //Step Size
        double h = 0.1;
        //Number of Iterations
        int n = 10;
        //Print Initial Conditions
        System.out.println("t = " + t + " y = " + y +"\n");
        //Print Approximations
        for (int i = 0; i < n; i++) {
            //Pass in the current t and y values to the function f_prime
            y = y + h * f_prime(t, y);
            t = t + h; //Increment t by the step size
            double exact = exact_solution(t); //Find the exact value
            System.out.println("Exact = " + exact);
            System.out.println("t = " + t + " y = " + y);
            System.out.println("Percent Error = " + Math.abs((exact - y) / exact) * 100 + "%");
            System.out.println();
        }

    }
    public static double f_prime(double t, double y) {
        return 2 - Math.pow(Math.E, -4 * t) - 2 * y;
    }
    public static double exact_solution(double t) {
        return -Math.pow(Math.E, -2 * t)/2 + Math.pow(Math.E, -4 * t)/2 + 1;
    }
}