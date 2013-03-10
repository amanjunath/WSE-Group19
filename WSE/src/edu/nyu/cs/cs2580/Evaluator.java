package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;

class Evaluator {

	static double prec[] = new double[3];
	static double rec[] = new double[3];

	public static void main(String[] args) throws IOException {
		HashMap<String, HashMap<Integer, Double>> relevance_judgments = new HashMap<String, HashMap<Integer, Double>>();
		HashMap<String, HashMap<Integer, Double>> graded_judgments = new HashMap<String, HashMap<Integer, Double>>();
		if (args.length < 1) {
			System.out.println("need to provide relevance_judgments");
			return;
		}
		String p = args[0];
		// first read the relevance judgments into the HashMap
		readRelevanceJudgments(p, relevance_judgments, graded_judgments);
		// now evaluate the results from stdin
		evaluateStdin(relevance_judgments, graded_judgments);
	}

	public static void readRelevanceJudgments(String p,
			HashMap<String, HashMap<Integer, Double>> relevance_judgments,
			HashMap<String, HashMap<Integer, Double>> graded_judgements) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(p));
			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					// parse the query,did,relevance line
					Scanner scanner = new Scanner(line);
					Scanner s = scanner.useDelimiter("\t");
					String query = s.next();
					int did = Integer.parseInt(s.next());
					String grade = s.next();
					double rel = 0.0;
					// convert to binary relevance
					if ((grade.equals("Perfect"))
							|| (grade.equals("Excellent"))
							|| (grade.equals("Good"))) {
						rel = 1.0;
					}
					double grel = 0.0;
					if (grade.equals("Perfect"))
						grel = 10.0;
					else if (grade.equals("Excellent"))
						grel = 7.0;
					else if (grade.equals("Good"))
						grel = 5.0;
					else if (grade.equals("Fair"))
						grel = 1.0;
					if (relevance_judgments.containsKey(query) == false) {
						HashMap<Integer, Double> qr = new HashMap<Integer, Double>();
						relevance_judgments.put(query, qr);
					}
					HashMap<Integer, Double> qr = relevance_judgments
							.get(query);
					qr.put(did, rel);
					if (graded_judgements.containsKey(query) == false) {
						HashMap<Integer, Double> gr = new HashMap<Integer, Double>();
						graded_judgements.put(query, gr);
					}
					HashMap<Integer, Double> gr = graded_judgements.get(query);
					gr.put(did, grel);
					scanner.close();
				}
			} finally {
				reader.close();
			}
		} catch (IOException ioe) {
			System.err.println("Oops " + ioe.getMessage());
		}
	}

	public static void evaluateStdin(
			HashMap<String, HashMap<Integer, Double>> relevance_judgments,
			HashMap<String, HashMap<Integer, Double>> graded_judgments) {
		// only consider one query per call
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			PrintWriter writer = new PrintWriter(new BufferedWriter(
					new FileWriter(".tmp")));
			String line;
			while ((line = reader.readLine()) != null) {
				writer.println(line);
			}
			writer.close();
			reader.close();
			BufferedReader filereader = new BufferedReader(new FileReader(
					".tmp"));
			int r = 0;
			if ((line = filereader.readLine()) != null) {
				Scanner scanner = new Scanner(line);
				Scanner s = scanner.useDelimiter("\t");
				String query = s.next();
				if (relevance_judgments.containsKey(query) == false) {
					scanner.close();
					writer.close();
					throw new IOException("query not found");
				}
				HashMap<Integer, Double> qr = relevance_judgments.get(query);
				Iterator<Entry<Integer, Double>> it = qr.entrySet().iterator();
				while (it.hasNext()) {
					Entry<Integer, Double> en = it.next();
					if (en.getValue() == 1)
						r++;
				}
				scanner.close();
			}
			String writeLine = computePrecision(relevance_judgments);
			writeLine = computeRecall(writeLine, r, relevance_judgments);
			writeLine = computeF_Measure(writeLine, relevance_judgments);
			writeLine = computePrecision_Recall(writeLine, r,
					relevance_judgments);
			writeLine = computeAveragePrecision(writeLine, relevance_judgments);
			writeLine = computeNDCG(writeLine, graded_judgments);
			computeReciprocalRank(writeLine, relevance_judgments);
			filereader.close();
		} catch (Exception e) {
			System.err.println("Error:" + e.getMessage());
		}
	}

	private static void computeReciprocalRank(String writeLine,
			HashMap<String, HashMap<Integer, Double>> relevance_judgments)
					throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(".tmp"));
		String line;
		int i = 1;
		while ((line = reader.readLine()) != null) {
			Scanner scanner = new Scanner(line);
			Scanner s = scanner.useDelimiter("\t");
			String query = s.next();
			int did = Integer.parseInt(s.next());
			HashMap<Integer, Double> qr = relevance_judgments.get(query);
			if (qr.containsKey(did) != false)
				if (qr.get(did) == 1) {
					writeLine += "\t" + (double)1 / i;
					break;
				}
			i++;
			scanner.close();
		}
		System.out.println(writeLine);
		reader.close();
	}

	private static String computeNDCG(String writeLine,
			HashMap<String, HashMap<Integer, Double>> graded_judgments)
					throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(".tmp"));
		HashMap<Integer, Double> gr = new HashMap<Integer, Double>();
		String line;
		int i = 1;
		double DCG = 0, DCG1 = 0, DCG5 = 0, DCG10 = 0, IDCG = 0, IDCG1 = 0, IDCG5 = 0, IDCG10 = 0;
		while ((line = reader.readLine()) != null) {
			Scanner scanner = new Scanner(line);
			Scanner s = scanner.useDelimiter("\t");
			String query = s.next();
			int did = Integer.parseInt(s.next());
			gr = graded_judgments.get(query);
			if (gr.containsKey(did) != false)
				DCG += (gr.get(did) * Math.log(2)) / Math.log(i + 1);
			if (i == 1)
				DCG1 = DCG;
			else if (i == 5)
				DCG5 = DCG;
			else if (i == 10)
				DCG10 = DCG;
			scanner.close();
			i++;
		}
		Iterator<Entry<Integer, Double>> it = gr.entrySet().iterator();
		ArrayList<Double> sortedQRels = new ArrayList<Double>();
		while (it.hasNext()) {
			Entry<Integer, Double> en = it.next();
			sortedQRels.add(en.getValue());
		}
		Collections.sort(sortedQRels, Collections.reverseOrder());
		Double d[] = sortedQRels.toArray(new Double[0]);
		for (i = 1; i <= d.length; i++) {
			IDCG += (d[i - 1] * Math.log(2)) / Math.log(i + 1);
			if (i == 1)
				IDCG1 = IDCG;
			else if (i == 5)
				IDCG5 = IDCG;
			else if (i == 10)
				IDCG10 = IDCG;
		}
		double NDCG1, NDCG5, NDCG10;
		NDCG1 = (IDCG1 == 0) ? DCG1 : DCG1/IDCG1;
		NDCG5 = (IDCG5 == 0) ? DCG5 : DCG5/IDCG5;
		NDCG10 = (IDCG10 == 0) ? DCG1 : DCG10/IDCG10;
		writeLine += "\t" + NDCG1 + "\t" + NDCG5 + "\t" + NDCG10;
		reader.close();
		return writeLine;
	}

	private static String computeAveragePrecision(String writeLine,
			HashMap<String, HashMap<Integer, Double>> relevance_judgments)
					throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(".tmp"));
		String line;
		double rr = 0, ap = 0, i = 1;
		while ((line = reader.readLine()) != null) {
			Scanner scanner = new Scanner(line);
			Scanner s = scanner.useDelimiter("\t");
			String query = s.next();
			int did = Integer.parseInt(s.next());
			HashMap<Integer, Double> qr = relevance_judgments.get(query);
			if (qr.containsKey(did) != false)
				if (qr.get(did) == 1) {
					rr += 1;
					ap += rr / i;
				}
			i += 1;
			scanner.close();
		}
		if (rr == 0)
			writeLine += "\t" + ap;
		else
			writeLine += "\t" + ap/rr;
		reader.close();
		return writeLine;
	}

	private static String computePrecision_Recall(String writeLine, int r,
			HashMap<String, HashMap<Integer, Double>> relevance_judgments)
					throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(".tmp"));
		String line;
		double i = 1, rr = 0;
		ArrayList<Double> recall = new ArrayList<Double>();
		ArrayList<Double> precision = new ArrayList<Double>();
		while ((line = reader.readLine()) != null) {
			Scanner scanner = new Scanner(line);
			Scanner s = scanner.useDelimiter("\t");
			String query = s.next();
			int did = Integer.parseInt(s.next());
			HashMap<Integer, Double> qr = relevance_judgments.get(query);
			if (qr.containsKey(did) != false)
				if (qr.get(did) == 1)
					rr++;
			recall.add(rr / r);
			precision.add(rr / i);
			i++;
			scanner.close();
			if (rr / r == 1.0)
				break;
		}
		Double recall_arr[] = recall.toArray(new Double[0]), precision_arr[] = precision
				.toArray(new Double[0]), precAtRec[] = new Double[11];
		for (double j = 0; j <= 1; j = j + 0.1) {
			int k;
			for (k = 0; recall_arr[k] < j && k < recall_arr.length; k++)
				;
			double max = precision_arr[k];
			for (int l = k + 1; l < precision_arr.length; l++) {
				if (precision_arr[l] > max) {
					max = precision_arr[l];
				}
			}
			precAtRec[(int) Math.round(j * 10)] = max;
		}
		for (int j = 0; j < precAtRec.length; j++)
			writeLine += "\t" + precAtRec[j];
		reader.close();
		return writeLine;
	}

	private static String computeF_Measure(String writeLine,
			HashMap<String, HashMap<Integer, Double>> relevance_judgments)
					throws IOException {
		double alpha = 0.5;
		double f1 = 0, f5 = 0, f10 = 0;
		if (rec[0] != 0 || prec[0] != 0)
			f1 = (rec[0] * prec[0])
			/ ((alpha * rec[0]) + ((1 - alpha) * prec[0]));
		if (rec[1] != 0 || prec[1] != 0)
			f5 = (rec[1] * prec[1])
			/ ((alpha * rec[1]) + ((1 - alpha) * prec[1]));
		if (rec[2] != 0 || prec[2] != 0)
			f10 = (rec[2] * prec[2])
			/ ((alpha * rec[2]) + ((1 - alpha) * prec[2]));
		writeLine += "\t" + f1 + "\t" + f5 + "\t" + f10;
		return writeLine;
	}

	private static String computeRecall(String writeLine, int r,
			HashMap<String, HashMap<Integer, Double>> relevance_judgments)
					throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(".tmp"));
		String line;
		double i = 1, rr = 0;
		int j = 0;
		while ((line = reader.readLine()) != null && i < 11) {
			Scanner scanner = new Scanner(line);
			Scanner s = scanner.useDelimiter("\t");
			String query = s.next();
			int did = Integer.parseInt(s.next());
			HashMap<Integer, Double> qr = relevance_judgments.get(query);
			if (qr.containsKey(did) != false)
				if (qr.get(did) == 1)
					rr++;
			if (i == 1 || i == 5 || i == 10) {
				if (r == 0) {
					writeLine += "\t" + rr;
					rec[j++] = rr;
				}
				else {
					writeLine += "\t" + rr/r;
					rec[j++] = rr/r;
				}
					
			}
			i++;
			scanner.close();
		}
		reader.close();
		return writeLine;
	}

	private static String computePrecision(
			HashMap<String, HashMap<Integer, Double>> relevance_judgments)
					throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(".tmp"));
		String line, writeLine = null;
		double i = 1, rr = 0;
		int j = 0;
		while ((line = reader.readLine()) != null && i < 11) {
			Scanner scanner = new Scanner(line);
			Scanner s = scanner.useDelimiter("\t");
			String query = s.next();
			int did = Integer.parseInt(s.next());
			HashMap<Integer, Double> qr = relevance_judgments.get(query);
			if (qr.containsKey(did) != false)
				if (qr.get(did) == 1)
					rr++;
			if (i == 1 || i == 5 || i == 10) {
				if (i == 1)
					writeLine = query + "\t" + rr / i;
				else
					writeLine += "\t" + rr / i;
				prec[j++] = rr / i;
			}
			scanner.close();
			i++;
		}
		reader.close();
		return writeLine;
	}
}