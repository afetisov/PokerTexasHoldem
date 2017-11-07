package client.game.misc;
import java.util.HashMap;
import java.util.Map;

public class Cards {
	
	public static final String BACK = "back"; 
	
	private static final Map<String, Matrix> matrix = new HashMap<String, Matrix>();

	static{
		matrix.put("As", new Matrix(0,0));
		matrix.put("Ks", new Matrix(1,0));
		matrix.put("Qs", new Matrix(2,0));
		matrix.put("Js", new Matrix(3,0));
		matrix.put("Ts", new Matrix(4,0));
		matrix.put("9s", new Matrix(5,0));
		matrix.put("8s", new Matrix(6,0));
		matrix.put("7s", new Matrix(7,0));
		matrix.put("6s", new Matrix(8,0));
		matrix.put("5s", new Matrix(9,0));
		matrix.put("4s", new Matrix(10,0));
		matrix.put("3s", new Matrix(11,0));
		matrix.put("2s", new Matrix(12,0));
		
		matrix.put("Ah", new Matrix(0,1));
		matrix.put("Kh", new Matrix(1,1));
		matrix.put("Qh", new Matrix(2,1));
		matrix.put("Jh", new Matrix(3,1));
		matrix.put("Th", new Matrix(4,1));
		matrix.put("9h", new Matrix(5,1));
		matrix.put("8h", new Matrix(6,1));
		matrix.put("7h", new Matrix(7,1));
		matrix.put("6h", new Matrix(8,1));
		matrix.put("5h", new Matrix(9,1));
		matrix.put("4h", new Matrix(10,1));
		matrix.put("3h", new Matrix(11,1));
		matrix.put("2h", new Matrix(12,1));
		
		matrix.put("Ad", new Matrix(0,2));
		matrix.put("Kd", new Matrix(1,2));
		matrix.put("Qd", new Matrix(2,2));
		matrix.put("Jd", new Matrix(3,2));
		matrix.put("Td", new Matrix(4,2));
		matrix.put("9d", new Matrix(5,2));
		matrix.put("8d", new Matrix(6,2));
		matrix.put("7d", new Matrix(7,2));
		matrix.put("6d", new Matrix(8,2));
		matrix.put("5d", new Matrix(9,2));
		matrix.put("4d", new Matrix(10,2));
		matrix.put("3d", new Matrix(11,2));
		matrix.put("2d", new Matrix(12,2));
		
		matrix.put("Ac", new Matrix(0,3));
		matrix.put("Kc", new Matrix(1,3));
		matrix.put("Qc", new Matrix(2,3));
		matrix.put("Jc", new Matrix(3,3));
		matrix.put("Tc", new Matrix(4,3));
		matrix.put("9c", new Matrix(5,3));
		matrix.put("8c", new Matrix(6,3));
		matrix.put("7c", new Matrix(7,3));
		matrix.put("6c", new Matrix(8,3));
		matrix.put("5c", new Matrix(9,3));
		matrix.put("4c", new Matrix(10,3));
		matrix.put("3c", new Matrix(11,3));
		matrix.put("2c", new Matrix(12,3));
		
		matrix.put(BACK, new Matrix(13,3));
	}
	
	public static int getOffsetX(String card, int width){
		Matrix m = matrix.get(card);
		return m.x * width;
	}
	
	public static int getOffsetY(String card, int height){
		Matrix m = matrix.get(card);
		return m.y * height ;
	}
}
