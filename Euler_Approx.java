import java.lang.Math;
import java.util.function.BiFunction;
import java.io.FileWriter;
import java.io.IOException;
//Find main function and input your function and initial values
//Run the program and it will generate a CSV file and a graph of the function

public class Euler_Approx {
    private final BiFunction<Double, Double, Double> f_prime;
    private final BiFunction<Double, Double, Double> exact_solution;
    private final double stepSize;
    private final double startT;
    private final double endT;
    private final double initialY;
    private final boolean hasExact;
    private final int decimalPlaces;

    public static class Result {
        public final double[] tValues;
        public final double[] yValues;
        public final double[] fPrimeValues;
        public final double[] errorValues;

        public Result(double[] t, double[] y, double[] fp, double[] err) {
            this.tValues = t;
            this.yValues = y;
            this.fPrimeValues = fp;
            this.errorValues = err;
        }
    }

    public Euler_Approx(BiFunction<Double, Double, Double> f_prime, 
                       double stepSize, 
                       double startT, 
                       double endT, 
                       double initialY) {
        this(f_prime, null, stepSize, startT, endT, initialY, 3);
    }

    public Euler_Approx(BiFunction<Double, Double, Double> f_prime,
                       BiFunction<Double, Double, Double> exact_solution,
                       double stepSize,
                       double startT,
                       double endT,
                       double initialY,
                       int decimalPlaces) {
        this.f_prime = f_prime;
        this.exact_solution = exact_solution;
        this.stepSize = stepSize;
        this.startT = startT;
        this.endT = endT;
        this.initialY = initialY;
        this.hasExact = (exact_solution != null);
        this.decimalPlaces = decimalPlaces;
    }

    public Result solve() {
        int n = (int) ((endT - startT) / stepSize) + 1;
        double scale = Math.pow(10, decimalPlaces);
        
        // Create arrays for numerical solution
        double[] tValues = new double[n];
        double[] yValues = new double[n];
        double[] fPrimeValues = new double[n];
        double[] errorValues = new double[n];

        // Create arrays for exact solution with more points
        int exactPoints = n * 10; // 10 times more points for smoother curve
        double[] tExact = new double[exactPoints];
        double[] yExact = new double[exactPoints];
        double exactStepSize = stepSize / 10;

        // Initialize and compute solutions
        tValues[0] = startT;
        yValues[0] = initialY;
        fPrimeValues[0] = f_prime.apply(startT, initialY);
        errorValues[0] = 0.0;

        // Perform Euler approximation
        for (int i = 1; i < n; i++) {
            tValues[i] = startT + i * stepSize;
            yValues[i] = Math.round((yValues[i-1] + stepSize * fPrimeValues[i-1]) * scale) / scale;
            fPrimeValues[i] = f_prime.apply(tValues[i], yValues[i]);
            
            if (hasExact) {
                double exact = exact_solution.apply(tValues[i], yValues[i]);
                errorValues[i] = Math.abs((exact - yValues[i]) / exact) * 100;
            }
        }

        // Generate more points for exact solution
        if (hasExact) {
            try (FileWriter writer = new FileWriter("exact_solution.csv")) {
                writer.write("t,y_exact\n");
                for (int i = 0; i < exactPoints; i++) {
                    double t = startT + i * exactStepSize;
                    if (t <= endT) {
                        double exact = exact_solution.apply(t, 0.0); // y value doesn't matter for t-only solutions
                        writer.write(String.format("%f,%f\n", t, exact));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new Result(tValues, yValues, fPrimeValues, errorValues);
    }

    //Exact solution can be null, you just have to set hasExact to false, result.errorValues will be 0.0, and pass in null for exact_solution
    public static void main(String[] args) {
        BiFunction<Double, Double, Double> f_prime = (t, y) -> t - Math.pow(y, 2);    
        BiFunction<Double, Double, Double> exact_solution = (t, y) -> Math.asin(t - Math.sin(1));  // Exact solution
        
       
        Euler_Approx euler = new Euler_Approx(
            f_prime,      // differential equation
            exact_solution, // exact solution
            0.25,        // step size
            0.0,         // start t
            2.0,         // end t
            1.0,          // initial y(0)
            3            // decimal places
        );
        
        Result result = euler.solve();

        // Display results using your preferred visualization method
        EulerTable table = new EulerTable();
        table.displayResults(result.tValues, result.yValues, 
                           result.fPrimeValues, result.errorValues, true);  // false since no exact solution
        
        // Write data to CSV and create graph
        try {
            // Write CSV file
            try (FileWriter writer = new FileWriter("euler_data.csv")) {
                writer.write("t,y,f_prime,error\n");
                for (int i = 0; i < result.tValues.length; i++) {
                    writer.write(String.format("%f,%f,%f,%f\n", 
                        result.tValues[i], result.yValues[i], 
                        result.fPrimeValues[i], result.errorValues[i]));  // Use 0.0 for error when no exact solution
                }
            }

            // Run Python script
            ProcessBuilder pb = new ProcessBuilder("python", "plot_euler.py");
            pb.inheritIO(); // This will show Python's output in Java's console
            Process p = pb.start();
            p.waitFor(); // Wait for Python script to complete

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}