public class RToken {
	private enum Kind {
		NUM, RAT, NEG, MUL, DIV, ADD, SUB, POW, BO1, BC1, HOL
	}

	private Kind[] kind;
	private Rational[] r;
	private int length = 0;

	private class Token {
		private char value;
		private Kind kind;

		private Token(char c) {
			switch(c) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '.':
					kind = Kind.NUM;
					value = c;
					break;
				case '+':
					kind = Kind.ADD;
					break;
				case '-':
					kind = Kind.SUB;
					break;
				case '*':
					kind = Kind.MUL;
					break;
				case '/':
					kind = Kind.DIV;
					break;
				case '(':
					kind = Kind.BO1;
					break;
				case ')':
					kind = Kind.BC1;
					break;
				case '^':
					kind = Kind.POW;
					break;
				default:
					throw new IllegalArgumentException();
			}
		}
	}
	public RToken(String s) {
		Token[] t = new Token[s.length()];
		for(int i = 0; i < s.length(); ++i) {
			t[i] = new Token(s.charAt(i));
		}
		int BO1 = 0, BC1 = 0;
		for(int i = 0; i < t.length; ++i) {
			Kind k = t[i].kind;
			switch(k) {
				case NUM:
					if(i - 1 >= 0 && t[i - 1].kind == Kind.SUB) {
						if((i - 2 >= 0 && t[i - 2].kind == Kind.BO1) || (i - 1 == 0)) {
							t[i - 1].kind = Kind.NEG;
						}
					}
					while(i + 1 < t.length && t[i + 1].kind == Kind.NUM) {
						++i;
					}
					break;
				case BO1:
					++BO1;
					if(i - 1 >= 0 && t[i - 1].kind == Kind.NUM) {
						++length;
					}
					break;
				case BC1:
					++BC1;
					if(i + 1 < t.length && t[i + 1].kind == Kind.NUM) {
						++length;
					}
					break;
				case MUL:
				case DIV:
				case ADD:
				case SUB:
				case POW:
					if(i + 1 >= t.length || (t[i + 1].kind != Kind.NUM && t[i + 1].kind != Kind.BO1)) {
						throw new IllegalArgumentException();
					}
					break;
			}
			++length;
		}
		if(BO1 != BC1) {
			throw new UnsupportedOperationException();
		}
		kind = new Kind[length];
		r = new Rational[length];
		StringBuffer sb = new StringBuffer();
		for(int i = 0, j = 0; j < t.length && i < length; ++i, ++j) {
			if(t[j].kind == Kind.BC1 && j + 1 < t.length && t[j + 1].kind == Kind.NUM) {
				kind[i] = Kind.BC1;
				kind[++i] = Kind.MUL;
			}
			else if(t[j].kind == Kind.NUM) {
				sb.delete(0, sb.length());
				while(true) {
					sb.append(t[j].value);
					if(j + 1 < t.length && t[j + 1].kind == Kind.NUM) {
						++j;
					}
					else {
						break;
					}
				}
				r[i] = new Rational(sb.toString());
				kind[i] = Kind.RAT;
				if(i - 1 >= 0 && kind[i - 1] == Kind.NEG) {
					kind[i - 1] = Kind.HOL;
					r[i].negate();
				}
				if(j + 1 < t.length && t[j + 1].kind == Kind.BO1) {
					kind[++i] = Kind.MUL;
				}
			}
			else {
				kind[i] = t[j].kind;
			}
		}
	}
	private final int pow(int current) {
		int next = 0;
		int previous = 0;
		boolean nextFound = false;
		boolean previousFound = false;
		for(int i = current + 1; i < length; ++i) {
			if(kind[i] == Kind.RAT) {
				next = i;
				nextFound = true;
				break;
			}
		}
		for(int i = current - 1; i >= 0; --i) {
			if(kind[i] == Kind.RAT) {
				previous = i;
				previousFound = true;
				break;
			}
		}
		if(nextFound && previousFound) {
			r[previous].pow(r[next]);
			kind[current] = Kind.HOL;
			kind[next] = Kind.HOL;
			return next;
		}
		throw new ArithmeticException();
	}
	private final int mul(int current) {
		int next = 0;
		int previous = 0;
		boolean nextFound = false;
		boolean previousFound = false;
		for(int i = current + 1; i < length; ++i) {
			if(kind[i] == Kind.RAT) {
				next = i;
				nextFound = true;
				break;
			}
		}
		for(int i = current - 1; i >= 0; --i) {
			if(kind[i] == Kind.RAT) {
				previous = i;
				previousFound = true;
				break;
			}
		}
		if(nextFound && previousFound) {
			r[previous].multiply(r[next]);
			kind[current] = Kind.HOL;
			kind[next] = Kind.HOL;
			return next;
		}
		throw new ArithmeticException();
	}
	private final int div(int current) {
		int next = 0;
		int previous = 0;
		boolean nextFound = false;
		boolean previousFound = false;
		for(int i = current + 1; i < length; ++i) {
			if(kind[i] == Kind.RAT) {
				next = i;
				nextFound = true;
				break;
			}
		}
		for(int i = current - 1; i >= 0; --i) {
			if(kind[i] == Kind.RAT) {
				previous = i;
				previousFound = true;
				break;
			}
		}
		if(nextFound && previousFound) {
			r[previous].divide(r[next]);
			kind[current] = Kind.HOL;
			kind[next] = Kind.HOL;
			return next;
		}
		throw new ArithmeticException();
	}
	private final int add(int current) {
		int next = 0;
		int previous = 0;
		boolean nextFound = false;
		boolean previousFound = false;
		for(int i = current + 1; i < length; ++i) {
			if(kind[i] == Kind.RAT) {
				next = i;
				nextFound = true;
				break;
			}
		}
		for(int i = current - 1; i >= 0; --i) {
			if(kind[i] == Kind.RAT) {
				previous = i;
				previousFound = true;
				break;
			}
		}
		if(nextFound && previousFound) {
			r[previous].add(r[next]);
			kind[current] = Kind.HOL;
			kind[next] = Kind.HOL;
			return next;
		}
		throw new ArithmeticException();
	}
	private final int sub(int current) {
		int next = 0;
		int previous = 0;
		boolean nextFound = false;
		boolean previousFound = false;
		for(int i = current + 1; i < length; ++i) {
			if(kind[i] == Kind.RAT) {
				next = i;
				nextFound = true;
				break;
			}
		}
		for(int i = current - 1; i >= 0; --i) {
			if(kind[i] == Kind.RAT) {
				previous = i;
				previousFound = true;
				break;
			}
		}
		if(nextFound && previousFound) {
			r[previous].subtract(r[next]);
			kind[current] = Kind.HOL;
			kind[next] = Kind.HOL;
			return next;
		}
		throw new ArithmeticException();
	}
	public final Rational eval() {
		int i = 0;
		while(i < length && kind[i] != Kind.BC1) {
			if(kind[i] == Kind.BO1) {
				kind[i] = Kind.HOL;
				eval(++i);
			}
			else {
				++i;
			}
		}
		for(i = 0; i < length; ++i) {
			if(kind[i] == Kind.POW) i = pow(i);
			else if(kind[i] == Kind.MUL) i = mul(i);
			else if(kind[i] == Kind.DIV) i = div(i);
		}
		for(i = 0; i < length; ++i) {
			if(kind[i] == Kind.ADD) i = add(i);
			else if(kind[i] == Kind.SUB) i = sub(i);
		}
		for(i = 0; true; ++i) {
			if(kind[i] == Kind.RAT) {
				return r[i];
			}
			else if(i + 1 == length) {
				throw new ArithmeticException();
			}
		}
	}
	private final void eval(final int start) {
		int i = start;
		while(kind[i] != Kind.BC1) {
			if(kind[i] == Kind.BO1) {
				kind[i] = Kind.HOL;
				eval(++i);
			}
			else {
				++i;
			}
		}
		i = start;
		for(; kind[i] != Kind.BC1; ++i) {
			if(kind[i] == Kind.POW) i = pow(i);
			else if(kind[i] == Kind.MUL) i = mul(i);
			else if(kind[i] == Kind.DIV) i = div(i);
		}
		i = start;
		for(; kind[i] != Kind.BC1; ++i) {
			if(kind[i] == Kind.ADD) i = add(i);
			else if(kind[i] == Kind.SUB) i = sub(i);
		}
		kind[i] = Kind.HOL;
	}
}
