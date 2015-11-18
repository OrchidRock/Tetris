package Tetris;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.EventHandler;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
/**
 * 主控制器类，该类控制整个游戏过程中的用户交互
 * 图形显示，数据驱动
 * @version 1.0 2015-11-13
 * @author rock
 */

public class ControlerComponent extends JComponent {
	/*
	 * 二维状态数组的行数，也代表界面中的元方格行数
	 */
	public static final int rowCountSquare=24;
	/*
	 * 二维状态数组的列数，也代表界面中的元方格列数
	 */
	public  static final int columnCountSquare=20;
	/*
	 * 视图界面中元方格的像素大小(也可以是物理尺寸)
	 */
	public final static int BaseSquareEdgeLen=25;
	/*
	 * 方块移动命令类型也是边界检查类型
	 * MTT_LEFT左移一个单位,MTT_RIGHT右移一个单位
	 * MTT_UP 右移一个单位，MTT_DOWN 下移一个单位
	 * MTT_CW_ROTATE 顺时针选择90角度
	 */
	private  enum MoveTetrisType{
		 MTT_LEFT,	 MTT_RIGHT,  MTT_DOWN, MTT_CW_ROTATE
	}
	/*
	 * 定义状态选项
	 */
	private enum StateSecType{
		SST_Wating,SST_Running,SST_Pause,SST_End
	}
	
   /*
    * 该控制器界面的高度和宽度
    */
	public final static  int ControlerComWidth=200;
	public final static int ControlerComHight=300;
	/*
	 * 当前状态
	 */
	StateSecType currentState=StateSecType.SST_Wating;
	/*
	 * 数据模型
	 */
	private DataMoudle dataMoudle;
	/*
	 * 视图
	 */
	private MyViewPanel myViewPanel;
	private Timer  moveTimer;
	public ControlerComponent() {
		dataMoudle=new DataMoudle();
		myViewPanel=new MyViewPanel();
		
		this.setLayout(new BorderLayout());
		this.add(myViewPanel,BorderLayout.CENTER);
		
		this.setSize(ControlerComWidth+myViewPanel.getWidth(),
				myViewPanel.getHeight());
		
		JPanel wastPanel=new JPanel(new GridLayout(2, 1));
		
		JPanel moudleSecPenal=new JPanel();
		moudleSecPenal.add(new JLabel("模式选择："));
		
		JComboBox<String> mouldComo=new JComboBox<>();
		mouldComo.addItem("单机模式");
		mouldComo.addItem("单机-主机模式");
		mouldComo.addItem("单机-从机模式");
		moudleSecPenal.add(mouldComo);
		
		JPanel buttongroupPenal=new JPanel(new GridLayout(5, 1,0,20)); 
		JButton rebeginButton=new JButton("重新开始");
		rebeginButton.addActionListener(
				EventHandler.create(ActionListener.class, this, "reBegin"));buttongroupPenal.add(rebeginButton);
		JButton pauseButton=new JButton("暂停");
		pauseButton.addActionListener(
				EventHandler.create(ActionListener.class, this, "pause"));buttongroupPenal.add(pauseButton);
		JButton continueButton=new JButton("继续");
		continueButton.addActionListener(
				EventHandler.create(ActionListener.class, this, "Continue"));buttongroupPenal.add(continueButton);
		JButton endButton=new JButton("结束");
		endButton.addActionListener(
				EventHandler.create(ActionListener.class, this, "end"));buttongroupPenal.add(endButton);
	
		wastPanel.add(moudleSecPenal);
		wastPanel.add(buttongroupPenal);
		this.add(wastPanel, BorderLayout.WEST);
		//添加边框
		
	    Border etched=BorderFactory.createEtchedBorder();//蚀刻边框
		Border titled= BorderFactory.createTitledBorder(etched, "控制区");
		wastPanel.setBorder(titled);
		
		//添加键盘侦听器
		this.addKeyListener(new ControlerKey());
		
	}
	@Override
	public void paintComponent(Graphics g){
		if(currentState==StateSecType.SST_Running){
			myViewPanel.showView(dataMoudle.getBvsquareCheck());
		}
	}
	
	public void reBegin(){
		dataMoudle.createNewTetris();
		repaint();
		/*if(moveTimer==null){
			moveTimer=new Timer(true);
		}else{
			moveTimer.cancel();
			
		}
		dataMoudle.clearBvsquareCheck();
		dataMoudle.createNewTetris();
		
		moveTimer.schedule(
				new TimerTask() {
					@Override
					public void run() {
						if(currentState==StateSecType.SST_Running){
							dataMoudle.moveTetries(MoveTetrisType.MTT_DOWN.name());
							repaint();
						}
					//	Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_DOWN, false);
						try {
							Robot robot=new Robot();
							robot.keyPress(KeyEvent.VK_DOWN);
						} catch (AWTException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}, 0, 500);
		//this.repaint();
		*/
		currentState=StateSecType.SST_Running;
		
		this.requestFocusInWindow();//让窗口获得焦点
	
}
	public void pause(){
		moveTimer.cancel();
		currentState=StateSecType.SST_Pause;
	}
	public void Continue(){
		/*moveTimer=new Timer(true);
		moveTimer.schedule(
				new TimerTask() {
					@Override
					public void run() {
						if(currentState==StateSecType.SST_Running){
							dataMoudle.moveTetries(MoveTetrisType.MTT_DOWN.name());
							repaint();
						}
					}
				}, 0, 500);
		currentState=StateSecType.SST_Running;
		this.requestFocusInWindow();//让窗口获得焦点
*/	}
	public void end(){
		moveTimer.cancel();
	}
	class ControlerKey extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
		if(currentState!=StateSecType.SST_Running)
				return;
			boolean isuse=true;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP :
				System.out.println("UP");
				//currentState=StateSecType.SST
				dataMoudle.moveTetries(MoveTetrisType.MTT_CW_ROTATE.name());
				isuse=true;
				break;
			case KeyEvent.VK_LEFT:
				System.out.println("LEFT");
				dataMoudle.moveTetries(MoveTetrisType.MTT_LEFT.name());
				isuse=true;
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("RIGHT");
				dataMoudle.moveTetries(MoveTetrisType.MTT_RIGHT.name());
				isuse=true;
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("down");
				dataMoudle.moveTetries(MoveTetrisType.MTT_DOWN.name());
				isuse=true;
				break;
			default:
				isuse=false;
				break;
			}
			if(isuse)
				repaint();
			else
				isuse=true;
		}
		
	}
}
