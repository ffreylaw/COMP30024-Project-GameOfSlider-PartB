package ai.partB.Minimax;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TD Leaf (Lambda) algorithm
 */
public class TDLeafLambda {
	
	private ArrayList<Double> weights;
	private ArrayList<Double> evals_c1;
	private ArrayList<Double> evals_c2;
	private ArrayList<Double> evals_c3;
	private ArrayList<Double> evals_c4;
	
	private Scanner reader;
	
	private static final String FILENAME = "learning/weight.txt";
	
	public TDLeafLambda() {
		weights = new ArrayList<Double>();
		evals_c1 = new ArrayList<Double>();
		evals_c2 = new ArrayList<Double>();
		evals_c3 = new ArrayList<Double>();
		evals_c4 = new ArrayList<Double>();
		
		try {
			reader = new Scanner(new File(FILENAME));
			while (reader.hasNextDouble()) {
				weights.add(reader.nextDouble());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(weights.size());
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
	
	public void addEvalC4(double eval_c4) {
		evals_c4.add(eval_c4);
	}
	
	public void finalize() {
		// get size
		int n = evals_c1.size();
		
		ArrayList<Double> evals = new ArrayList<Double>();
		ArrayList<Double> rewards = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			// calculate eval(s_i^l,w) and store in an array
			evals.add(evals_c1.get(i)*weights.get(0) + evals_c2.get(i)*weights.get(1) + evals_c3.get(i)*weights.get(2) + evals_c4.get(i)*weights.get(3));
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
		double tmp_4 = 0.0;
		for (int i = 0; i < n-1; i++) {
			// tmp_j = sum dr(s_i^l,w)/dw_j * td
			double coe_1 = Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c1.get(i);
			double coe_2 = Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c2.get(i);
			double coe_3 = Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c3.get(i);
			double coe_4 = Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c4.get(i);
			tmp_1 += coe_1 * td;
			tmp_2 += coe_2 * td;
			tmp_3 += coe_3 * td;
			tmp_4 += coe_4 * td;
		}
		
		// update weights; new_w_j <- old_w_j + alpha * tmp_j
		double new_w1 = weights.get(0) + alpha*tmp_1;
		double new_w2 = weights.get(1) + alpha*tmp_2;
		double new_w3 = weights.get(2) + alpha*tmp_3;
		double new_w4 = weights.get(3) + alpha*tmp_4;
		// write new weights to file
		try {
			FileWriter writer = new FileWriter(new File(FILENAME));
			writer.write(new_w1 + "\n" + new_w2 + "\n" + new_w3 + "\n" + new_w4 + "\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
