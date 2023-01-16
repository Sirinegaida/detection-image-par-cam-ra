package part2leapmotion;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import com.jme3.font.Rectangle;

public class detection {
	List<MatOfPoint> contours;
	Mat source;
	MatOfPoint2f contour;

	public Mat RVBTOMB(Mat m1) {
		this.source = m1;
		// 1er étape convertir imgage à une image gris
		// Imgproc.cvtColor(m1, m1, Imgproc.COLOR_BGR2GRAY);
		// 2eme étape convertir image à une image binaire noir/blanc
		Mat binary = new Mat();
		Imgproc.threshold(m1, binary, 100, 255, Imgproc.THRESH_BINARY);
		// Writing the image
		System.out.println("image gris saved");
		Imgcodecs.imwrite("imagegris.jpg", binary);

		return binary;

	}

	/*
	 *
	 * 
	 * //convexité public void convex() { for (int i=0; i< contours.size(); i++){
	 * MatOfPoint cm = new MatOfPoint();
	 * 
	 * approxCurve.convertTo(cm, CvType.CV_32S); int[] points = new int[8];
	 * cm.get(0, 0, points); if (Imgproc.isContourConvex(cm)){ contours.add(cm); }
	 * 
	 * }
	 * 
	 * List<Rectangle> rectangles = new ArrayList<Rectangle>();
	 * 
	 * Rectangle m=new Rectangle(contourSize, contourSize, contourSize, contourSize
	 * ); double angle[]=m.getAngle();
	 * 
	 * 
	 * 
	 * 
	 */

	public Mat DetectcolorRouge(Mat m1) {

		Scalar hsvMinRouge = new Scalar(0, 100, 100);
		Scalar hsvMaxRouge = new Scalar(10, 255, 255);
		// convertir en hsv
		Mat imgeHSVresult = new Mat();

		Imgproc.cvtColor(m1, imgeHSVresult, Imgproc.COLOR_BGR2HSV);

		Mat imgedetectedrouge = new Mat();
		Core.inRange(imgeHSVresult, hsvMinRouge, hsvMaxRouge, imgedetectedrouge);
		Imgcodecs.imwrite("imagerougedetect.png", imgedetectedrouge);

		detection dc = new detection();
		int nbcontourrouge = dc.DetectionContour(imgedetectedrouge);

		System.out.println("nombre contours rouge est " + nbcontourrouge);

		return imgedetectedrouge;

	}

	public Mat DetectcolorBleu(Mat m1) {

		Scalar hsvMinBleu = new Scalar(110, 50, 50);
		Scalar hsvMaxBleu = new Scalar(130, 255, 255);
		// convertir en hsv
		Mat imgeHSVresult = new Mat();

		Imgproc.cvtColor(m1, imgeHSVresult, Imgproc.COLOR_BGR2HSV);

		Mat imgedetectedbleu = new Mat();
		Core.inRange(imgeHSVresult, hsvMinBleu, hsvMaxBleu, imgedetectedbleu);
		Imgcodecs.imwrite("imagebluedetect.png", imgedetectedbleu);
		detection dc = new detection();

		int nbcontourrouge = dc.DetectionContour(imgedetectedbleu);

		System.out.println("nombre contours bleu est " + nbcontourrouge);
		return imgedetectedbleu;

	}

	public int DetectionContour(Mat gris) {
		System.out.println(
				"detection contours avec detection des formes de 4 sommets et élimination qui n'ont pas 4 sommets ");

		Mat binary = new Mat(gris.rows(), gris.cols(), gris.type(), new Scalar(0));
		Imgproc.threshold(gris, binary, 128, 255, Imgproc.THRESH_BINARY);
		// detection dc = new detection();
		// Mat binary = dc.RVBTOMB(m1);
// Finding Contours
		contours = new ArrayList<>();
		Mat hierarchey = new Mat();

		Imgproc.findContours(binary, contours, hierarchey, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
// Drawing the  Contours

		Scalar color = new Scalar(0, 255, 0);

		Mat imageContours = this.source.clone();

		Imgproc.drawContours(imageContours, contours, -1, color, 2, Imgproc.LINE_8, hierarchey, 2, new Point());

		Imgcodecs.imwrite("contourimage.jpg", imageContours);

// detection des formes de 4 sommets et élimination qui n'ont pas 4 sommets 
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		/*
		 * for (int i = 0; i < contours.size(); i++) {
		 * 
		 * MatOfPoint2f contour = new MatOfPoint2f(contours.get(i).toArray());
		 * 
		 * Imgproc.approxPolyDP(contour, approxCurve, contours.size() * 0.05, true); if
		 * (approxCurve.total() == 4) { MatOfPoint cm = new MatOfPoint();
		 * approxCurve.convertTo(cm, CvType.CV_32S); int[] points = new int[8];
		 * cm.get(0, 0, points);
		 * 
		 * } else { System.out.print("else de 4: i= " + i); System.out.println("  ");
		 * 
		 * contours.remove(i); }
		 * 
		 * }
		 */
		return contours.size();
	}

	public void Centre(Mat imagesrouce) {
		List<Moments> mu = new ArrayList<Moments>(contours.size());
		for (int i = 0; i < contours.size(); i++) {
			mu.add(i, Imgproc.moments(contours.get(i), false));
			Moments p = mu.get(i);
			int x = (int) (p.get_m10() / p.get_m00());
			int y = (int) (p.get_m01() / p.get_m00());

			// insère le centre dans l'image

			Imgproc.circle(imagesrouce, new Point(x, y), 4, new Scalar(10, 255, 255), 2);
			// System.out.println(" air de chaque rectangle" + " " + air);

			if (y == 0 && x == 0) {
				contours.remove(i);
			}

			System.out.println("" + x + " " + y);

		}

		HighGui.imshow("Drawing circle", imagesrouce);
		HighGui.waitKey();

	}

	public static void main(String args[]) throws Exception {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		detection dc = new detection();
		Mat sourcecouleur = Imgcodecs.imread("tabl.jpg");

		System.out.println("convertir en image gris ");

		// dc.RVBTOMB(sourcecouleur);

		// System.out.println("detection couleur rouge ");
		// dc.DetectcolorRouge(sourcecouleur);

		// System.out.println("detection couleur blue ");
		// dc.DetectcolorBleu(sourcecouleur);

		// System.out.println("detection de contours ");

		int nbcontours = dc.DetectionContour(sourcecouleur);
		System.out.println("nombre contours est " + nbcontours);

		// dc.Centre(sourcecouleur);

	}
}