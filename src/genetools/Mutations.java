package genetools;

import genetools.exceptions.InvalidBaseException;

public enum Mutations
{
	A,G,C,T,R,Y,K,M,N,X,S,W,INS,DEL;
	
	public static Mutations getBase(String mutation) throws InvalidBaseException
	{
		if(mutation.equals("A")||mutation.equals("a"))
			return A;
		if(mutation.equals("C")||mutation.equals("c"))
			return C;
		if(mutation.equals("G")||mutation.equals("g"))
			return G;
		if(mutation.equals("T")||mutation.equals("t"))
			return T;
		
		
		if(mutation.equals("R")||mutation.equals("r"))
			return R;
		if(mutation.equals("Y")||mutation.equals("y"))
			return Y;
		if(mutation.equals("K")||mutation.equals("k"))
			return K;
		if(mutation.equals("M")||mutation.equals("m"))
			return M;
		if(mutation.equals("S")||mutation.equals("s"))
			return S;
		if(mutation.equals("W")||mutation.equals("w"))
			return W;
		if(mutation.equals("N")||mutation.equals("n"))
			return N;
		if(mutation.equals("X")||mutation.equals("x"))
			return X;
		
		else
			throw new InvalidBaseException(mutation);
	}
}