class TestLocalVarSafe {
    public static void main(String[] a){
	System.out.println(0);
    }
}

class Flow {

    public int l() {
        int low;
	return low;
    }

    public int h1() {
        int low;
	return low;
    }

    public int h2() {
        int high;
        return high;
    }

}

