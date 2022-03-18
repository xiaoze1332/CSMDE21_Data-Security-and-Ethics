class TestBinOpSafe {
    public static void main(String[] a){
	System.out.println(0);
    }
}

class Flow {

    public int l() {
	return 1+1;
    }

    public int h1() {
        int low;
        int high;
	return low*high;
    }

    public boolean h2() {
        boolean high1;
        boolean high2;
        return high1 && high2;
    }

}

