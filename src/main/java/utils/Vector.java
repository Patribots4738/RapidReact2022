package utils;

public class Vector {

	public double x, y, z;
	private boolean _isPolar = false; // Do not change this unless you know what you are doing

	public Vector(double x, double y) {

		this(x, y, 0);

	}

	public Vector(double x, double y, double z) {

		this.x = x;
		this.y = y;
		this.z = z;

	}

	public Vector(double x, double y, boolean isPolar) {

		this(x, y, 0, isPolar);

	}

	public Vector(double x, double y, double z, boolean isPolar) {

		this(x, y, z);
		this._isPolar = isPolar;

	}

	public Vector(Vector vector) {

		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this._isPolar = vector._isPolar;

	}

	public boolean isPolar() {

		return _isPolar;

	}

	/**
	 * @return the Length of any given vector
	 */
	public static double Length(Vector v) {

		return Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));

	}

	/**
	 * Not Complete
	 * 
	 * @return Polar value of a in (r, theta, phi)
	 */
	public static Vector Cartesian2Polar(Vector a) {

		if (a.isPolar()) {

			System.err.println("Cannot convert Polar Vector to Polar Vector");

			return a;

		}

		double r = Vector.Length(a);
		double theta = Math.acos(a.z / r);
		double phi = Math.acos((a.x / Math.sqrt(a.x * a.x + a.y)) * (a.y < 0 ? -1 : 1));

		return new Vector(r, theta, phi, true);

	}

	/**
	 * @return Vector value of a from (r, theta, phi)
	 */
	public static Vector Polar2Cartesian(Vector a) {

		if (!a.isPolar()) {

			System.err.println("Cannot convert Cartesian Vector to Cartesian Vector");

			return a;

		}

		// Remember in a Polar Vector here
		// x => r
		// y => theta
		// z => phi

		double x = a.x * Math.sin(a.y) * Math.cos(a.z);
		double y = a.x * Math.sin(a.y) * Math.sin(a.z);
		double z = a.x * Math.cos(a.y);

		return new Vector(x, y, z);

	}

	/**
	 * @return Value of a + b
	 */
	public static Vector Add(Vector a, Vector b) {

		return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);

	}

	/**
	 * @return Value of a - b
	 */
	public static Vector Subtract(Vector a, Vector b) {

		return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);

	}

	/**
	 * You cannot multiply 2 Vectors together
	 * 
	 * @return (a.x * b, a.y * b, a.z * b)
	 */
	public static Vector Multiply(Vector a, double b) {

		return new Vector(a.x * b, a.y * b, a.z * b);

	}

	/**
	 * @return Dot product of a and b -> a · b
	 */
	public static double Dot(Vector a, Vector b) {

		return (a.x * b.x) + (a.y * b.y) + (a.z * b.z);

	}

	/**
	 * @return Cross product of a and b -> a x b
	 */
	public static Vector Cross(Vector a, Vector b) {

		return new Vector(a.y * b.z - a.z * b.y, a.z * b.z - a.z * b.z, a.x * b.y - a.y * b.x);

	}

	/**
	 * @return Distance between a and b
	 */
	public static double Distance(Vector a, Vector b) {

		return Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y) + (b.z - a.z) * (b.z - a.z));

	}

	public static double Angle(Vector a, Vector b) {

		double top = Vector.Dot(a, b);
		double bottom = Vector.Length(a) * Vector.Length(b);

		return top / bottom;
		
	}

}