package part2leapmotion;

import org.opencv.core.Mat;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import back.Control;
import part2leapmotion.Camera;
import puissance4.Puissance4;

public class Puissance  extends SimpleApplication {
	
	
	Camera camera;

	public static void main(String[] args) {
		Puissance4 app = new Puissance4();
		app.setShowSettings(false);
		app.start();
	}

	Control control = new Control();
	int idJoueur1 = control.getJoueur01();
	int idJoueur2 = control.getJoueur02();
	private float larBox = 0.5f, larBoxZ = 0.01f;
	private float contour = 0.050f;
	private float pas = 2 * larBox + contour;
	private boolean isRunning = true;
	Spatial nodeEchiqieur;
	Geometry[][] tabBox = new Geometry[6][7];
	private int nbLigne = 6;
	private int nbColonne = 7;
	detection det = new detection();

	@Override
	public void simpleInitApp() {
		camera = new Camera();
		camera.init();
		nodeEchiqieur = createEchiquier();
		rootNode.attachChild(nodeEchiqieur);
		centreNodeEchiquier();
		/* rootNode.attachChild(refer)); */
		initKeys();

	}

	@Override
	public void simpleUpdate(float tpf) {
		Mat m2 = camera.lecture();
		if (m2 != null) {
			det.RVBTOMB(m2);

			int nbcontours = det.DetectionContour(m2);
			// System.out.println("nbcontours: "+nbcontours);
		}

	}

	private void centreNodeEchiquier() {
		nodeEchiqieur.setLocalTranslation(-3, 3, 0);
		System.out.println("settings.getHeight() " + settings.getHeight());
		System.out.println("settings.getWidth() " + settings.getWidth());
		System.out.println("settings.getMinHeight() " + settings.getMinHeight());
	}

	private Spatial createEchiquier() {
		float posX = 0, posY = 0, posZ = 0;
		Node racineEchiquier = new Node("racineEchiquier");
		for (int i = 0; i < nbLigne; i++) {
			posX = 0;
			for (int j = 0; j < nbColonne; j++) {
				tabBox[i][j] = createBoxGris(posX, posY, posZ, i, j);
				racineEchiquier.attachChild(tabBox[i][j]);
				posX = posX + pas;
			}
			posY = posY - pas;
		}
		return racineEchiquier;
	}

	private Geometry createBoxGris(float posX, float posY, float posZ, int i, int j) {
		Box b = new Box(larBox, larBox, larBoxZ);
		Geometry box = new Geometry("Player" + i, b);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

		mat.setColor("Color", ColorRGBA.Gray);

		box.setMaterial(mat);
		box.move(posX, posY, posZ);
		return box;
	}

	private void initKeys() {
		// You can map one or several inputs to one named action
		inputManager.addMapping("colonne01", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("colonne02", new KeyTrigger(KeyInput.KEY_B));
		inputManager.addMapping("colonne03", new KeyTrigger(KeyInput.KEY_C));
		inputManager.addMapping("colonne04", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("colonne05", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addMapping("colonne06", new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping("colonne07", new KeyTrigger(KeyInput.KEY_G));

		inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE));

		inputManager.addListener(actionListener, "colonne01", "colonne02", "colonne03");
		inputManager.addListener(actionListener, "colonne04", "colonne05", "colonne06");
		inputManager.addListener(actionListener, "colonne07");
		inputManager.addListener(analogListener, "Rotate");
	}

	private final ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {
			int numLigne = 0, numColonne = 0;
			Material mat;
			if (name.contains("colonne") && !keyPressed) {
				String ch = name.substring(8);
				numColonne = Integer.parseInt(ch) - 1;
				if (numColonne >= 0 && numColonne < 7) {
					numLigne = control.gestionAction(numColonne);
					if (numLigne >= 0 && numLigne < 6) {
						mat = tabBox[numLigne][numColonne].getMaterial();
						if (control.getRolejoueur() == control.getJoueur01())
							mat.setColor("Color", ColorRGBA.Cyan);
						else
							mat.setColor("Color", ColorRGBA.Orange);
						tabBox[numLigne][numColonne].setMaterial(mat);
					} else
						System.out.println("code fin " + numLigne);
				}
			}
			System.out.println(control.getRolejoueur() + " numLigne." + numLigne + " numColonne=" + numColonne);
		}
	};

	private final AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {
			if (isRunning) {
				if (name.equals("Rotate")) {
					nodeEchiqieur.rotate(0, value * speed, 0);
				}
			} else {
				System.out.println("Press P to unpause.");
			}
		}
	};

	////
	public Node repere() {
		float pas = 0.2f;
		int nbLigne = 10, nbColonne = 10;
		float posX = 0, posY = 0, posZ = 0;
		Node racineEchiquier = new Node();
		for (float i = 0; i < nbLigne; i = i + pas) {
			posX = 0;
			for (float j = 0; j < nbColonne; j = j + pas) {
				System.out.println("(" + posX + "," + posY + "," + posZ + ")");
				Geometry geo = createBoxGrisRepere(posX, posY, posZ);
				racineEchiquier.attachChild(geo);
				posX = posX + pas;
			}
			posY = posY - pas;
		}
		return racineEchiquier;
	}

	private Geometry createBoxGrisRepere(float posX, float posY, float posZ) {
		Box b = new Box(0.009f, 0.009f, 0.005f);
		Geometry box = new Geometry("Player", b);
		Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Gray);
		box.setMaterial(mat);
		box.move(posX, posY, posZ);
		return box;
	}

}
