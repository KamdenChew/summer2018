
public class MainTest {

	public static void main(String[] args) {
		Integer[] data = {1,2,3,4,5,6,7,8,9,10,11,122235};
		Array2D<Integer> arr = new Array2D<Integer>(2,6, data);
		System.out.println(arr);
		int x = 5;
		int y = 1;
		System.out.println("Data at: (" + x + ", " + y + ") = " + arr.get(x, y));
		int newData = 99;
		System.out.println("Setting (" + x + ", " + y + ") to " + newData);
		arr.set(x, y, newData);
		System.out.println(arr);
		System.out.println("Data at: (" + x + ", " + y + ") = " + arr.get(x, y));
	}
}
