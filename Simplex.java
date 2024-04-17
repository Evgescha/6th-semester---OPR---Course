import static java.lang.Math.pow;
import static java.lang.System.out;

public class Simplex {

	// общее количество переменных
	final static int n = 3;

	// количество базисных переменных
	final static int m = 3;

	// большое число
	final static double M = Double.MAX_VALUE;

	public static void main(String[] args) {
		// коэффициенты из целевой функции
		// коэффициенты из ограничений
		// коэффициенты при базисных переменных
		// базисные переменные
		// базисное решение

		double[] c = new double[n];
		double[][] a = new double[m][n];
		double[] cB = new double[m];
		int[] bV = new int[m];
		double[] bD = new double[m];

		// Задача №1
		c[0] = 108;
		c[1] = 112;
		c[2] = 126;

		a[0][0] = 0.8;
		a[0][1] = 0.5;
		a[0][2] = 0.6;

		a[1][0] = 0.4;
		a[1][1] = 0.4;
		a[1][2] = 0.3;

		a[2][0] = 0;
		a[2][1] = 0.1;
		a[2][2] = 0.1;

		cB[0] = 1;
		cB[1] = 1;
		cB[2] = 1;

		bV[0] = 2;
		bV[1] = 3;
		bV[2] = 4;

		bD[0] = 800;
		bD[1] = 600;
		bD[2] = 120;

		System.out.println("Задача №1. На максимум.");
		double[] max = simplexMax(c, a, cB, bV, bD);
		printArray(max);

		// Задача №2

		c[0] = -9;
		c[1] = -12;
		c[2] = -10;

		a[0][0] = 3;
		a[0][1] = 3;
		a[0][2] = 4;

		a[1][0] = 2;
		a[1][1] = 2;
		a[1][2] = 1;

		a[2][0] = 1;
		a[2][1] = 4;
		a[2][2] = 5;

		cB[0] = 1;
		cB[1] = 1;
		cB[2] = 1;

		bV[0] = 4;
		bV[1] = 5;
		bV[2] = 6;

		bD[0] = 60;
		bD[1] = 52;
		bD[2] = 18;

		System.out.println("Задача №2. На минимум.");
		double[] min = simplexMin(c, a, cB, bV, bD);
		printArray(min);
	}

	/**
	 * Симплекс-метод. Подсчет минимума.
	 */
	static double[] simplexMin(double[] c, double[][] a, double[] cB, int[] bV, double[] bD) {
		double[] delta = new double[n];
		double[] min = new double[m];

		out.println("\n\nПоиск минимума\n\n");
		int i, j, r = -1, s = -1;
		double[][] tmpA = new double[m][];
		double[] tmpBD = new double[m];

		int k = 0;
		while (true) {
			++k;

			// Считаем дельты, выделяем разрешающий столбец
			// (столбец с минимальной оценкой)
			double deltaMin = Double.POSITIVE_INFINITY;
			r = -1;
			for (j = 0; j < n; ++j) {
				double z = 0;
				for (i = 0; i < m; ++i) {
					z += cB[i] * a[i][j];
				}
				delta[j] = c[j] - z;
				if (deltaMin > delta[j]) {
					deltaMin = delta[j];
					r = j;
				}
			}

			// Условие выхода (все оценки неотрицательны)
			if (deltaMin >= 0 || k > 100) {
				break;
			}

			// Определяем разрешающую строку (с минимальным отношением
			// bD[i] / a[i][r])
			double minRow = Double.POSITIVE_INFINITY;
			s = -1;
			for (i = 0; i < m; ++i) {
				min[i] = bD[i] / a[i][r];
				if (min[i] < 0) {
					min[i] = Double.NaN;
				}
				if (minRow > min[i]) {
					minRow = min[i];
					s = i;
				}
			}

			out.println("Разрешающий столбец: r = " + r);
			out.println("Разрешающая строка:  s = " + s);
			printTable(c, a, cB, bV, bD, min);

			// Разрешающий элемент (на пересечении разрешающих строки и столбца)
			double element = a[s][r];

			// Сохраняем содержимое массивов (т.к. их значения в ходе
			// вычисления изменяются, но остаются нужными для этих вычислений
			// ну как-то так :)
			for (i = 0; i < m; ++i) {
				tmpA[i] = new double[n];
				for (j = 0; j < n; ++j) {
					tmpA[i][j] = a[i][j];
				}
				tmpBD[i] = bD[i];
			}

			// Вносим переменную в базис,
			// Пересчитываем базисное решение и коэффицетов переменных -

			// - в разрешающей строке
			bV[s] = r;
			cB[s] = c[r];
			for (j = 0; j < n; ++j) {
				a[s][j] /= element;
			}
			bD[s] /= element;

			// - в остальных строках
			for (i = 0; i < m; ++i) {
				// разрешающая строка уже пересчитана
				if (i == s) {
					continue;
				}

				// элемент разрешающего столбца
				double air = tmpA[i][r];

				// пересчет коэфициентов
				for (j = 0; j < n; ++j) {
					a[i][j] -= (air * tmpA[s][j]) / element;
				}

				// пересчет базисного решения
				bD[i] -= (air * tmpBD[s]) / element;
			}

			printDelta(delta);
			out.println("----------------------------------------------------");
		}

		out.println("Разрешающий столбец: r = " + (r + 1));
		out.println("Разрешающая строка:  s = " + (s + 1));
		printTable(c, a, cB, bV, bD, min);
		printDelta(delta);
		return printDecision(bV, bD);
	}

	/**
	 * Симплекс-метод. Подсчет максимума.
	 */
	static double[] simplexMax(double[] c, double[][] a, double[] cB, int[] bV, double[] bD) {
		double[] delta = new double[n];
		double[] min = new double[m];

		out.println("\n\nПоиск максимума\n\n");
		int i, j, r = -1, s = -1;
		double[][] tmpA = new double[m][];
		double[] tmpBD = new double[m];

		int k = 0;
		while (true) {
			++k;

			// Считаем дельты, выделяем разрешающий столбец
			// (столбец с максимальной оценкой)
			double deltaMax = Double.NEGATIVE_INFINITY;
			r = -1;
			for (j = 0; j < n; ++j) {
				double z = 0;
				for (i = 0; i < m; ++i) {
					z += cB[i] * a[i][j];
				}
				delta[j] = c[j] - z;
				if (deltaMax < delta[j]) {
					deltaMax = delta[j];
					r = j;
				}
			}

			// Условие выхода (все оценки неположительны)
			if (deltaMax <= 0 || k > 100) {
				break;
			}

			// Определяем разрешающую строку (с минимальным отношением
			// bD[i] / a[i][r])
			double minRow = Double.POSITIVE_INFINITY;
			s = -1;
			for (i = 0; i < m; ++i) {
				min[i] = bD[i] / a[i][r];
				if (min[i] < 0) {
					min[i] = Double.NaN;
				}
				if (minRow > min[i]) {
					minRow = min[i];
					s = i;
				}
			}

			out.println("Разрешающий столбец: r = " + r);
			out.println("Разрешающая строка:  s = " + s);
			printTable(c, a, cB, bV, bD, min);

			// Разрешающий элемент (на пересечении разрешающих строки и столбца)
			double element = a[s][r];

			// Сохраняем содержимое массивов (т.к. их значения в ходе
			// вычисления изменяются, но остаются нужными для этих вычислений
			// ну как-то так :)
			for (i = 0; i < m; ++i) {
				tmpA[i] = new double[n];
				for (j = 0; j < n; ++j) {
					tmpA[i][j] = a[i][j];
				}
				tmpBD[i] = bD[i];
			}

			// Вносим переменную в базис,
			// Пересчитываем базисное решение и коэффицетов переменных -

			// - в разрешающей строке
			bV[s] = r;
			cB[s] = c[r];
			for (j = 0; j < n; ++j) {
				a[s][j] /= element;
			}
			bD[s] /= element;

			// - в остальных строках
			for (i = 0; i < m; ++i) {
				// разрешающая строка уже пересчитана
				if (i == s) {
					continue;
				}

				// элемент разрешающего столбца
				double air = tmpA[i][r];

				// пересчет коэфициентов
				for (j = 0; j < n; ++j) {
					a[i][j] -= (air * tmpA[s][j]) / element;
				}

				// пересчет базисного решения
				bD[i] -= (air * tmpBD[s]) / element;
			}

			printDelta(delta);
			out.println("----------------------------------------------------");
		}

		out.println("Разрешающий столбец: r = " + (r + 1));
		out.println("Разрешающая строка:  s = " + (s + 1));
		printTable(c, a, cB, bV, bD, min);
		printDelta(delta);
		return printDecision(bV, bD);
	}

	/**
	 * Печатает решение
	 */
	static double[] printDecision(int[] bV, double[] bD) {
		int i, j;
		out.println("Все оценки неположительны, подсчет завершен.");
		out.print("Решение x = (");
		double[] res = new double[m];
		boolean f;
		for (j = 0; j < n; ++j) {
			f = false;
			for (i = 0; i < m; ++i) {
				if (bV[i] == j) {
					if (j < m) {
						res[j] = bD[i];
					}
					out.print(round(bD[i], 2));
					f = true;
					break;
				}
			}
			if (!f) {
				out.print("0");
			}
			if (j < n - 1) {
				out.print(", ");
			}
		}
		out.print(")");
		return res;
	}

	/**
	 * Печатает строку оценок
	 */
	static void printDelta(double[] delta) {
		out.print("\t\t\t\t");
		for (int j = 0; j < n; ++j) {
			out.print(round(delta[j], 2) + "\t");
		}
		out.println("delta[j]");
	}

	/**
	 * Печатает таблицу
	 */
	static void printTable(double[] c, double[][] a, double[] cB, int[] bV, double[] bD, double[] min) {
		int i, j;
		// вывод: строка коэффициентов
		out.print("\t\t\t\t");
		for (j = 0; j < n; ++j) {
			out.print(round(c[j], 2) + "\t");
		}
		out.println("C[j]");

		// вывод: ряд x
		out.print("\tcB\tbV\tbD\t");
		for (j = 0; j < n; ++j) {
			out.print("x[" + j + "]\t");
		}
		out.println("bD[i] / a[i][r]");

		for (i = 0; i < m; ++i) {
			out.print("\t" + round(cB[i], 2) + "\tx[" + (bV[i] + 1) + "]\t" + round(bD[i], 2) + "\t");
			for (j = 0; j < n; ++j) {
				out.print(round(a[i][j], 2) + "\t");
			}
			out.println(round(min[i], 2));
		}
	}

	static void printArray(double[] ar) {
		out.println();
		for (int i = 0; i < ar.length; ++i) {
			out.println("\t[" + i + "] = " + ar[i]);
		}
		out.println();
	}

	static String round(double n, int p) {
		if (Double.isNaN(n)) {
			return "NaN";
		}
		if (Double.isInfinite(n)) {
			return "\u221E";
		}
		double d = pow(10, p);
		return (Math.round(n * d) / d) + "";
	}
}
