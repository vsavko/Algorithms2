//import edu.princeton.cs.algs4.Merge;

public class CircularSuffixArray {
	private int[] index;
	private int length;
	
	/*private class tableNode implements Comparable<tableNode>{
		public int val;
		public char c;
				
		public int compareTo(tableNode o) {
			if (c > o.c)
				return 1;
			else if(c < o.c)
				return -1;
			else
				return 0;
		}
	}*/
	
	
	 private static void sort(CircularSuffix a){ 
		 sort(a, 0, a.length - 1, 0);
	 }
	 
	 private static void sort(CircularSuffix a, int lo, int hi, int d) {
		 if (hi <= lo) return;
		 int lt = lo, gt = hi;
		 int v = a.getChar(lo, d);
		 int i = lo + 1;
		 
		 while (i <= gt) {
			 int t = a.getChar(i, d);
			 if (t < v) a.exch(lt++, i++); //exchange row lt with i
			 else if (t > v) a.exch(i, gt--);
			 else i++; 
		 }
		 
		 sort(a, lo, lt-1, d);
		 if (v >= 0) sort(a, lt, gt, d+1);
		 sort(a, gt+1, hi, d);
	 }
	 
		
	private class CircularSuffix{
		private String text;
		private int length;
		private int [] positionArray;
		//private tableNode [] nodes;
		
		public CircularSuffix(String text) {
			super();
			this.text = text;
			this.length = text.length();
			//this.nodes = new tableNode[length];
			this.positionArray = new int[length];
			
			for(int i=0; i< length; i++) {
				positionArray[i] = i;
				//nodes[i] = new tableNode();
				//nodes[i].val = i;
			}

			/*for(int i=length-1; i>= 0; i--) {
				makeCol(i);
				Merge.sort(nodes);
			}
			
			for(int z = 0; z < length; z++) {
				positionArray[z] = nodes[z].val;
			}*/
			
			
		}
		
		public int getChar(int rowNr, int colNr) {
			if (rowNr < 0 || colNr < 0 || rowNr> length || colNr> length) return -1;
			return text.charAt((colNr + positionArray[rowNr]) % length);
		}
		
		public void exch(int row1, int row2) {
			int tmp;
			tmp = positionArray[row1];
			positionArray[row1] = positionArray[row2];
			positionArray[row2] = tmp;
		}
		
		/*private void makeCol(int col) {
			for(int i = 0; i < length; i++) {
				nodes[i].c = getChar(i, col);
			}
		}*/
		
		public int [] getPositionArray() {
			return positionArray;
		}
	}
	
    // circular suffix array of s
    public CircularSuffixArray(String s) {
    	if (s == null) throw new IllegalArgumentException("Null string input!");
    	CircularSuffix suff = new CircularSuffix(s);
    	sort(suff);
    	index = suff.getPositionArray();
    	length = s.length();
    }

    // length of s
    public int length() {
    	return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
    	if (i < 0 || i > length-1) throw new IllegalArgumentException("Index range outside of bounds!");
    	return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
    	CircularSuffixArray carr = new CircularSuffixArray("ABRACADABRA!");
    	System.out.println("Length test " + (carr.length() - 12 == 0 ));
    	int [] indexTestArr = {11,10,7,0,3,5,8,1,4,6,9,2};
    	boolean indexTest = true;
    	
    	
    	for(int i = 0; i < carr.length(); i++) {
    		//System.out.println(carr.index(i));
    		if (carr.index(i) - indexTestArr[i] != 0) {
    			indexTest = false;
    			//break;
    		}
    	}
    	System.out.println("Index test " + indexTest);

    	

    	
    	
    //	String s = BinaryStdIn.readString();
    	//CircularSuffixArray bricks = new CircularSuffixArray(s);
    	//System.out.println(bricks.length);
    	//BinaryStdOut.write(s);
    	
    	

	    /*String s = BinaryStdIn.readString();
	    BinaryStdOut.write(s);
	    
    	BinaryStdOut.flush();
    	BinaryStdOut.close();*/
    }

}