package lab;
import java.util.ArrayList;
import java.util.Collections;



public class Generator {

	private int[][] lab;

	int count = 0;

	private void init(int N) {

		lab = new int[N + 2][N + 2];
		

		
		for (int x = 0; x < N + 2; x++) {
			for (int y = 0; y < N + 2; y++) {
			
				lab[x][y] = 1;
			}
		}

	}

	private void generate(int x, int y) {
		lab[x][y] = 1;
		count++;


	
		while (lab[x][y + 1] != 1 || lab[x + 1][y] != 1 || lab[x][y - 1] != 1
				|| lab[x - 1][y] != 1) {

		
			while (count <= lab.length * 100) {
				

				System.out.println();
				int r = (int) (Math.random() * 8);
				if (r == 4 && lab[x][y + 1] != 1 && y + 1 < lab.length - 1) {

					generate(x, y + 1);
					System.out.println("test");
					break;

				} else if (r == 5 && lab[x + 1][y] != 1
						&& x + 1 < lab.length - 1) {

					generate(x + 1, y);
					break;
				} else if (r == 6 && lab[x][y - 1] != 1 && y - 1 > 0) {

					generate(x, y - 1);
					break;
				} else if (r == 7 && lab[x - 1][y] != 1 && x - 1 > 0) {

					generate(x - 1, y);
					break;
				}
			}
		}

	}

	public void makemal(int x, int y, char dir) {

		
		int wall = 1;
		int room = 2;
		count++;
		if(lab.length<150 || count%lab.length == 0){

		}
			
		
		
		int w = (int) 2;
		int nx = (int) (Math.random()*lab.length/2);
	
		
		Integer[] r = randomizelist();

		for (int j = 0; j < r.length; j++) {

			switch (r[j]) {

			case 1:
				// hoch
				System.out.println(dir);
				if (y - 2 <= 0) {
					continue;
				}
				if (lab[x][y - 2] != 2) {
					lab[x][y - 2] = w;
					lab[x][y - 1] = w;
					makemal(x, y - 2, 'u');
				}

				break;
			case 2:
				// rechts

				if (x + 2 >= lab.length - 1) {
					continue;
				}
				if (lab[x + 2][y] != 2) {
					lab[x + 2][y] = w;
					lab[x + 1][y] = w;
					makemal(x + 2, y, 'r');
				}

				break;
			case 3:
				// links

				if (x - 2 <= 0) {
					continue;
				}
				if (lab[x - 2][y] != 2) {
				
					lab[x - 2][y] = w;
					lab[x - 1][y] = w;
					makemal(x - 2, y, 'l');
				}

				break;
			case 4:
				// runter
				if (y + 2 >= lab.length - 1) {
					continue;
				}
				if (lab[x][y + 2] != 2) {
					lab[x][y + 2] = w;
					lab[x][y + 1] = w;
					makemal(x, y + 2, 'd');
				}
				break;

			}
			
		}
		
	}

	public int[][] giveLab(int N) {
		init(N);
		makemal(1, 1, 'd');
		for (int x = 0; x < N + 2; x++) {
			for (int y = 0; y < N + 2; y++) {
				if(lab[x][y] == 2){
					lab[x][y] =	1;
				}else{
					lab[x][y] =	2;
				}
				
			}
		}
		
		while(true){
			int s =(int) (Math.random()*lab.length);
			if(lab[s][1] ==1){
				lab[s][0] =1;
				break;
			}
			
			
		}
		while(true){
			int s =(int) (Math.random()*lab.length);
			if(lab[s][lab.length-3] ==1){
				lab[s][lab.length-2] =1;
				lab[s][lab.length-3] =1;
				lab[s][lab.length-1] =1;
				break;
			}
			
			
		}
		
		
		return lab;
	}

	public Integer[] randomizelist() {
		ArrayList<Integer> rand = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++)
			rand.add(i + 1);
		Collections.shuffle(rand);
	
		
		return rand.toArray(new Integer[4]);

	}

	public static void main(String[] args) {
		
		Generator g = new Generator();
		int [][] arr = g.giveLab(25);
		printlab(arr);
		
	}

	public static void printlab(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				int val = arr[i][j];
				if(val==2) System.out.print("##");
				if(val==1) System.out.print("  ");
				if(val==3) System.out.print("--");
				
			}
			System.out.println();
		}
	}
}