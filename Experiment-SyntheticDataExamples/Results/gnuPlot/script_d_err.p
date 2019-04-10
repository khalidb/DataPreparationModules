      # Gnuplot script file for plotting data in file "force.dat"
      # This file is called   force.p
      set terminal aqua dashed
      set   autoscale                        # scale axes automatically
      unset log                              # remove any log-scaling
      unset label                            # remove any previous labels
      set xtic auto                          # set xtics automatically
      set ytic auto                          # set ytics automatically
      # set title "Force Deflection Data for a Beam and a Column"
      set xlabel "Feedback Amount" 
      #set ylabel "Error"
      # set key 0.01,100
      set key outside bmargin bottom horizontal
      set pointsize 1 
      set style line 1 lt 2 lc rgb "black" lw 2.5
      #set label "Yield Point" at 0.003,260
      #set arrow from 0.0028,250 to 0.003,280
      set xr [1:150] #dataset 1
      set yr [0.0:0.5]
      plot    "d3_i_err.dat" using 1:2 title 'Error in F-score for dataset 3' with linespoints lt -1, \
            "d4_i_err.dat" using 1:3 title 'Error in F-score for dataset 4' with linespoints lt -1 lw 1.5


