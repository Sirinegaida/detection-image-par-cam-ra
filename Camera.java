package part2leapmotion;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Camera {
	

	int id = 0;

	VideoCapture camera;

	

	public Mat lecture() {
		Mat image = new Mat(400,300,3);
		if (camera.read(image))
		  return image;
		System.out.println("lecture effectué");
		return null;
	}

	public void init() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		camera = new VideoCapture(id);
		if (!camera.isOpened()) {
			System.out.println("problème");
		}		
	}

}
