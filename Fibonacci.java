import java.util.ArrayList;

public class Fibonacci {
	int count;

	static double f(double x) {
		return 3 * x * x - 8 * x + 6;

	}

	public ArrayList<Integer> F(int n) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(1);
		arr.add(1);
		for (int i = 2; i <= n - 1; i++) {
			arr.add(arr.get(i - 1) + arr.get(i - 2));
		}
		return arr;
	}

	public double fibonacci(double a, double b, double x, double y, double n, ArrayList<Integer> s, double l) {
		double x1, x2, f1, f2;

		if (count < (s.size() - 3)) {
			count = count + 1;
			if (n == 1) {
				x1 = a + l * s.get(s.size() - count - 2) / s.get(s.size() - 1);
				x2 = x;
				f1 = f(x1);
				f2 = y;

			} else {
				x1 = x;
				x2 = a + l * s.get(s.size() - count - 1) / s.get(s.size() - 1);
				f1 = y;
				f2 = f(x2);
			}
			if ((f1) > (f2)) {
				return fibonacci(x1, b, x2, f2, 2, s, l);
			} else {
				return fibonacci(a, x2, x1, f1, 1, s, l);
			}
		} else {
			return a;
		}

	}

	public static void main(String[] args) {
		double x1, x;
		double a, b;
		int n = 18;
		a = 0;
		b = 20;
		Fibonacci fbr = new Fibonacci();
		ArrayList<Integer> arr;
		arr = fbr.F(n);

		x1 = a + (b - a) * arr.get(n - 2) / arr.get(n - 1);
		x = fbr.fibonacci(a, b, x1, f(x1), 1, arr, b - a);
		System.out.println("Iterations=" + n + " x = " + x + " f(x) = " + f(x));

	}

}
