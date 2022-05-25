package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PlagiarismDetector implements IPlagiarismDetector {
	public int n;
	public Map<String, Set<String>> pan = new HashMap<String, Set<String>>();
	public Map<String, Map<String , Integer>> map = new HashMap<>();
	
	public PlagiarismDetector(int n) {
		// TODO implement this method
		this.n = n;
	}
	
	@Override
	public int getN() {
		// TODO Auto-generated method stub
		
		return n;
	}

	@Override
	public Collection<String> getFilenames() {
		// TODO Auto-generated method stub
		return pan.keySet();
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return pan.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return pan.get(filename).size();
		
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		// TODO Auto-generated method stub
		
		return map;
	}

	@SuppressWarnings("resource")
	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		String wor = "";
		int t = n;
		Set<String> set = new LinkedHashSet<>();
		Scanner scan = new Scanner(file);
		while(scan.hasNextLine()) {
			String x = scan.nextLine();
			String[] word = x.split(" ");
			for(int j=0; j <= word.length-n; j++) {
				wor = "";
				for (int i=j; i<t; i++) {
					wor = wor +" "+word[i];
				}
				wor = wor.trim();
				set.add(wor);
				t++;
			}
		}
		
		pan.put(file.getName(), set);
		System.out.println(pan);
		Map<String, Integer> m = new HashMap<>();
		Set<String> k = pan.keySet();
		k.remove(file.getName());
		for(String x : k) {
			m.put(x, getNumNGramsInCommon(file.getName(), x));
		}
		map.put(file.getName(), m);
	}
	
	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		Set<String> gram1 = pan.get(file1);
		Set<String> gram2 = pan.get(file2);
		int counter = 0;
		
		for(String x : gram1) {
			for(String y : gram2) {
				if(x.equals(y)) {
					counter++;
				}
			}
		}
		
		return counter;
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		int i = 0;
		Set<String> res = new HashSet<>();
		Set<String> k = pan.keySet();
		for(String x : k) {
			for(String y : k) {
				i = getNumNGramsInCommon(x, y);
				if(i >= minNgrams) {
					if(x.compareTo(y) < 0) {
						String str = String.format("%s %d", x, y, i);
						res.add(str);
					}
				}
			}
		}
		
		return res;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
	}
}
