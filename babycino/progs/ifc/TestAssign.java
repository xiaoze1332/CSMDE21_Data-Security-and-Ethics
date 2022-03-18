class TestAssign {
    public static void main(String[] a){
	System.out.println(0);
    }
}

class Flow {

    public int l1() {
        int high1;
        int low1;
        
        low1 = low1;
        
	return 0;
    }

    public int l2() {
        int high2;
        int low2;
        
        high2 = high2;
        
	return 0;
    }

    public int l3() {
        int high3;
        int low3;
        
        high3 = low3;
        
	return 0;
    }
    
    public int h() {
        int high4;
        int low4;

        low4 = high4;
        
        return 0;
    }

}

