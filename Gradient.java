import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

class XY {
	private double x, y;

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

public class Gradient {
	static double f(double x, double y) {
		return Math.pow((x + 3) * y, 2) - 2;
	}

	static double dfx(double x, double y) {
		return Math.pow(y, 2) * (2 * x + 6);
	}

	static double dfy(double x, double y) {
		return 2 * y * Math.pow(x + 3, 2);
	}

	ArrayList<XY> gradient_descendent(double x0, double y0, double eps, double h) {
		int k = 0;
		double G;
		double Y;
		double x1_p;
		double x2_p;

		ArrayList<XY> arr = new ArrayList<XY>();

		do {
			// System.out.println(x0 + " " + y0);
			XY xy = new XY();
			xy.setX(x0);
			xy.setY(y0);
			arr.add(xy);
			x1_p = x0;
			x2_p = y0;
			G = f(x0, y0);
			x0 = x1_p - h * dfx(x1_p, x2_p);
			y0 = x2_p - h * dfy(x1_p, x2_p);
			Y = f(x0, y0);
			k++;

		} while ((Math.abs(Y - G)) > eps);

		return arr;
	}

	public static void main(String[] args) throws IOException {
		Gradient gr = new Gradient();
		XYSeriesCollection xyDataset = new XYSeriesCollection();

		XYSeries series = new XYSeries("Grad");

		XY xy = new XY();
		ArrayList<XY> arr = new ArrayList<XY>();
		arr = gr.gradient_descendent(0, 10, 0.01, 0.001);
		for (int i = 0; i < arr.size(); i++) {
			xy = arr.get(i);

			series.add(xy.getX(), xy.getY());
			System.out.println(xy.getX() + " " + xy.getY() + " " + f(xy.getX(), xy.getY()));

		}
		series.add(xy.getX(), xy.getY());
		System.out.println("MIN: X= " + xy.getX() + " Y= " + xy.getY() + " F(X,Y)= " + f(xy.getX(), xy.getY()));

		xyDataset.addSeries(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Gradient", "x", "y", xyDataset, PlotOrientation.VERTICAL,
				true, true, true);
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		final XYPlot plot = chart.getXYPlot();

		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesPaint(0, Color.RED);

		plot.setRenderer(renderer);

		FileOutputStream fos = new FileOutputStream("123.png");
		ChartUtilities.writeChartAsPNG(fos, chart, 600, 400);

	}
}
