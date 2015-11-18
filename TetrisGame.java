package Tetris;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;



public class TetrisGame {  
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			    JFrame frame=new GameFrame();
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.setTitle("Tetris");
			    frame.setResizable(false);
			    frame.setVisible(true);
			}
		});
	}
	
	
}
/**
 *游戏界面
 *
 */
class GameFrame extends JFrame{
	public  static int ControlerFrameWidth=500;//整个游戏界面的宽度
	public static int ControlerFrameHight=700;//整个游戏界面的高度
	
	public GameFrame() {
		JComponent controler=new ControlerComponent();
		this.add(controler);
	   this.setSize(controler.getWidth(), controler.getHeight());
	 
	   createMenu();
	//改变整个界面的观感
		  try
          {
             UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
             SwingUtilities.updateComponentTreeUI(this);
          }
          catch (Exception e)
          {
             e.printStackTrace();
          }
	}

	private void createMenu() {
		// TODO Auto-generated method stub
		JMenuBar menuBar=new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu fileMenu=new JMenu("File");
		menuBar.add(fileMenu);
		JMenu aboutMenu=new JMenu("About");
		menuBar.add(aboutMenu);
		
		JMenuItem exitItem=new JMenuItem("exit");
		fileMenu.add(exitItem);
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
	}
}
