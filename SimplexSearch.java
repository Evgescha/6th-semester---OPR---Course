import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//класс для хранения точки
class XY {

	double x, y;

	XY(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}

public class SimplexSearch {

	// public ArrayList<XY> point = new ArrayList<XY>(); //список точек
	static double l = 1; // длина ребра
	double n = 2; // размерность пространства (вершины)
	static double delta1; // прирощения
	static double delta2;

	// Дельта 1
	public double getDelta1() {
		delta1 = (Math.sqrt(n + 1) + n - 1) * l / (n * Math.sqrt(2));
		return delta1;
	}

	// Дельта 2
	public double getDelta2() {
		delta2 = (Math.sqrt(n + 1) - 1) * l / (n * Math.sqrt(2));
		return delta2;
	}

	// Задание Функция
	public static double Function(double x, double y) {
		return Math.pow((x + 3) * y, 2) - 2;

	}

	// Находим точки первоначально симплекса
	public XY[] Calculate(XY xy) {
		XY[] points;
		points = new XY[3];
		points[0] = xy;

		XY xy2 = new XY(points[0].getX() + getDelta1(), points[0].getY() + getDelta2());

		points[1] = xy2;
		XY xy3 = new XY(points[0].getX() + getDelta2(), points[0].getY() + getDelta1());

		points[2] = xy3;

		return points;
	}

	// поиск максимального элемента массива
	int maxArr(double[] mas) {
		int i;
		int mx = 0;
		for (i = 0; i < mas.length; i++) {
			if (mas[i] > mas[mx])
				mx = i;
		}
		return mx;
	}

	// поиск минимального элемента массива
	int minArr(double[] mas) {
		int i;
		int min = 0;
		for (i = 0; i < mas.length; i++) {
			if (mas[i] < mas[min])
				min = i;
		}
		return min;
	}

	// поиск индекса максимального
	int maxIndex(XY[] points) {
		double[] ptf = new double[3];
		for (int i = 0; i < ptf.length; i++)
			ptf[i] = Function(points[i].getX(), points[i].getY());
		return maxArr(ptf);
	}

	// поиск индекса минимального
	int minIndex(XY[] points) {
		double[] ptf = new double[3];
		for (int i = 0; i < ptf.length; i++)
			ptf[i] = Function(points[i].getX(), points[i].getY());
		return minArr(ptf);
	}

	// поиск центра тяжести
	XY simC(XY[] points, int index) {
		double сx = 0, сy = 0;
		for (int i = 0; i < points.length; i++) {
			if (i != index) {
				сx += points[i].getX();
				сy += points[i].getY();
			}

		}
		return new XY(сx / 2, сy / 2);
	}

	// проверка значения функции в точке
	boolean check(XY[] points, int index, XY newP) {
		double v1 = Function(points[index].getX(), points[index].getY());
		double v2 = Function(newP.getX(), newP.getY());
		if (v2 < v1) {
			points[index] = newP;
			return true;
		} else {
			return false;
		}
	}

	// поиск симметричной точки
	public XY newPoint(XY[] points, int index) {
		double x;
		double y;
		x = 2 * simC(points, index).getX() - points[index].getX();
		y = 2 * simC(points, index).getY() - points[index].getY();

		return new XY(x, y);
	}

	// реализация симплекса
	public XY Search(XY xy) throws IOException {
		XYSeriesCollection xyDataset = new XYSeriesCollection();

		XYSeries series = new XYSeries("Graphic");
		XY[] pt = Calculate(xy);
		int index;
		int k = 0;
		series.add(pt[0].getX(), pt[0].getY());
		while (true) {

			index = maxIndex(pt);

			XY pts = newPoint(pt, index);

			if (check(pt, index, pts) == true) {
				series.add(pts.getX(), pts.getY());

				System.out.println(k + " " + pts.getX() + " " + pts.getY() + " " + Function(pts.getX(), pts.getY()));

			} else {
				series.add(pt[minIndex(pt)].getX(), pt[minIndex(pt)].getY());
				xyDataset.addSeries(series);

				JFreeChart chart = ChartFactory.createXYLineChart("Simplex search", "x", "y", xyDataset,
						PlotOrientation.VERTICAL, true, true, true);
				XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
				final XYPlot plot = chart.getXYPlot();

				renderer.setSeriesShapesVisible(0, true);
				renderer.setSeriesPaint(0, Color.RED);
				plot.setRenderer(renderer);

				FileOutputStream fos = new FileOutputStream("123.png");

				ChartUtilities.writeChartAsPNG(fos, chart, 600, 600);
				return pt[minIndex(pt)];

			}

			k++;
		}

	}

	public static void main(String[] args) throws IOException {
		SimplexSearch sm = new SimplexSearch();
		XY xy = new XY(0, 10);

		xy = sm.Search(xy);
		System.out
				.println("MIN: X= " + xy.getX() + " Y= " + xy.getY() + " F(X,Y)= " + sm.Function(xy.getX(), xy.getY()));

	}

}
