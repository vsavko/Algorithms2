import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycleX;
import edu.princeton.cs.algs4.In;

public class WordNet {
	
	private HashMap<Integer, String> synsetsHM = new HashMap<>();
	private HashMap<String, ArrayList<Integer>> synsetsHMString = new HashMap<>();
	private Digraph hypernymsHM;
	private SAP sap;
	
	   // constructor takes the name of the two input files
	   public WordNet(String synsets, String hypernyms) {//
		   if (synsets == null || hypernyms == null) throw new IllegalArgumentException ("Null file!"); // 
		   In in = new In(synsets);
		   while(in.hasNextLine()) {
			   String names = in.readLine();
			   String[] fields;
			   fields = names.split("\\,");
			   synsetsHM.put(Integer.parseInt(fields[0]), fields[1]);
			   String[] synonims = fields[1].split("\\ ");
			   int i = 0;
			   
			   while(synonims.length > i) {
				   if (!synsetsHMString.containsKey(synonims[i]))
					   synsetsHMString.put(synonims[i], new ArrayList<Integer>());
				   synsetsHMString.get(synonims[i++]).add(Integer.parseInt(fields[0]));
			   }			   
		   }

		   hypernymsHM = new Digraph(synsetsHM.size());

		   in = new In(hypernyms);
		   while(in.hasNextLine()) {
			   String names = in.readLine();
			   String[] fields;
			   fields = names.split("\\,");
			   int i = 1;
			   while(fields.length > i) {
				   hypernymsHM.addEdge(Integer.parseInt(fields[0]),Integer.parseInt(fields[i++]));
			   }
			   

		   }
		   //check DAG cycling
		   int count = 0;
		   for(int i = 0; i < hypernymsHM.V(); i++) {  
			   if (hypernymsHM.outdegree(i) == 0)
			   	count++;
			   if (count > 1) throw new IllegalArgumentException ("2 roots in digraph!"); 
		   }
		   
		   DirectedCycleX cycle = new DirectedCycleX(hypernymsHM);
		   if (cycle.hasCycle()) throw new IllegalArgumentException ("Has cycle!"); 
		   
		   
		   
		   sap = new SAP(hypernymsHM);
		   in = null;
	   }

	   	   
	   // returns all WordNet nouns
	   public Iterable<String> nouns(){  
		   return synsetsHMString.keySet();
	   }

	   // is the word a WordNet noun?
	   public boolean isNoun(String word) {
		   if (word == null) throw new IllegalArgumentException ("Noun is null!");
		   return synsetsHMString.containsKey(word);
	   }
	   
	   // distance between nounA and nounB (defined below)
	   public int distance(String nounA, String nounB) {
		   if (isNoun(nounA) == false || isNoun(nounB) == false || nounA == null || nounB == null)
			   throw new IllegalArgumentException ("Noun is not contained in WordNet!"); 
		   return sap.length(synsetsHMString.get(nounA),synsetsHMString.get(nounB));	   
	
	   }

	   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	   // in a shortest ancestral path (defined below)
	   public String sap(String nounA, String nounB) {
		   if (isNoun(nounA) == false || isNoun(nounB) == false || nounA == null || nounB == null) 
			   throw new IllegalArgumentException ("Noun is not contained in WordNet!");
		   return synsetsHM.get(sap.ancestor(synsetsHMString.get(nounA),synsetsHMString.get(nounB)));
	   }
	   

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordNet test = new WordNet("D:\\java\\projects\\WordNet2\\test\\synsets.txt",
				"D:\\java\\projects\\WordNet2\\test\\hypernyms.txt");
		
		System.out.println("Distance " + test.distance("word", "bird"));
		System.out.println(test.sap("word", "bird"));
		/*int i = 0;
		for(String key: test.nouns()) {
			if (i++ >10) break;
			System.out.println(key);
		}*/

		
	}

}
