import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Array2D<D> implements Iterable<D> {
	/*
	 * Example Coordinates representation: 
	 * (0,0) (1,0) (2,0) 
	 * (0,1) (1,1) (2,1)
	 * (0,2) (1,2) (2,2)
	 */

	private int rows;
	private int columns;
	private D[] data;

	/**
	 * Constructs a new generic Array2D structure with a specified columns and
	 * rows
	 *
	 * @param columns the amount of columns stored in the Array2D
	 * @param rows the amount of rows stored in the Array2D
	 */
	@SuppressWarnings("unchecked")
	public Array2D(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.data = (D[]) new Object[columns * rows];
	}

	/**
	 * Constructs a new generic Array2D structure with a specified columns,
	 * rows, and data
	 *
	 * @param columns the amount of columns stored in the Array2D
	 * @param rows the amount of rows stored in the Array2D
	 * @param data the data to be stored in the Array2D
	 */
	public Array2D(int rows, int columns, D[] data) {
		if (columns * rows != data.length) {
			throw new IllegalArgumentException("Data dimensions do not match.");
		}
		this.columns = columns;
		this.rows = rows;
		this.data = data;
	}

	/**
	 * Returns the data stored at the specified row and column of the Array2D
	 *
	 * @param x the column data is wanted from
	 * @param y the row data is wanted from
	 * @return the data stored in this Array2D at the specified row and column
	 */
	public D get(int x, int y) {
		if (y > rows - 1 || x > columns - 1) {
			throw new ArrayIndexOutOfBoundsException("Dimensions of this Array2D: " + rows + " x " + columns);
		}
		return data[y * columns + x];
	}

	/**
	 * Sets the data stored at the specified row and column of the Array2d to
	 * the specified data
	 *
	 * @param x the column data is overwritten at
	 * @param y the row data is overwritten at
	 * @param data  the data to write to the specified coordinates
	 */
	public void set(int x, int y, D data) {
		if (y > rows - 1 || x > columns - 1) {
			throw new ArrayIndexOutOfBoundsException("Dimensions of this Array2D: " + rows + " x " + columns);
		}
		this.data[y * columns + x] = data;
	}

	/**
	 * @return the number of rows in this Array2D
	 */
	public int getNumRows() {
		return this.rows;
	}

	/**
	 * @return the number of columns in this Array2D
	 */
	public int getNumColumns() {
		return this.columns;
	}

	/**
	 * Returns the CoordinatePair neighbors of the specified element in order of 
	 * North, East, South, West If the specified element has no neighbor to that 
	 * side, the neighbor in that position will be null
	 *
	 * @param x the row of the element neighbors are wanted from
	 * @param y the column of the element neighbors are wanted from
	 * @return the neighbors of element (row, column) in NESW order
	 */
	public List<CoordinatePair> getOrderedNeighbors(int x, int y) {
		ArrayList<CoordinatePair> neighbors = new ArrayList<CoordinatePair>();

		// Add North Neighbor
		if (y > 0) {
			neighbors.add(new CoordinatePair(x, y - 1));
		} else {
			neighbors.add(null);
		}

		// Add East Neighbor
		if (x < columns - 1) {
			neighbors.add(new CoordinatePair(x + 1, y));
		} else {
			neighbors.add(null);
		}

		// Add South Neighbor
		if (y < rows - 1) {
			neighbors.add(new CoordinatePair(x, y + 1));
		} else {
			neighbors.add(null);
		}

		// Add West Neighbor
		if (x > 0) {
			neighbors.add(new CoordinatePair(x - 1, y));
		} else {
			neighbors.add(null);
		}

		return neighbors;
	}

	@Override
	public Iterator<D> iterator() {
		return new Array2DIterator();
	}

	private class Array2DIterator implements Iterator<D> {
		private int i;

		public Array2DIterator() {
			i = 0;
		}

		@Override
		public D next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			} else {
				i++;
				return data[i - 1];
			}
		}

		@Override
		public boolean hasNext() {
			return i < data.length;
		}
	}

	@Override
	public String toString() {
		String returnString = "";
		int maxElementLength = 0;

		// Save the longest elements length so we can print neatly
		for (D element : data) {
			if (element.toString().length() > maxElementLength) {
				maxElementLength = element.toString().length();
			}
		}

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				returnString = returnString + data[r * columns + c];
				if (c == columns - 1) {
					returnString = returnString + "\n";
				} else {
					int difference = maxElementLength - data[r * columns + c].toString().length();
					for (int i = 0; i < difference + 1; i++) {
						returnString = returnString + " ";
					}
				}
			}
		}
		return returnString;
	}
}
