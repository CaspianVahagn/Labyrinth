package view;

import java.awt.geom.GeneralPath;
import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.Generator;

public class AppView extends Application {
	public static Stage primaryStage;
	private static int[][] lab;
	private static PixelWriter writer;
	private static int count = 0;
	static boolean geloest = false;
	static int tempstep = Integer.MAX_VALUE;
	static int tempLab[][];

	public static void writeBigPixel(PixelWriter w, int x, int y, int size, Color c) {
		x = x * size;
		y = y * size;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				w.setColor(x + i, y + j, c);
			}

		}

	}

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;
		BorderPane root = new BorderPane();
		Canvas c = new Canvas();
		Button b = new Button("solve");
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				int start = 0;
				for (int i = 0; i < lab.length; i++) {
					if(lab[i][0] == 1) start = i;
		
				}
				System.out.println(start);
				final int s = start;
				AnimationTimer t = new AnimationTimer() {
					
					@Override
					public void handle(long now) {
						abKlappern(1, s, 0);
						paintLab(10);
					}
				};
				t.start();
				
			}
		});
		c.setWidth(800);
		c.setHeight(800);
		root.setBottom(b);
		root.setCenter(c);
		writer = c.getGraphicsContext2D().getPixelWriter();
		Generator g = new Generator();
		lab = g.giveLab(15);
		int size = 10;
		paintLab(size);
		tempLab = new int[lab.length][lab[0].length];
		Scene scene = new Scene(root, 1200, 900);

		stage.setScene(scene);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				System.out.println("BYEBYE");
				Platform.exit();
				System.exit(0);

			}
		});
		stage.show();

	}

	private static void paintLab(int size) {
		for (int f = 0; f < lab.length; f++) {
			for (int i = 0; i < lab.length; i++) {

				Color col = Color.BLACK;
				int val = lab[i][f];
				if (val == 1)
					col = Color.WHITE;
				if (val == 3)
					col = Color.GREEN;
				writeBigPixel(writer, f, i, size, col);

			}
		}
	}

	public static boolean abKlappern(int x, int y, int steps) {
		count++;

		boolean test = true;

//      Aufg1.printLab(lab, Aufg1.WEISS);
		Generator.printlab(lab);

		int tempo;

		if (x == lab[0].length) {

			if (steps < tempstep) {
				tempstep = steps;
				for (int i = 0; i < lab.length; i++) {
					for (int j = 0; j < lab[0].length; j++) {
						tempLab[i][j] = lab[i][j];
						geloest = true;

					}
				}
//           System.out.println(tempstep);
			}
		}

		if (x < 0 || y < 0 || x > lab[1].length - 1 || y > lab.length - 1) {
			return false;
		}

		if (lab[y][x] == 2 || lab[y][x] == 3) {
			return false;
		}

		if (x != lab[0].length - 1) {
			lab[y][x] = 3;
		}

		if (steps > tempstep || steps + abstandBerechnen(x, y) > tempstep) {
			lab[y][x] = 1;
			return false;
		}

		// hoch
		if (abKlappern(x, y - 1, steps++)) {

			return true;
		}
		// rechts
		if (abKlappern(x + 1, y, steps++)) {

			return true;

		}

		// runter
		if (abKlappern(x, y + 1, steps++)) {

			return true;

		}

		// links
		if (abKlappern(x - 1, y, steps++)) {

			return true;
		}

		lab[y][x] = 1;

		return false;

	}

	public static int abstandBerechnen(int x, int y) {
		int abstand;
		abstand = (int) (Math.sqrt(Math.pow((double) (lab[0].length - 1 - x), 2) + (Math.pow((double) (y), 2))));

		return abstand;
	}

	public static void main(String[] args) {

		launch(args);
	}
}
