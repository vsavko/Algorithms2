
public class Outcast {
	private WordNet wordnet;
	   public Outcast(WordNet wordnet) { 
		   // constructor takes a WordNet object
		  this.wordnet = wordnet;
	   }
	   public String outcast(String[] nouns) {
		   int [] maxArr = new int [nouns.length];
		   
		   for(int i = 0; i < nouns.length; i++) {
			   for(int j = i + 1; j < nouns.length; j++) {
				   int distance = wordnet.distance(nouns[i], nouns[j]);
				   maxArr[i] += distance;
				   maxArr[j] += distance;
			   }
		   }
		   
		   int maxLen = -1;
		   int outcastNoun = -1;
		   for(int i=0; i < maxArr.length; i++) {
			   if (maxArr[i] > maxLen) { 
				   maxLen = maxArr[i];
				   outcastNoun = i;
			   }
		   }

		   return nouns[outcastNoun];  
	   }
	   public static void main(String[] args) {
			WordNet test = new WordNet("D:\\java\\projects\\WordNet2\\test\\synsets.txt",
					"D:\\java\\projects\\WordNet2\\test\\hypernyms.txt");
			
			Outcast outcast1 = new Outcast(test);
			System.out.println(outcast1.outcast(new String [] {"cat","dog","troll"}));
			
	   }
}
