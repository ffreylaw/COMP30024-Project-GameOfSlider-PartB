package ai.partB.Minimax;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TDLeafLambda {
	
	private static TDLeafLambda singleton = new TDLeafLambda();
	
	private ArrayList<Double> weights;
	private ArrayList<Double> evals_c1;
	private ArrayList<Double> evals_c2;
	private ArrayList<Double> evals_c3;
	
	private Scanner reader;
	
	private TDLeafLambda() {
		weights = new ArrayList<Double>();
		evals_c1 = new ArrayList<Double>();
		evals_c2 = new ArrayList<Double>();
		evals_c3 = new ArrayList<Double>();
		
		try {
			reader = new Scanner(new File("learning/weight.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (reader.hasNextDouble()) {
			weights.add(reader.nextDouble());
		}
	}
	
	public static TDLeafLambda getInstance() {
		return singleton;
	}
	
	public double getWeight(int index) {
		return weights.get(index);
	}
	
	public void addEvalC1(double eval_c1) {
		evals_c1.add(eval_c1);
	}
	
	public void addEvalC2(double eval_c2) {
		evals_c2.add(eval_c2);
	}
	
	public void addEvalC3(double eval_c3) {
		evals_c3.add(eval_c3);
	}
	
	public void finalize() {
		// get size
		int n = evals_c1.size();
		
		ArrayList<Double> evals = new ArrayList<Double>();
		ArrayList<Double> rewards = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			// calculate eval(s_i^l,w) and store in an array
			evals.add(evals_c1.get(i)*weights.get(0) + evals_c2.get(i)*weights.get(1) + evals_c3.get(i)*weights.get(2));
			// calculate r(s_i^l,w) and store in an array
			rewards.add(Math.tanh(evals.get(i)));
		}
		
		double alpha = 1.0;
		double lambda = 0.7;
		
		double td = 0.0;
		for (int m = 0; m < n-1; m++) {
			// td = sum lambda * temporal difference
			td += lambda * (rewards.get(m+1) - rewards.get(m));
		}
		
		double tmp_1 = 0.0;
		double tmp_2 = 0.0;
		double tmp_3 = 0.0;
		for (int i = 0; i < n-1; i++) {
			// sum dr(s_i^l,w)/dw_j * td
			double coe_1 = Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c1.get(i);
			double coe_2 = Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c2.get(i);
			double coe_3 = Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c3.get(i);
			tmp_1 += coe_1 * td;
			tmp_2 += coe_2 * td;
			tmp_3 += coe_3 * td;
		}
		
		// update weights
		double new_w1 = weights.get(0) + alpha*tmp_1;
		double new_w2 = weights.get(1) + alpha*tmp_2;
		double new_w3 = weights.get(2) + alpha*tmp_3;
		
		try {
			FileWriter writer = new FileWriter(new File("learning/weight.txt"));
			writer.write(new_w1 + " " + new_w2 + " " + new_w3);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
