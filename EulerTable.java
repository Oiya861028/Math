import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Vector;

public class EulerTable {
    private JFrame frame;
    private JTable table;
    
    public void displayResults(double[] tValues, double[] yValues, 
                             double[] fPrimeValues, double[] errorValues, 
                             boolean hasExact) {
        // Create table model
        String[] columnNames = {"t", "y", "f'(t,y)", "Error (%)"};
        Vector<Vector<Object>> data = new Vector<>();

        // Populate data
        for (int i = 0; i < tValues.length; i++) {
            Vector<Object> row = new Vector<>();
            row.add(String.format("%.3f", tValues[i]));
            row.add(String.format("%.3f", yValues[i]));
            row.add(String.format("%.3f", fPrimeValues[i]));
            row.add(hasExact ? String.format("%.3f", errorValues[i]) : "N/A");
            data.add(row);
        }

        // Create and configure JTable
        table = new JTable(data, new Vector<>(java.util.Arrays.asList(columnNames)));
        frame = new JFrame("Euler Approximation Results");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Style the table
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 12));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        
        // Display the window
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}