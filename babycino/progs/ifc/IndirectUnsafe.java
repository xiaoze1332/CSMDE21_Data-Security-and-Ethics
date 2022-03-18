class IndirectUnsafe {
    public static void main(String[] a){
	System.out.println(new Flow().l(false));
    }
}

class Flow {

    public int l(boolean h){
	int l0;
	l0 = 0;
	if (h) {
	    l0 = 1;
        }
        else {
        
        }
	return l0;
    }

}

