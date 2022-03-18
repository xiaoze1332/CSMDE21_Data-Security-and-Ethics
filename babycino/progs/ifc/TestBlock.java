class TestBlock {
    public static void main(String[] a){
	System.out.println(0);
    }
}

class Flow {

    public int l() {
        int high;
        int low;

        {
            if (high < 10) {
                high = high + 1;
            }
            else {
            
            }

            if (low < 10) {
                low = low + 1;
                low = low * 2;
            }
            else {
                low = low + 2;
                high = high * 2;
            }
        }

	return 0;
    }

}

