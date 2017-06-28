package br.ufal.ic.operations;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

//import com.google.gson.Gson;

import br.ufal.ic.json.BugInfo;
import br.ufal.ic.objects.Metric;
import br.ufal.ic.utils.IO;
import br.ufal.ic.utils.Paths;

public class Collector {

	private String projectName;

	public Collector(String projectName) {
		this.projectName = projectName;
	}

	// private HashSet<String> map;

	public void collectMetricsInProjects(String projectName) {

		BugsOperations bugsOperations = new BugsOperations(); 
		
		List<BugInfo> bugs = bugsOperations.assignBugList(projectName);//collect method/inicial commit/reported commit/fixed
		// BugInfo's type of data, writed above
		// List<Integer> missingElements = getElementsNotCollected(projectName);

		int count = 0;
		int bugsSize =  bugs.size();
		for (int j = 0; j < bugsSize; j++){ //for each BugInfo

			BugInfo bug = bugs.get(j);
			if (!checkValited(bug)){ //if is "false" returns true
				count++;
				continue;
			}

			System.out.println(bug.getElement() + " has some missing elements.");

			HashMap<String, List<Metric>> output = new HashMap<String, List<Metric>>();

			//System.out.println(count + "/" + bugs.size());
			//System.out.println("Element: " + count);
			//System.out.println("Start " + bug.getOrder_init() + " End " + bug.getOrder_reported());
			//System.out.println("Real Size " + (bug.getOrder_reported() - bug.getOrder_init()));

			List<String> hashs = IO.readAnyFile(projectName + "/hashs_by_element/" + bug.getElement() + ".txt");
			System.out.println("Hashs Size: " + hashs.size());

			for (int i = 0; i < hashs.size(); i++) {

				String c = hashs.get(i);

				if (c == null) {
					System.out.println("No commit at " + i);
					continue;
				}

				ReaderUtils r = new ReaderUtils();

				if (c.equals("null")) { // ignore
					continue;
				}
				
				String path = Paths.PATH_DATA + "/" + projectName + "/metrics2/commit_" + c + "/"; 

				HashMap<String, Metric> metrics = r.getHashMetricsCSV(path); //create a Hash<element, metrics>

				String e = bug.getElement();
				if (metrics != null) {
					
					if(e.contains("?")){
						e = e.replaceAll("?", "");
					}
					if(e.contains("<")){
						e = e.replaceAll("<", "");
					}
					if(e.contains(">")){
						e = e.replaceAll(">", "");
					}
					
					if (metrics.containsKey(e)) {
						try {
							Metric m = metrics.get(e);
							m.setCommit(c);

							if (!output.containsKey(e)) {
								output.put(e, new LinkedList<Metric>());
							}
							output.get(e).add(m);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			
			}

			System.out.println(output.keySet().size());
			//System.out.println("Writing started.");
			for (String s : output.keySet()){
				if (output.get(s) != null) {
					
					WriterUtils w = new WriterUtils();
					w.writeMetrics("timeline/" + s, output.get(s));
				}
				System.out.println("Penis");
			}
			//System.out.println("Writing ended.");

			long end = System.currentTimeMillis();

			Date date = new Date(end);
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
			String dateFormatted = formatter.format(date);

			System.out.println("Time: " + dateFormatted);
			count++;
		}

	}

	public boolean checkValited(BugInfo bug) {

		List<String> bugFile = IO.readAnyFile(projectName + "/validation/" + bug.getElement() + ".txt");
		
		for (String line : bugFile) {
			if (line.contains("false") && !line.contains("null")) {
				return true;
			}
		}

		return false;

	}

	public void collectMetricsInProjects3(String projectName) {
		List<String> listHashs = IO.readAnyFile(projectName + "/hashs_" + projectName + ".txt");
		BugsOperations bugsOperations = new BugsOperations();
		List<BugInfo> bugs = bugsOperations.assignBugList(projectName);
		double max = bugsOperations.getMaxCommit();

		HashMap<String, List<Metric>> output = new HashMap<String, List<Metric>>();

		for (int i = 0; i < max; i++) {

			String c = listHashs.get(i);

			if (c == null) {
				System.out.println("No commit at " + i);
				continue;
			}

			System.out.println(i + "/" + max);

			ReaderUtils r = new ReaderUtils();

			String path = Paths.PATH_DATA + projectName + "/metrics/commit_" + c + "/";

			HashMap<String, Metric> metrics = r.getHashMetricsCSV(path);

			checkBugs(bugs, metrics, output, i, c);

			long end = System.currentTimeMillis();

		}

		int aux = 0;
		for (String s : output.keySet()) {
			if (output.get(s) != null) {
				System.out.println(aux + "/" + output.keySet().size());
				WriterUtils.writeMetrics("timeline/" + s, output.get(s));
				aux++;
			}
		}

	}

	private void checkBugs(List<BugInfo> bugs, HashMap<String, Metric> metrics, HashMap<String, List<Metric>> output,
			int i, String c) {

		if (metrics == null) {
			return;
		}

		for (int j = 1000; j < 1100; j++) {

			BugInfo b = bugs.get(j);

			if (i > b.getOrder_reported()) {
				continue;
			}

			if (i < b.getOrder_init()) {
				continue;
			}

			String e = b.getElement();
			if (metrics != null) {
				if (metrics.containsKey(e)) {
					try {
						Metric m = metrics.get(e);
						m.setCommit(c);

						if (!output.containsKey(e)) {
							output.put(e, new LinkedList<Metric>());
						}
						output.get(e).add(m);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}

	public void checkMissingMetrics() {
		
	}

	public List<Integer> getElementsNotCollected(String projectName) {

		BugsOperations bo = new BugsOperations();
		List<BugInfo> bugs = bo.assignBugList(projectName);

		List<String> files = Filter.filesOnFolder("timeline/");

		List<Integer> elementsMissing = new ArrayList<Integer>();

		for (int i = 0; i < bugs.size(); i++) {
			BugInfo b = bugs.get(i);
			boolean flag = true;
			for (String f : files) {
				if (b.getElement().contains(f)) {
					flag = false;
				}
			}
			if (flag) {
				// System.out.println(b);
				elementsMissing.add(i);
			}
		}

		return elementsMissing;

	}
	
	public void changeDelimeters(String path, char oldDelimeter, char newDelimeter){
		
		try{
			List<String> fileNames = Filter.filesOnFolder(path);
			int cont = 0;
			String output = "";
			
			for(String method : fileNames){
				cont++;
				System.out.println("Files:" + cont + "/" + fileNames.size());
				if(cont >= 13){
					String filePath = path + "/" + method + ".txt"; //file to acess
					
					File f = new File(filePath);
					List<String> metrics = IO.readAnyFile(filePath);
					int cont2 = 0;
					
					for(String line : metrics){ //running the txt
						cont2++;
						System.out.println("Line: " + cont2);
						
						char lineAux[] = line.toCharArray();
						Stack myStack = new Stack();
						int lineLength = lineAux.length;
						
						for(int i = 0 ; i < lineLength ; i++){// running the line							
							if(lineAux[i] == '('){
								myStack.push('(');
							}
							if(lineAux[i] == ')'){
								myStack.pop();
							}
								
							if(lineAux[i] == oldDelimeter){
								if(myStack.isEmpty()){
									lineAux[i] = newDelimeter;
								}
							}
							output += lineAux[i];
						}
						output += "\n";
					}
					//convert object to JSON here
					//String json = new Gson().toJson(metrics);
	
					Writer writer = new FileWriter(f);
					writer.write(output);
					writer.close();
			}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void methodNotValidated(String projectName){
		
		String elementsPath = projectName + "/elementsToGetMetrics_apache_tomcat.txt";
		
		try {
			
			File f = new File(projectName + "/elementsNotValidated.txt");
			List<String> notValid = new ArrayList<String>();
			List<String> elements = IO.readAnyFile(elementsPath);
			
			int elementsSize = elements.size();
			int elementCont = 0;
			
			for(String method : elements){
				elementCont++;
				System.out.println("Elements to check:" + elementCont + "/" + elementsSize);
				
				String[]  elementSplitted = method.split("%");
				String validationPath = projectName + "/validation/" + elementSplitted[0] + ".txt";
				List<String> validation = IO.readAnyFile(validationPath);
				//looking for commits in method validation
				for(String commits : validation){
				
					String[] commit = commits.split(",");// hash, boolean
					if(commit[1].contains("false")){

						notValid.add(elementSplitted[0]);
						System.out.println("---"+elementSplitted[0]);
						break;
					}
				}
				
			}
			Writer writer = new FileWriter(f);

			String out = "";
			for (String m : notValid) {
				out += m + "\n";
			}
			writer.write(out);
			writer.close();
			System.out.println("END!!!");

			// OLHAR VALIDATION NO METODO ONDE TEM FALSE, CHECAR COMMIT, SE NAO TIVER MESMO. IGNORAR NO METODO checkMinning no class MineData
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
