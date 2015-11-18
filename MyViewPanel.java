package Tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 * 视图
 * @author rock
 *
 */
public class MyViewPanel extends JPanel {
	private boolean[][] currentbvSauqre;
	private int rowCounts;
	private int columnCounts;
	private static int borderWidth=0;
	public MyViewPanel() {
		 rowCounts=ControlerComponent.rowCountSquare;
		 columnCounts=ControlerComponent.columnCountSquare;
		 
		this.setSize(columnCounts*ControlerComponent.BaseSquareEdgeLen+2*borderWidth,
				rowCounts*ControlerComponent.BaseSquareEdgeLen+2*borderWidth);
		//this.setBorder(BorderFactory.createLineBorder(Color.GREEN, borderWidth));
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLUE, Color.GRAY));
		//this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.BLUE, Color.GRAY));
		//this.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED, Color.BLUE, Color.GRAY));
	   currentbvSauqre=null;
	  
	}
	/**
	 * 显示
	 * @param bvSquare 二维状态矩阵
	 */
	public void showView(final boolean[][] bvSquare){
		currentbvSauqre=bvSquare;
		this.repaint();
	}
	@Override
	public final  void paintComponent(Graphics g) {
		
		if(currentbvSauqre==null)
			return;
		Graphics2D g2=(Graphics2D)g;
		Rectangle2D rec=new Rectangle2D.Float();
		int baseedgelen=ControlerComponent.BaseSquareEdgeLen;
		for(int i=0;i<columnCounts;i++)
			for(int j=0;j<rowCounts;j++){
				if(currentbvSauqre[i][j]){
				rec.setRect(i*baseedgelen, j*baseedgelen, baseedgelen,baseedgelen);
				g2.draw(rec);
				}
			}
				
	}
}
