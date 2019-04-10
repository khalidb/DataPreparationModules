      # Gnuplot script file for plotting data in file "force.dat"
      # This file is called   force.p
      set terminal aqua dashed font "Times-Roman,30"
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
      set xr [0:150]
      set yr [0.0:0.5]
      plot    "p_e.dat" every 5 using 1:2  title 'Dataset 1' with linespoints lt -1, \
            "p_e.dat" every 5 using 1:3 title 'Dataset 2' with linespoints lt -1 lw 1.5, \
	    "p_e.dat" every 5 using 1:4  title 'Dataset 3' with linespoints lt -1 lw 1.5, \
	    "p_e.dat" every 5 using 1:5 title 'Dataset 4' with linespoints lt -1 pointinterval 10


