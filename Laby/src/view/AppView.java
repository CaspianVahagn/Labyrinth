package view;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.Generator;

public class AppView extends Application {
	public static Stage primaryStage;
	private static int[][] lab;

	private static int count = 0;
	static boolean geloest = false;
	static int tempstep = Integer.MAX_VALUE;
	static int tempLab[][];
	public static Rectangle r[][];
	public static Pane field;
	public static SimpleStringProperty commands = new SimpleStringProperty("");
	public static List<String> commandList = new ArrayList<>(); 
	public static long elapsed = 0;

	public static void writeBigPixel(int x, int y, int size, Color c) {
		Rectangle rec = new Rectangle();
		r[x][y] = rec;
		x = x * size;
		y = y * size;
		rec.setWidth(size);
		rec.setHeight(size);
		rec.setFill(c);
		rec.relocate(x, y);
		field.getChildren().add(rec);

	}

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;
		BorderPane root = new BorderPane();
		field = new Pane();
		Button b = new Button("solve");
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				int start = 0;
				for (int i = 0; i < lab.length; i++) {
					if (lab[i][0] == 1)
						start = i;

				}
				System.out.println(start);
				final int s = start;

				abKlappern(1, s, 0);
				count = 0;
				Iterator<String> it = commandList.listIterator();
				AnimationTimer t = new AnimationTimer() {

					@Override
					public void handle(long arg0) {

						
							if (it.hasNext()) {

								String[] com = it.next().split(";");
								int x = Integer.parseInt(com[0]);
								int y = Integer.parseInt(com[1]);
								if (com[2].contains("w")) {
									r[x][y].setFill(Color.WHITE);
								} else {
									r[x][y].setFill(Color.GREEN);
								}
							}
						

					}
				
				};
				t.start();
			}
		});

		commands.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

				String[] com = arg2.split(";");
				int x = Integer.parseInt(com[0]);
				int y = Integer.parseInt(com[1]);
				if (com[2].contains("w")) {
					r[x][y].setFill(Color.WHITE);
				} else {
					r[x][y].setFill(Color.GREEN);
				}

			}
		});

		root.setBottom(b);
		root.setCenter(field);

		Generator g = new Generator();
		lab = g.giveLab(155);
		int size = 4;

		tempLab = new int[lab.length][lab[0].length];
		r = new Rectangle[lab.length][lab.length];
		paintLab(size);
		Scene scene = new Scene(root, 1200, 700);

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
				writeBigPixel(f, i, size, col);

			}
		}
	}

	public static boolean abKlappern(int x, int y, int steps) {
		count++;

		boolean test = true;

		int tempo;

		if (x == lab[0].length) {
			commands.set((x - 1) + ";" + (y) + ";g");
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

		if (geloest)
			return true;

		if (x < 0 || y < 0 || x > lab[1].length - 1 || y > lab.length - 1) {
			return false;
		}

		if (lab[y][x] == 2 || lab[y][x] == 3) {
			return false;
		}

		if (x != lab[0].length - 1) {
			lab[y][x] = 3;
			commandList.add(x + ";" + y + ";" + "g");

		}

		if (steps > tempstep || steps + abstandBerechnen(x, y) > tempstep) {
			lab[y][x] = 1;
			commandList.add(x + ";" + y + ";" + "w");
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
		commandList.add(x + ";" + y + ";" + "w");

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
