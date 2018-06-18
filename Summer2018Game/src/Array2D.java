/* Example Coordinates representation:
 * (0,0) (1,0) (2,0)
 * (0,1) (1,1) (2,1)
 * (0,2) (1,2) (2,2)
 */
public class Array2D<D> {
	int rows;
	int columns;
	D[] data;
	
	/**
	 * Constructs a new generic Array2D structure with a specified columns and rows
	 *
	 * @param  columns: the amount of columns stored in the Array2D
	 * @param  rows: the amount of rows stored in the Array2D
	 */
	@SuppressWarnings("unchecked")
	public Array2D(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.data  = (D[]) new Object[columns * rows];
	}
	
	/**
	 * Constructs a new generic Array2D structure with a specified columns, rows, and data
	 *
	 * @param  columns: the amount of columns stored in the Array2D
	 * @param  rows: the amount of rows stored in the Array2D
	 * @param data: the data to be stored in the Array2D
	 */
	public Array2D(int rows, int columns, D[] data) {
		if(columns * rows != data.length) {
			throw new IllegalArgumentException("Data dimensions do not match.");
		}
		this.columns = columns;
		this.rows = rows;
		this.data = data;
	}
	
	/**
	 * Returns the data stored at the specified row and column of the Array2D
	 *
	 * @param  x: the column data is wanted from
	 * @param  y:the row data is wanted from
	 * @return the data stored in this Array2D at the specified row and column
	 */
	public D get(int x, int y) {
		if(y > rows - 1 || x > columns - 1) {
			throw new ArrayIndexOutOfBoundsException("Dimensions of this Array2D: " + rows + " x " + columns);
		}
		return data[y * columns + x];
	}
	
	@Override
	public String toString() {
		String returnString = "";
		int maxElementLength = 0;
		
		//Save the longest elements length so we can print neatly
		for(D element: data) {
			if(element.toString().length() > maxElementLength) {
				maxElementLength = element.toString().length();
			}
		}
			
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				returnString = returnString + data[r * columns + c];
				if(c == columns - 1) {
					returnString = returnString + "\n";
				} else {
					int difference = maxElementLength - data[r * columns + c].toString().length();
					for(int i = 0; i < difference + 1; i++) {
						returnString = returnString + " ";
					}	
				}
			}
		}
		return returnString;
	}
}
