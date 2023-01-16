package part2leapmotion;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class image {
	 public static void main( String[] args ) {
		 System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		 Mat m1= Imgcodecs.imread("image1.png");
		 Mat m2=Imgcodecs.imread("image2.png");

	Mat m= new Mat(	m1.rows(),m1.cols()+m2.cols(),m1.channels());
	
	for(int i=0;i<m1.rows();i++){	
		for(int j=0;j<m1.cols();j++) {
			double[] tab1=m1.get(i,j);
			m.put(i,j,tab1);
			
			
		}
	}
	
	for(int i=0;i<m1.rows();i++){	
		for(int j=0;j<m1.cols();j++) {
			double[] tab2=m2.get(i,j);
			m.put(i,j+m1.cols(),tab2);
			
			
		}
	}
	Imgcodecs.imwrite("result.png",m);
	
	
	 }	

}
