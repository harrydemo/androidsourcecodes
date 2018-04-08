package five.itcast.cn;

//棋子类
public class Point {
	// 这了性能，设成公有
	public int x;
	public int y;
	

	public int getX() {
		return x;
	}

	public Point setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public Point setY(int y) {
		this.y = y;
		return this;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}


	@Override
	public int hashCode() {
		return x + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}