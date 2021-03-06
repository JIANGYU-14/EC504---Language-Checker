package SpellSearch;

import java.util.*;

import GrammarCheck.Bigram;
import SpellSearch.Dictionary;

public class checker {
	
	public String original = "";
	public Bigram n;
	public Dictionary root;
	public StringBuilder sb;
	public boolean WrongSpell = false;
	public boolean GrammarCheck = false;
	public ArrayList<String> IndexString;
	public HashMap<Integer,String> Correction;
	public checker(StringBuilder sb, Dictionary root, Bigram n)
    {
	 
		this.n = n;
		this.root = root;
		this.sb = sb;
		this.Correction = new HashMap<>();
		this.IndexString = new ArrayList<>();
        
    }
	
	public void SpellCheck(){
		String[] test = sb.toString().split("[\\s;,]+");
		original = sb.toString();
        Set<String> suggested = new HashSet<String>();
        Set<String> correction = new HashSet<String>();
        
        String pre = "<S>";
        int index = 0;
        LevDistance lev = new LevDistance();
        if(test.length == 1){
        	
        	String finalCor = "";
        	String str = test[0];
        	IndexString.add(str);
        	if(root.search(str.toLowerCase()) == false){
        		WrongSpell = true;
        		suggested = lev.getEdits(str);
        		correction = lev.findCorrection(suggested, str, 2);
        		double max = Integer.MIN_VALUE;
        		String inner = "";
        		for(String cor : correction){
        			if(root.search(cor)){
	        			if(n.wordCount.getOrDefault(cor, 0.0) > max){
	        				max = n.wordCount.getOrDefault(cor,0.0);
	        				inner = cor;
	        			}
        			}
        			
        			//System.out.println("For " + str +", you may mean : " + cor);
        		}
        		Correction.put(0, inner);
        	}
        	//else System.out.println(str + "  " + root.tagger);
        	
        }
        else {
        	GrammarCheck = true;
        	for(String str : test){
        		IndexString.add(str);
        		String finalCor = "";
	        	if(root.search(str.toLowerCase()) == false){
	        		WrongSpell = true;
	        		//System.out.println("The word" + " " +  str +" is outlier");
	        		suggested = lev.getEdits(str);
	        		correction = lev.findCorrection(suggested, str, 2);
	        		//System.out.println("For " + str +", you may mean ");
	        		double max = Integer.MIN_VALUE;
	        		for(String cor : correction){
	        			if(root.search(cor)){
	        				double val =  n.count(pre,cor);
	        				//System.out.println(pre + "   " + cor +  "   " +  val);
	        				if(val > max){
	        					max = val;
	        					finalCor = cor;
	        				}
	        			}
	        		}
	        		//System.out.println(finalCor);
	        		original = original.replaceAll(str, finalCor);
	        		Correction.put(index, finalCor);
	        		pre = finalCor;
	        		
	        		
	        	}
	        	else{
		        	pre = str;
	        	}
	        	index++;
        	}
        }
        //System.out.println(original);
	}
	
	
	
}
