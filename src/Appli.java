import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class Appli {
	private static Map<String, ArrayList<String>> graph;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int k = 0;
		String S = "";
		
		//lecture du fichier texte	
		try{
			InputStream ips=new FileInputStream(args[0]); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			
			// lecture de k
			k = Integer.parseInt(br.readLine());
			System.out.println("k = " + k);
			// on effectue l'algo sur toute les chaines
			while ((S=br.readLine()) != null){
				System.out.println(S);
				build_graph(k, S);
			}
			br.close();
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

	public static void build_graph(int k, String S) {
		graph = new TreeMap<>();
		for (int i = 0; i < S.length()-(k-1); i++) {
			graph.put(S.substring(i, i+k), new ArrayList<String>());
		}
		
		for (String x : graph.keySet()) {
			for (String y : graph.keySet()) {
				if (!x.equals(y)) {
					boolean arc = false;
					if (x.substring(1, x.length()).equals(y.substring(0, y.length()-1))) {
						String tmp = x+y.charAt(y.length()-1);
						for (int i = 0; i < S.length()-k; i++) {
							if (S.substring(i, i+k+1).equals(tmp)) {
								arc = true;
								break;
							}
						}
					}
					if (arc)
						graph.get(x).add(y);
				}
			}
		}
		
		save_graph(S);
	}
	
	public static void save_graph(String S) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter (new FileWriter(S)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int i = 0, j = 0;
	    
		pw.println("digraph Bruijn {");
		for (String x : graph.keySet()) {
			pw.println(x+"[label="+x+"]");
			for (String y : graph.get(x)) {
				pw.println(x+"->"+y);
				pw.println(y+"[label="+y+"]");
			}
		}
		pw.println("}");
	    pw.close();
	    
	    try {
			Process p = Runtime.getRuntime().exec("dot -O -Tsvg "+S);
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
