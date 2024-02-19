# ILP (Informatics Large Practical) 2019

This is my solution to the simulation of the game of 'Powergrab'. The project was the focus of the first semester 3rd Year course Informatics Large Practical in the University of Edinburgh (2019/2020).

The file ILP specification is the specification document for the system.

The folder powergrab is my solution (written in Java and built in Maven), while the file ILP report.pdf is my written report on the system I made.

Check out some sample simultations (in sample-results) for both the stateless and stateful drones on 24 different maps. The .geojson files can be interactively viewed with the ILP visualiser tool https://homepages.inf.ed.ac.uk/stg/ilp/ created by Bora M. Alper.
<br />

## Sample output from the stateful drone
The stateful (look ahead) drone uses:
> Pathfinding = A*
> Tour Construction = Nearest Neighbour, Nearest Insertion, Farthest Insertion
> Tour Improvement = 2-Opt and 3-Opt

![alt text](https://github.com/daraghmeehan/ILP/blob/master/sample-images/stateful01-01-2019.jpg?raw=true)
