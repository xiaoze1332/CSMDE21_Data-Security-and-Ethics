package babycino;

// A security level.
public enum Level {
    LOW,
    HIGH;

    // Least upper bound of two security levels.
    public static Level lub(Level l1, Level l2) {
        // TODO: Task 1.1
		
		//	Truth Table
		// .........................
		// : l1    : l2    : l3    :
		// .........................
		// : HIGH  : HIGH  : HIGH  :
		// : HIGH  : LOW   : HIGH  :
		// : LOW   : HIGH  : HIGH  :
		// : LOW   : LOW   : LOW   :
		// .........................
		
		//	Define l3
		Level l3;
		
		
		if((l1 == l2) && (l1 == Level.LOW)){
			l3 = Level.LOW;
		}else{
			l3 = Level.HIGH;
		}
		
        return l3;
    }

    // Greatest lower bound of two security levels.
    public static Level glb(Level l1, Level l2) {
        // TODO: Task 1.2
		
		
		//	Truth Table
		// .........................
		// : l1    : l2    : l3    :
		// .........................
		// : HIGH  : HIGH  : HIGH  :
		// : HIGH  : LOW   : LOW   :
		// : LOW   : HIGH  : LOW   :
		// : LOW   : LOW   : LOW   :
		// .........................

		//	Define l3
		Level l3;
		
		if((l1 == l2) && (l1 == Level.HIGH)){
			l3 = Level.HIGH;
		}else{
			l3 = Level.LOW;
		}
		
        return l3;
    }

    // Less than or equal on security levels.
    public static boolean le(Level l1, Level l2) {
        return ((l1 == l2) || (l1 == Level.LOW));
    }

}

