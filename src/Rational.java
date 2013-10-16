import java.math.BigInteger;

public class Rational {
	// ----------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------
	public Rational() {
		num = new BigInteger("0");
		den = new BigInteger("1");
	}
	public Rational(int n) {
		num = new BigInteger(Integer.toString(n));
		den = new BigInteger("1");
	}
	public Rational(String s) {
		boolean itsAnInt = true;
		boolean itsNegative = false;
		int i = 0;
		if(s.charAt(i) == '-') {
			itsNegative = true;
			++i;
		}
		while(i < s.length()) {
			if(s.charAt(i++) == '.') itsAnInt = false;
		}
		if(itsAnInt) {
			num = new BigInteger(s);
			den = new BigInteger("1");
		}
		else {
			if(itsNegative) i = 1;
			else i = 0;
			num = new BigInteger("0");
			den = new BigInteger("1");
			int c;
			while((c = s.charAt(i++)) != '.') {
				c -= 48;
				BigInteger BigTemp = new BigInteger(Integer.toString(c));
				num = num.add(BigTemp);
				num = num.multiply(BigInteger.TEN);
			}
			while((c = s.charAt(i++)) == '.')
				;
			--i;
			while(i < s.length()) {
				c = s.charAt(i++);
				if(c < '0' || c > '9') {
					System.out.println("Rational(s) if continue");
					continue;
				}
				c -= 48;
				BigInteger BigTemp = new BigInteger(Integer.toString(c));
				num = num.add(BigTemp);
				num = num.multiply(BigInteger.TEN);
				den = den.multiply(BigInteger.TEN);
			}
			num = num.divide(BigInteger.TEN);
			simplify();
			if(itsNegative) num = num.negate();
		}
	}
	public Rational(int n, int d) {
		if(d > 0) {
			num = new BigInteger(Integer.toString(n));
			den = new BigInteger(Integer.toString(d));
			simplify();
		}
		else {
			throw new ArithmeticException("zero den");
		}
	}
	public Rational(Rational r) {
		num = r.num;
		den = r.den;
	}
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------
	// Methods
	// ----------------------------------------------------------------
	public void setNum(String s) {
		num = new BigInteger(s);
		simplify();
	}
	public void setDen(String s) {
		den = new BigInteger(s);
		if(den.compareTo(BigInteger.ZERO) < 0) {
			throw new ArithmeticException("denominator < 1");
		}
		simplify();
	}
	public void add(Rational r) {
		if(den.compareTo(r.den) == 0) {
			num = num.add(r.num);
		}
		else {
			num = num.multiply(r.den).add(den.multiply(r.num));
			den = den.multiply(r.den);
		}
		simplify();
	}
	public void subtract(Rational r) {
		if(den.compareTo(r.den) == 0) {
			num = num.subtract(r.num);
		}
		else {
			num = num.multiply(r.den).subtract(den.multiply(r.num));
			den = den.multiply(r.den);
		}
		simplify();
	}
	public void multiply(Rational r) {
		num = num.multiply(r.num);
		den = den.multiply(r.den);
		simplify();
	}
	public void divide(Rational r) {
		if(r.num.signum() == 0) {
			throw new ArithmeticException("zero den");
		}
		num = num.multiply(r.den);
		den = den.multiply(r.num);
		if(den.signum() == -1) {
			den = den.negate();
			num = num.negate();
		}
		simplify();
	}
	public void pow(Rational r) {
		if((r.den.compareTo(BigInteger.ONE)) != 0) {
			// no roots
			throw new ArithmeticException();
		}
		int i = r.num.intValue();
		if(i < 0) {
			BigInteger temp = num;
			num = den;
			den = temp;
			i *= -1;
		}
		if((num.compareTo(BigInteger.ONE)) != 0) {
			num = num.pow(i);
		}
		if((den.compareTo(BigInteger.ONE)) != 0) {
			den = den.pow(i);
		}
		simplify();
	}
	public void negate() {
		num = num.negate();
	}
	public void simplify() {
		// BigInteger i = new BigInteger(num.gcd(den).toString()); ???
		BigInteger i = num.gcd(den);
		if(!(i.equals(BigInteger.ONE))) {
			num = num.divide(i);
			den = den.divide(i);
		}
	}
	public void equalTo(Rational r) {
		num = r.num;
		den = r.den;
	}
	public String convToString() {
		if(den.compareTo(BigInteger.ONE) == 0) {
			return num.toString();
		}
		return "(" + num.toString() + "/" + den.toString() + ")";
	}
	public String convToStringF() {
		return Double.toString(num.doubleValue() / den.doubleValue());
	}

	// ----------------------------------------------------------------
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------
	// Fields
	// ----------------------------------------------------------------
	private BigInteger num;
	private BigInteger den;
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------
	// ----------------------------------------------------------------
}
