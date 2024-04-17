
public class Gold {
	final static double PHI = (1 + Math.sqrt(5)) / 2;
	int count;

	static double f(double x) {
		return 3 * x * x - 8 * x + 6;
	}

	double gold(double a, double b, double x, double y, double n, double eps) {
		double x1, x2, f1, f2;
		if ((b - a) > eps) {
			if (n == 1) {
				x1 = 2 * (a + (b - a) / 2) - x;
				x2 = x;
				f1 = f(x1);
				f2 = y;
			} else {
				x1 = x;
				x2 = 2 * (a + (b - a) / 2) - x;
				f1 = y;
				f2 = f(x2);
			}
			if (f1 > f2) {
				count++;
				return gold(x1, b, x2, f2, 2, eps);
			} else {
				count++;
				return gold(a, x2, x1, f1, 1, eps);
			}
		} else {
			count++;
			return a;
		}
	}

	public static void main(String[] args) {
		Gold GS = new Gold();
		double x, x1;
		x1 = (20 + 0) / PHI;
		x = GS.gold(0, 20, x1, f(x1), 1, 0.01);
		System.out.println("Iterations=" + GS.count + " x = " + x + " f(x) = " + f(x));

	}
}
