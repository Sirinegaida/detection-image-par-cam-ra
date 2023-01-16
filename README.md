# detection-image-par-camera
lecture camera
    ///inctanciation de VideoCapture (test d'ouverture de camera)
    /// lecture image detecté 

OpenCv 
1er étape convertir imgage à une image gris
		// Imgproc.cvtColor(m1, m1, Imgproc.COLOR_BGR2GRAY);
		// 2eme étape convertir image à une image binaire noir/blanc
2 ème étape :detect contours
// Finding Contours
// Drawing the  Contours
// detection des formes de 4 sommets et élimination qui n'ont pas 4 sommets 
3ème étape:detecs couleur 
		// convertir en hsv
    //insère les intervales des couleurs qui va detectés
4ème étape: detect centre 
// insère le centre dans l'image
