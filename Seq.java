
public class Seq {

	public double method_sek(double x_prev, double x_curr, double e) {
		double x_next = 0;
		double tmp;
		do {
			tmp = x_next;
			x_next = x_curr - f(x_curr) * (x_prev - x_curr) / (f(x_prev) - f(x_curr));
			x_prev = x_curr;
			x_curr = tmp;
		} while (Math.abs(x_next - x_curr) > e);
		return x_next;
	}

	public static double f(double x) {
		// double rez = Math.pow(x, 3) - x * 5 - 5;
		// return rez;
		return Math.pow(x, 2) + 5 * x - 4;
	}

	public static void main(String[] args) {

		double x0 = -20;
		double x1 = 20;
		double e = 0.001;
		Seq s = new Seq();
		double x = s.method_sek(x0, x1, e);
		System.out.println(x);
	}

}
