\begin{tikzpicture}
   \begin{groupplot}[
       group style={
           group size= 3 by 2,
       },
       height=5cm,
       width=5cm,
   ]

   \nextgroupplot[title={Ratio: 50\%}, ylabel={Size: 100000}, cycle list name=color]
       \addplot table {data/comparison_throughput_100000_50_tree_FCDynamicGraphFlush.dat};\label{plots:fc}
       \addplot table {data/comparison_throughput_100000_50_tree_BlockingDynamicGraph.dat};\label{plots:blocking}
       \addplot table {data/comparison_throughput_100000_50_tree_BlockingRWDynamicGraph.dat};\label{plots:rwblocking}
       \coordinate (top) at (rel axis cs:0,1);% coordinate at top of the first plot

   \nextgroupplot[title={Ratio: 80\%}, cycle list name=color]
       \addplot table {data/comparison_throughput_100000_80_tree_FCDynamicGraphFlush.dat};
       \addplot table {data/comparison_throughput_100000_80_tree_BlockingDynamicGraph.dat};
       \addplot table {data/comparison_throughput_100000_80_tree_BlockingRWDynamicGraph.dat};

   \nextgroupplot[title={Ratio: 100\%}, cycle list name=color]
       \addplot table {data/comparison_throughput_100000_100_tree_FCDynamicGraphFlush.dat};
       \addplot table {data/comparison_throughput_100000_100_tree_BlockingDynamicGraph.dat};
       \addplot table {data/comparison_throughput_100000_100_tree_BlockingRWDynamicGraph.dat};

  \nextgroupplot[xlabel={Number of Threads}, ylabel={Size: 400000}, cycle list name=color]
       \addplot table {data/comparison_throughput_400000_50_tree_FCDynamicGraphFlush.dat};
       \addplot table {data/comparison_throughput_400000_50_tree_BlockingDynamicGraph.dat};
       \addplot table {data/comparison_throughput_400000_50_tree_BlockingRWDynamicGraph.dat};

  \nextgroupplot[xlabel={Number of Threads}, cycle list name=color]
       \addplot table {data/comparison_throughput_400000_80_tree_FCDynamicGraphFlush.dat};
       \addplot table {data/comparison_throughput_400000_80_tree_BlockingDynamicGraph.dat};
       \addplot table {data/comparison_throughput_400000_80_tree_BlockingRWDynamicGraph.dat};

  \nextgroupplot[xlabel={Number of Threads}, cycle list name=color]
       \addplot table {data/comparison_throughput_400000_100_tree_FCDynamicGraphFlush.dat};
       \addplot table {data/comparison_throughput_400000_100_tree_BlockingDynamicGraph.dat};
       \addplot table {data/comparison_throughput_400000_100_tree_BlockingRWDynamicGraph.dat};
       \coordinate (bot) at (rel axis cs:1,0);% coordinate at bottom of the last plot


  \end{groupplot}
  \path (top-|current bounding box.west)--
       node[anchor=south,rotate=90] {Throughput, mops/s}
       (bot-|current bounding box.west);

  %legend
  \path (top|-current bounding box.north)--
        coordinate(legendpos)
       (bot|-current bounding box.north);
  \matrix[
     matrix of nodes,
     anchor=south,
      draw,
      inner sep=0.2em,
  ] at ([yshift=1ex]legendpos) {
     \ref{plots:fc}& FC Parallel \\
     \ref{plots:blocking}& Blocking \\
     \ref{plots:rwblocking}& Read-Write Blocking \\
  };
\end{tikzpicture}



