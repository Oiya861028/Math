import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Read the data
df = pd.read_csv('euler_data.csv')
try:
    df_exact = pd.read_csv('exact_solution.csv')
    has_exact = True
except:
    has_exact = False

# Create the plot
plt.figure(figsize=(10, 6))

# Plot Euler approximation
plt.plot(df['t'], df['y'], 'bo-', label='Euler Approximation', markersize=4)

# Plot exact solution if available
if has_exact:
    plt.plot(df_exact['t'], df_exact['y_exact'], 'r-', label='Exact Solution')

# Styling
plt.grid(True, linestyle='--', alpha=0.7)
plt.xlabel('t')
plt.ylabel('y')
plt.title("Euler's Method Approximation")
plt.legend()

# Add points to show the discrete steps
plt.scatter(df['t'], df['y'], color='blue', zorder=5, s=30)

plt.show()