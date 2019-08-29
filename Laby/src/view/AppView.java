package view;

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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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

	public static List<List<String>> batches = new ArrayList<>();
	public static long elapsed = 0;
	public static SimpleIntegerProperty iters = new SimpleIntegerProperty(0);

	public static void writeBigPixel(int x, int y, int size, Color c) {
		Rectangle rec = new Rectangle();
		
		final int x_e = x,y_e=y;
		rec.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(rec.getFill().equals(Color.BLACK)) {
					rec.setFill(Color.WHITE);
					lab[y_e][x_e] = 1;
					tempLab[y_e][x_e] = 1;
				}else {
					rec.setFill(Color.BLACK);
					lab[y_e][x_e] = 2;
					tempLab[y_e][x_e] = 2;
				}
				
			}
		});
		
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
		Generator g = new Generator();
		int N = 175;

		int size = 3;
		lab = g.giveLab(N);
		BorderPane root = new BorderPane();
		field = new Pane();
		Button b = new Button("solve");
		Button b2 = new Button("solve-fast");
		Button b3 = new Button("Generate");
		root.setTop(b3);

		b2.setOnAction(new EventHandler<ActionEvent>() {

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
				paintLab(size, true);

			}
		});
		root.setRight(b2);
		batches.add(new ArrayList<String>());
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

				List<Iterator<String>> its = new ArrayList<>();

				for (List<String> lis : batches) {
					its.add(lis.iterator());
				}

				System.out.println("Batches: " + batches.size());
				AnimationTimer t = new AnimationTimer() {

					@Override
					public void handle(long arg0) {
						if (iters.get() < its.size()) {
							for (int i = 0; i < 5; i++) {

								if (its.get(iters.get()).hasNext()) {

									String[] com = its.get(iters.get()).next().split(";");
									int x = Integer.parseInt(com[0]);
									int y = Integer.parseInt(com[1]);
									if (com[2].contains("w")) {
										r[x][y].setFill(Color.WHEAT);
									} else {
										r[x][y].setFill(Color.GREEN);
									}
								} else {
									iters.set(iters.get() + 1);
								}
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

		tempLab = new int[lab.length][lab[0].length];
		r = new Rectangle[lab.length][lab.length];
		b3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				tempstep = Integer.MAX_VALUE;
				count = 0;
				batches.get(0).clear();
				lab = g.giveLab(N);
				tempLab = new int[lab.length][lab[0].length];

				paintLab(size, false);

			}
		});

		paintLab(size, false);
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

	private static void paintLab(int size, boolean temp) {
		if (temp)
			lab = tempLab;
		for (int f = 0; f < lab.length; f++) {
			for (int i = 0; i < lab.length; i++) {

				Color col = Color.BLACK;
				int val = lab[i][f];
				if (val == 1)
					col = Color.WHITE;
				if (val == 3 && temp)
					col = Color.ORANGE;
				if (val == 3 && !temp)
					col = Color.GREEN;

				writeBigPixel(f, i, size, col);

			}
		}
	}

	public static boolean abKlappern(int x, int y, int steps) {
		try {

			count++;
			List<String> commandList = batches.get(batches.size() - 1);
			if (commandList.size() >= Integer.MAX_VALUE - 100000)
				batches.add(new ArrayList<String>());
			boolean test = true;

			int tempo;

			if (x == lab[0].length) {
				commands.set((x - 1) + ";" + (y) + ";g");
				if (steps < tempstep) {
					tempstep = steps;
					System.out.println(tempstep);
					for (int i = 0; i < lab.length; i++) {
						for (int j = 0; j < lab[0].length; j++) {
							tempLab[i][j] = lab[i][j];
							geloest = true;

						}
					}
//           System.out.println(tempstep);
				}
			}

			// if (geloest)return true;

			if (x < 0 || y < 0 || x > lab[1].length - 1 || y > lab.length - 1) {
				return false;
			}

			if (lab[y][x] == 2 || lab[y][x] == 3) {
				return false;
			}

			if (x != lab[0].length - 1) {
				lab[y][x] = 3;
				batches.get(batches.size() - 1);
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
		} catch (StackOverflowError e) {
			System.err.println(e.getCause());
			e.printStackTrace();
			System.err.println(batches.get(0).size());
			long heapSize = Runtime.getRuntime().totalMemory();

			// Get maximum size of heap in bytes. The heap cannot grow beyond this size.//
			// Any attempt will result in an OutOfMemoryException.
			long heapMaxSize = Runtime.getRuntime().maxMemory();

			// Get amount of free memory within the heap in bytes. This size will increase
			// // after garbage collection and decrease as new objects are created.
			long heapFreeSize = Runtime.getRuntime().freeMemory();
			System.err.println("Free:" + heapFreeSize);
			System.err.println("heap:" + heapSize);
			System.err.println("Max:" + heapMaxSize);

			return false;
		}

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
