import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
    	String s = "";
	    s = BinaryStdIn.readString();
    	BinaryStdIn.close();

    	CircularSuffixArray CSA = new CircularSuffixArray(s);
    	int originalSuffixPos = -1;
    	char [] temp = new char[CSA.length()];
    	
    	for(int i =0; i< CSA.length(); i++) {
    		if(CSA.index(i) == 0) {
    			temp[i] = s.charAt(CSA.length()-1);
    			originalSuffixPos = i;
    		}
    		else
    			temp[i] = s.charAt(CSA.index(i)-1);
    	}
    	
    	BinaryStdOut.write(originalSuffixPos);
    	
    	for(int i = 0; i < CSA.length(); i++)
    		BinaryStdOut.write(temp[i]);
    	
    	BinaryStdOut.flush();
    	BinaryStdOut.close();
    }
    
    private static int [] keyIndexSort(char [] a) {
	    int N = a.length;
	    int R = 256;
	    int [] count = new int[R+1];
	    int [] aux = new int[N];
	    char [] temp = new char[N];
	    
	    for (int i = 0; i < N; i++) {
	    	count[a[i]+1]++;
	    }
	    
	    for (int r = 0; r < R; r++)
	    	count[r+1] += count[r];

	    for (int i = 0; i < N; i++) {
	    	aux[i] = count[a[i]];
	    	temp[count[a[i]]] = a[i];
	    	count[a[i]]++;
	    }
	    
	    for(int i =0; i < N; i++) {
	    	a[i] = temp[i];
	    }

	    return aux;
    }


    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
    	int first = BinaryStdIn.readInt();
    	
    	ArrayList<Character> c = new ArrayList<>();
    	
    	while(!BinaryStdIn.isEmpty())
    		c.add(BinaryStdIn.readChar());
    	
    	BinaryStdIn.close();
    	int len = c.size();
    	char [] charArr = new char[len];
    	int z = 0;

    	for(char key: c) {
    		charArr[z++] = key;
    	}

    	int [] next = keyIndexSort(charArr); 

    	char[] d = new char[len];

    	for(int i = len-1; i >= 0; i--) {
    		d[i] = charArr[next[first]];
    		first = next[first];
    	}
    	
    	for(int i=0; i< len; i++){
    		BinaryStdOut.write(d[i]);
    	}
    	BinaryStdOut.flush();
    	BinaryStdOut.close();	
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
    	if (args[0].equals("-")) 
    		transform();
    	else if(args[0].equals("+"))
    		inverseTransform();
    }
}