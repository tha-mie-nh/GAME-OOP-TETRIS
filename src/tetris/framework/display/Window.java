package tetris.framework.display;

import javax.swing.JFrame;

public class Window {

	public static final int WIDTH = 300;
	public static final int HEIGHT = 500;
	
	/**Creates window<br>
	 * Called on game startup
	 */
	public static void create() {
		JFrame window = new JFrame("Tetris");
		window.setBounds(100, 50, WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.add(new GameScreen());
		window.setVisible(true);
		System.out.println("[Framework][Display]: Created window");
	}
}