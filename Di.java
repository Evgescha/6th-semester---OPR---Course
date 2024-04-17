
public class Di {
	int count = 0;

	static double f(double x) {
		return 3 * x * x - 8 * x + 6;

	}

	double dich(double a, double b, double delta, double eps) {
		double x1, x2;
		if ((b - a) > eps) {
			x1 = a + (b - a) / 2 - delta;
			x2 = a + (b - a) / 2 + delta;
			if (f(x1) > f(x2)) {
				count++;
				return dich(x1, b, delta / 2, eps);
			} else {
				count++;
				return dich(a, x2, delta / 2, eps);
			}
		} else {
			count++;
			return a;
		}

	}

	public static void main(String[] args) {
		Di d = new Di();

		double x = d.dich(0, 20, 0.05, 0.0001);
		System.out.println("Iterations= " + d.count + " x = " + x + " f(x) = " + f(x));

	}

}
