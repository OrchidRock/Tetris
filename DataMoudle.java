package Tetris;

import java.util.Random;

/**
 * 该类主要完成整个游戏过程中数据的处理,并把结果以二维数组的
 * 形式传递给显示类
 * 该类还定义了要用到的Teries方块类型及其属性
 * @author rock
 * @version 1.1 2015-11-18
 */

public class DataMoudle {
	/*
	 * 更新命令类型
	 * UOT_CLEAR 清空小方块所在位置标记，即清0
	 * UOT_FULL 填充小方块所在位置标记，即置1
	 * 其余四个对应左、右、下移动的操作
	 */
	private enum UpdataOrderType{
		UOT_CLEAR,UOT_FULL,UOT_LCHECK,UOT_RCHECK,UOT_DOWNCHECK
	}
	/*
	 * 方块类型枚举变量 TST_SQUARE 正方形，TST_LSHAPE 左手L型
	 * TST_ZSHAPE 左Z型，TST_ISHAPE 长条型
	 */
	private enum TetrisShapeType{
		TST_SQUARE, TST_LSHAPE, TST_ZSHAPE, TST_ISHAPE
	}
	/*
	 * 方块类型的个数
	 */
	private static final int countTetris=4;
	/*
	 * 该二维数组标识各方块所在最小包含矩形的行数和列数
	 * [*][0]代表行数，[*][1]代表列数
	 */
	private static int[][] DefaultRowColOfTST={{2,2},{4,2},{2,3},{4,1}};
	/*
	 * 二维状态数组，true表示x,y下标所对应位置的元方块被填充，false表示不被填充
	 * 其访问权限要严格控制
	 */
	private boolean [][] bvSquareCheck;
	private int columnCountSquare;
	private int rowCountSquare;
	/*
	 * 当前正在运动的方块
	 */
	private RunningTetris runningTetris;
	/**
	 *初始化操作，为数组分配内存空间，初始化下一个运动方块
	 */
	public DataMoudle() {
		columnCountSquare=ControlerComponent.columnCountSquare;
		rowCountSquare=ControlerComponent.rowCountSquare;
		
		bvSquareCheck=new boolean[columnCountSquare][rowCountSquare];
		clearBvsquareCheck();
		//createNewTetris();
	}
	public void clearBvsquareCheck(){
		if(bvSquareCheck==null) return;
		for(int i=0;i<columnCountSquare;i++)
			for(int j=0;j<rowCountSquare;j++)
				bvSquareCheck[i][j]=false;
	}
	/**
	 * 返回当前二维状态数组的引用，不允许修改
	 */
	final boolean[][] getBvsquareCheck(){
		return bvSquareCheck;
	}
	/**
	 * 生成新的小方块，这里只是修改了runningTeris域值
	 * 是不是应该由该类自己负责新方块的生成？？？
	 */
	public void createNewTetris(){
		if(runningTetris==null)
			runningTetris=new RunningTetris();
		runningTetris.updataData();
		updataTetris(runningTetris, UpdataOrderType.UOT_FULL);
	}
	/**
	 * 根据控制器的移动命令来更新二维标记数组
	 * @param moveTetriesType 控制器的命令类型
	 */
	public void moveTetries(String moveTetriesType){
		//TetrisShapeType temptype=TetrisShapeType.values()[runningTetris.tst];//得到当前运动方块的种类
		updataTetris(runningTetris, UpdataOrderType.UOT_CLEAR);
		switch (moveTetriesType) {
		case  "MTT_LEFT":  //左移
			if(updataTetris(runningTetris, UpdataOrderType.UOT_LCHECK)){
				//updataTetris(runningTetris, UpdataOrderType.UOT_CLEAR);
				runningTetris.xp--;
				//updataTetris(runningTetris, UpdataOrderType.UOT_FULL);
			}
			break;
		case "MTT_RIGHT":
			if(updataTetris(runningTetris, UpdataOrderType.UOT_RCHECK)){
				//updataTetris(runningTetris, UpdataOrderType.UOT_CLEAR);
				runningTetris.xp++;
				//updataTetris(runningTetris, UpdataOrderType.UOT_FULL);
			}
			break;
		case "MTT_DOWN":
			System.out.println("MTT_DOWN");
			if(updataTetris(runningTetris, UpdataOrderType.UOT_DOWNCHECK)){
			//	updataTetris(runningTetris, UpdataOrderType.UOT_CLEAR);
				runningTetris.yp++;
			//	updataTetris(runningTetris, UpdataOrderType.UOT_FULL);
			}else{
				updataTetris(runningTetris, UpdataOrderType.UOT_FULL);
				cheackFullRows(runningTetris.yp,runningTetris.crow);
			}
			break;
		case "MTT_CW_ROTATE":
			runningTetris.swapCrowAndCcolumn();
			if(updataTetris(runningTetris, UpdataOrderType.UOT_LCHECK) 
					&& updataTetris(runningTetris, UpdataOrderType.UOT_RCHECK)){
				//updataTetris(runningTetris, UpdataOrderType.UOT_CLEAR);
			//	updataTetris(runningTetris, UpdataOrderType.UOT_FULL);
			}else {
				runningTetris.swapCrowAndCcolumn();
			}
			break;
		}
		updataTetris(runningTetris, UpdataOrderType.UOT_FULL);
	}
	
	/**
	 * 更新
	 * @param nrunningTeris 正在运动的方块，final表示该函数不能对其域进行修改
	 * @param utotype 操作类型
	 * @return 标记控制器的命令是否有效，false表示无效，标记数组没有改变
	 * true表示有效，标记数组已经更新
	 */
	private boolean updataTetris(final RunningTetris nrunningTeris,UpdataOrderType utotype) {
		if(nrunningTeris==null)
			return false;
		boolean result=true;
		boolean tempResult=true;
		int x=nrunningTeris.xp;int y=nrunningTeris.yp;
		int row=nrunningTeris.crow;int column=nrunningTeris.ccolumn;
		switch (TetrisShapeType.values()[nrunningTeris.tst]) {
		case TST_ISHAPE:
			result=divide(row, column, x, y, utotype);
			break;
		case TST_LSHAPE:
			switch (nrunningTeris.degress) {
			case 0:
				result=divide(row-1, 1, x,y, utotype);
				tempResult=divide(1, column, x, y+row-1, utotype);
				break;
			case 90:
				result=divide(1, column, x, y, utotype);
				tempResult=divide(row-1, 1, x, y+1, utotype);
				break;
			case 180:
				result=divide(1, column, x, y, utotype);
				tempResult=divide(row-1, 1, x+1, y+1, utotype);
				break;
			case 270:
				result=divide(1, 1, x+column-1, y, utotype);
				tempResult=divide(1, column, x, y+1, utotype);
				break;
			}
			break;
		case TST_ZSHAPE:
			if(nrunningTeris.degress%180==0){
				result=divide(1, column-1, x, y, utotype);
				tempResult=divide(1, column-1, x+1, y+1, utotype);
			}else{
				result=divide(row-1, 1, x, y+1, utotype);
				tempResult=divide(row-1, 1, x+1, y, utotype);
			}
			break;
		case TST_SQUARE:
			result=divide(row, column, x, y, utotype);
			break;
		}
		
		return result && tempResult ;
	}
	/**
	 * 分列分行更新
	 * @param ncrow  行数
	 * @param nccolumn 列数
	 * @param nx  当前x
	 * @param ny  当前y
	 * @param type 操作类型
	 * @return  
	 */
	private boolean divide(int ncrow,int nccolumn,int nx,int ny,UpdataOrderType type){
			switch (type) {
			case UOT_LCHECK:
				if(nx<=0) return false;
				for(int j=ny;j<ny+ncrow;j++){
					if(bvSquareCheck[nx-1][j])
						return false;
				}
				break;
			case UOT_RCHECK:
				if(nx+nccolumn>=rowCountSquare) return false;
				for(int j=ny;j<ny+ncrow;j++){
					if(bvSquareCheck[nx+nccolumn][j])
						return false;
				}
				break;		
			case UOT_DOWNCHECK:
				System.out.println("UOT_DOWNCHECK");
				if(ny+ncrow>=columnCountSquare){
					runningTetris.isShouldStop=true;
					
					return false;
				}
				for(int i=nx;i<nx+nccolumn;i++){
					if(bvSquareCheck[i][ny+ncrow])
						runningTetris.isShouldStop=true;
						return false;
				}
				break;
			case UOT_CLEAR:
			case UOT_FULL:
				for(int i=0;i<nccolumn;i++){
					for(int j=0;j<ncrow;j++){
						if(type==UpdataOrderType.UOT_CLEAR)
							bvSquareCheck[nx+i][ny+j]=false;
						else if(type==UpdataOrderType.UOT_FULL)
							bvSquareCheck[nx+i][ny+j]=true;
					}
				}
				break;
			default:
				break;
			}	
		return true;
	}
	private void cheackFullRows(int currenty,int crow){
		boolean btemp=true;
		for(int j=currenty;j<currenty+crow;j++){
			btemp=true;
			for(int i=0;i<columnCountSquare;i++){
				if(!bvSquareCheck[i][j]){
					btemp=false;
					break;
				}
			}
			if(btemp){
				for(int col=0;col<columnCountSquare;col++)
					for(int row=j;row>=0;row--)
						bvSquareCheck[col][row]=bvSquareCheck[col][row-1];
			}
		}
	}
	/**
	 * 声明一个内部类，表示当前运动的俄罗斯方块
	 * 
	*/
	class RunningTetris{
		int tst; //方块类型
		int  xp;  //方块x位置，这里不是实际位置，只是指数组下标位置
		int yp;  //方块y位置
		int crow;//方块所在最小包含矩形的行数
		int ccolumn;//方块所在最小包含矩形的行数
		int degress;//方块当前角度，顺时针
		boolean isShouldStop;//标记是否停止运动
		
		public void swapCrowAndCcolumn(){
			int temp=crow;
			crow=ccolumn;
			ccolumn=temp;
		
		}
		public void updataData(){
			degress=((int)(Math.random()*4))*90;
			tst=(int)(Math.random()*countTetris);
			isShouldStop=false;
			if(degress%180==0){
				crow=DefaultRowColOfTST[tst][0];
				ccolumn=DefaultRowColOfTST[tst][1];
			}else{
				crow=DefaultRowColOfTST[tst][1];
				ccolumn=DefaultRowColOfTST[tst][0];
			}
			xp=Math.max(0, (int)(Math.random()*columnCountSquare)
					-ccolumn);	
			yp=0;
		}
		
	}
}
