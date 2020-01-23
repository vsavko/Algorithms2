import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class SAP {
	private final Digraph graph;
	private int[][] cashe;
	private int casheLength, maxCashe = 10;
	private HashMap<resultsObtained, distanceAnsestor> resultsHashTable = new HashMap<>();
	private int [][] bfs;
	
	private class resultsObtained{
		public int v1;
		public int v2;
		
		public resultsObtained(int v1, int v2) {
			if (v1 > v2) {
				this.v1 = v1;
				this.v2 = v2;
			}
			else {
				this.v2 = v1;
				this.v1 = v2;
			}
		}
	}

	   // constructor takes a digraph (not necessarily a DAG)
	   public SAP(Digraph G) {
		   graph = new Digraph(G);
		   cashe = new int[maxCashe][4];
		   casheLength = 0;
		   bfs = new int [G.V()][2];  
	   }
	
	   // length of shortest ancestral path between v and w; -1 if no such path
	   public int length(int v, int w) {
		   if (v < 0 || w < 0 || v >= graph.V() || w >= graph.V() || (Integer)v == null || (Integer)w == null)
			   throw new IllegalArgumentException ("Wrong length input");
		   if (resultsHashTable.containsKey(new resultsObtained(v,w)))
			   return resultsHashTable.get(new resultsObtained(v,w)).distance;
		   else {  
			   distanceAnsestor foundDA = searchDistance(v, w, graph);
			   if (foundDA.distance == Integer.MAX_VALUE) foundDA.distance = -1;
			   resultsHashTable.put(new resultsObtained(v,w), foundDA);
			   return foundDA.distance;	
		   }
	   }
	   
	   private class distanceAnsestor{
		   public int distance;
		   public int ansestor;
		   
		   public distanceAnsestor(int distance, int ansestor) {
			this.distance = distance;
			this.ansestor = ansestor;
		}
	   }
	   
	   private distanceAnsestor searchDistance(int v, int w, Digraph hypernums) {
		   for(int i = 0; i < casheLength; i++) {
			   if (cashe[i][0] == v && cashe[i][1] == w ||
					   cashe[i][0] == w && cashe[i][1] == v) {
				   return new distanceAnsestor(cashe[i][2], cashe[i][2]);
			   }
		   }

		   ArrayList<Integer> v1 = new ArrayList<>();
		   ArrayList<Integer> w1= new ArrayList<>();
		   v1.add(v);
		   w1.add(w);
		   
		   distanceAnsestor output = searchDistance(v1,w1,hypernums);
		   
		   if (casheLength == 10) casheLength = 0;
		   
		   cashe[casheLength % maxCashe][0] = v; 
		   cashe[casheLength % maxCashe][1] = w; 
		   cashe[casheLength % maxCashe][2] = output.distance; 
		   cashe[casheLength % maxCashe][3] = output.ansestor; 
		   
		   return output;
		}	
	   
	   private distanceAnsestor searchDistance(Iterable<Integer> v, Iterable<Integer>  w, Digraph hypernums) {
		   Stack<Integer> usedSpaces = new Stack<>();
		   Queue<Integer> qA = new Queue<>(); 
		   Queue<Integer> qB = new Queue<>(); 
		   distanceAnsestor dATemp = new distanceAnsestor(Integer.MAX_VALUE,-1);
		   //put set of nounA and nounB, each word can have different meanings, hence id numbers in the diGraph
		   
		   
		   for(Integer key: v) {
			   if (key == null || key == null || key < 0 || key < 0 || key >= graph.V() || key >= graph.V() )
				   throw new IllegalArgumentException ("Wrong input");
			   qA.enqueue(key);
			   bfs[key][0] = 1;
			   usedSpaces.push(key);
		   }
		   
		   for(Integer key: w) {
			   if (key == null || key == null || key < 0 || key < 0 || key >= graph.V() || key >= graph.V() )
				   throw new IllegalArgumentException ("Wrong input");
			   qB.enqueue(key);
			   bfs[key][1] = 1;
			   usedSpaces.push(key);
			   if(bfs[key][0] != 0) {
				   for(int key2: usedSpaces) {
					   bfs[key2][0] = 0;
					   bfs[key2][1] = 0;
				   }
				   return new distanceAnsestor(0,key); //same word
			   }
		   }

		   while(!qA.isEmpty() || !qB.isEmpty()) {
			   int A,B, distance;
			   boolean stop1 = false, stop2= false;
			   if (stop1 && stop2) break;
			   
			   if(!qA.isEmpty() && !stop1) {
				   A = qA.dequeue();					   
				   for(int key: hypernums.adj(A)) {
					   if (bfs[key][0] == 0) {
						   bfs[key][0] = bfs[A][0] + 1;
						   usedSpaces.push(key);
						   if(bfs[key][1] != 0) {
							   distance = bfs[key][0] + bfs[key][1] - 2;
							   if(dATemp.distance > distance) {
								   dATemp.distance = distance;
								   dATemp.ansestor = key;
							   }
						   }
						   qA.enqueue(key);
						   if (!qA.isEmpty() && bfs[qA.peek()][0] -1 <= dATemp.distance)
							   stop1 = true;
					   }
				   }
			   }
			   
			   if(!qB.isEmpty() && !stop2) { 

				   B = qB.dequeue();
				   for(int key: hypernums.adj(B)) {
					   if (bfs[key][1] == 0) {
						   bfs[key][1] = bfs[B][1] + 1;
						   usedSpaces.push(key);
						   if(bfs[key][0] != 0) {	
							   distance = bfs[key][0] + bfs[key][1] - 2;
							   if(dATemp.distance > distance) {
								   dATemp.distance = distance;
								   dATemp.ansestor = key;
							   }
						   }
						   qB.enqueue(key);  
						   if (!qB.isEmpty() && bfs[qB.peek()][0] -1 <= dATemp.distance)
							   stop2 = true;
					   }
				   }
			   }
		   }
		   for(int key2: usedSpaces) {
			   bfs[key2][0] = 0;
			   bfs[key2][1] = 0;
		   }
		return dATemp;  
	   }
	
	   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	   public int ancestor(int v, int w) {
		   if (v < 0 || w < 0 || v >= graph.V() || w >= graph.V() || (Integer)v == null || (Integer)w == null) 
			   throw new IllegalArgumentException ("Wrong ancestor input");
		   
		   if (resultsHashTable.containsKey(new resultsObtained(v,w)))
			   return resultsHashTable.get(new resultsObtained(v,w)).ansestor;
		   else {  
			   distanceAnsestor foundDA = searchDistance(v, w, graph);
			   if (foundDA.distance == Integer.MAX_VALUE) foundDA.distance = -1;
			   resultsHashTable.put(new resultsObtained(v,w), foundDA);
			   return foundDA.ansestor;
		   }
	   }
	
	   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	   public int length(Iterable<Integer> v, Iterable<Integer> w) {
		   if (v == null || w == null) throw new IllegalArgumentException ("Length iterable is null!");
		   int distance = searchDistance(v,w,graph).distance;
		   if (distance == Integer.MAX_VALUE) return -1;
		   return distance;
	   }
	   
	   // a common ancestor that participates in shortest ancestral path; -1 if no such path
	   public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
		   if (v == null|| w == null) throw new IllegalArgumentException ("Ancestor iterable is null!");
		   return searchDistance(v,w,graph).ansestor;
	   }
	
	   // do unit testing of this class
	   public static void main(String[] args) {
		    In in = new In("D:\\java\\projects\\WordNet2\\test\\digraph3.txt");
		    Digraph G = new Digraph(in);
		    SAP sap = new SAP(G);
		    //System.out.println("Length " + sap.length(8, 13) +  " Ancestor " + sap.ancestor(8,13)); //correct: 3 1
		    System.out.println("Length " + sap.length(9, 13) +  " Ancestor " + sap.ancestor(9,13)); //correct: 3 1
		    System.out.println("Length " + sap.length(7, 13) +  " Ancestor " + sap.ancestor(7,13)); //correct: 3 1
		    
		    ArrayList<Integer> v1 = new ArrayList<>();
		    ArrayList<Integer> v2 = new ArrayList<>();
		    
		    v1.add(0);
		    v1.add(1);
		    v1.add(2);
		    v2.add(3);
		    v2.add(1);
		    v2.add(2);


		   System.out.println(sap.length(v1, v2));
		    //System.out.println("Length " + sap.length(7, 2) +  " Ancestor " + sap.ancestor(7, 2)); //correct: 4 0
		    //System.out.println("Length " + sap.length(1, 6) +  " Ancestor " + sap.ancestor(1, 6)); //correct: -1 -1


	   }

}
