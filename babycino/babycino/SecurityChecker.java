package babycino;

import org.antlr.v4.runtime.ParserRuleContext;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;

// Check that methods whose names begin with "h" or "l" are noninterfering.
// Treat variable names beginning with "h" as being High; everything else is Low.
public class SecurityChecker extends MiniJavaBaseListener {

    // Symbol Table for the program being type-checked.
    private SymbolTable sym;
    // Class currently being type-checked.
    private Class current;
    // Method currently being type-checked.
    private Method method;
    // Flag: Have any errors occurred so far?
    private boolean errors;
    // Flag: Are we checking security types in this method?
    private boolean secure;

    // Stack of unprocessed security types, corresponding to checked subexpressions.
    private Stack<Level> types;
    
    public SecurityChecker(SymbolTable sym) {
        this.sym = sym;
        this.method = null;
        this.errors = false;
        this.secure = false;
        this.types = new Stack<Level>();
    }

    // ------------------------------------------------------------------------
    // Track what the current class/method is.

    @Override
    public void enterMainClass(MiniJavaParser.MainClassContext ctx) {
        this.current = sym.get(ctx.identifier(0).getText());
        // Set a dummy method with no variables to avoid null-pointer errors later.
        this.method = new Method("main", null, this.current, null);
    }

    @Override
    public void exitMainClass(MiniJavaParser.MainClassContext ctx) {
        this.current = null;

        // It is a fatal error if somehow not all types on the stack are used.
        if (!this.types.isEmpty()) {
            System.err.println("Internal error: not all types consumed during type-checking.");
            System.exit(1);
        }
    }
    
    @Override
    public void enterClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        this.current = this.sym.get(ctx.identifier(0).getText());
    }
    
    @Override
    public void exitClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        this.current = null;
    }

    @Override
    public void enterMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        String name = ctx.identifier(0).getText();
        this.method = this.current.getOwnMethod(name);

        // Check noninterference in methods whose names begin with "h" or "l".
        if (name.startsWith("h") || name.startsWith("l")) {
            this.secure = true;
        }
    }

    @Override
    public void exitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        if (!this.secure) {
            return;
        }

        String name = ctx.identifier(0).getText();
        Level t = this.types.pop();
        // If the method name indicates the return value is Low, check this is true.
        if (name.startsWith("l")) {
            this.check(t == Level.LOW, ctx, "Method " + this.method.getQualifiedName() + " returns High value; Low expected");
        }

        // Remove types from the stack for each top-level statement.
        for (MiniJavaParser.StatementContext s : ctx.statement()) {
            this.types.pop();
        }

        // It is a fatal error if somehow not all types on the stack are used.
        if (!this.types.isEmpty()) {
            System.err.println("Internal error: not all security types consumed during type-checking.");
            System.exit(1);
        }

        // Clear the flag, as we are no longer checking for noninterference.
        this.secure = false;
    }

    // ------------------------------------------------------------------------
    // When leaving an expression or statement:
    // firstly, pop the types of any subexpressions off the stack and check them;
    // secondly, for expressions, push the type of the expression onto the stack.

    // Statements:
    @Override
    @SuppressWarnings("unchecked")
    public void exitStmtBlock(MiniJavaParser.StmtBlockContext ctx) {
        if (!this.secure) {
            return;
        }

        
        // TODO: Task 3.4
        //this.check(false, ctx, "Unimplemented");

        //  Store the Level into the List.
        List params =new ArrayList<>();
        Level tmp;
        // Calcute the number of Level.HIGH
        int m=0;
        // Calcute the number of Level.LOW
        int n=0;

        for (MiniJavaParser.StatementContext s : ctx.statement()) {
            tmp = this.types.pop();
            params.add(tmp);
            //System.out.println(tmp);
        }

        for(Object tmp1:params){
            if(tmp1==Level.HIGH){
                m++;
            }else{
                n++;
            }
        }

        //System.out.println("m = " + m);
        //System.out.println("n = " + n);
        if(n==0){
            this.types.push(Level.HIGH);
        }else{
            this.types.push(Level.LOW);
        }



    }

    @Override
    public void exitStmtIf(MiniJavaParser.StmtIfContext ctx) {
        if (!this.secure) {
            return;
        }

        // Truth Table
        // ............................................................................
        // : t1    : t2    : Statement need to be for whole IF  : t3    : compatible  :
        // ............................................................................
        // : HIGH  : HIGH  : HIGH                               : HIGH  : TRUE        :
        // : HIGH  : HIGH  : HIGH                               : LOW   : TRUE        :
        // : HIGH  : LOW   : LOW                                : LOW   : TRUE        :
        // : HIGH  : LOW   : LOW                                : HIGH  : FALSE       :
        // : LOW   : HIGH  : LOW                                : HIGH  : FALSE       :
        // : LOW   : HIGH  : LOW                                : LOW   : TRUE        :
        // : LOW   : LOW   : LOW                                : LOW   : TRUE        :
        // : LOW   : LOW   : LOW                                : HIGH  : FALSE       :
        // ............................................................................


        // TODO: Task 3.3
        //this.check(false, ctx, "Unimplemented");
		Level t1 = this.types.pop();
        Level t2 = this.types.pop();
        Level t3 = this.types.pop();


        //  Incompatible is checked using check() and the highest value of t1 and t2 is pushed onto the stack in the compatible case.
        if(t3==Level.HIGH && Level.glb(t1, t2)==Level.LOW){
            this.check(false, ctx, "Assignment of value of type Level." + Level.glb(t1, t2) + " to variable of incompatible type Level." + t3);
            this.types.push(t3);
        }else{
            this.types.push(Level.glb(t1, t2));
        }
    }

    @Override
    public void exitStmtWhile(MiniJavaParser.StmtWhileContext ctx) {
        if (!this.secure) {
            return;
        }

		// .............................
		// : lhs  : rhs  : compatible  :
		// .............................
		// : HIGH : HIGH : TRUE        :
		// : HIGH : LOW  : TRUE        :
		// : LOW  : LOW  : TRUE        :
		// : LOW  : HIGH : FALSE       :
		// .............................

        // TODO: Task 3.2
        //this.check(false, ctx, "Unimplemented");
		Level lhs = this.types.pop();
        Level rhs = this.types.pop();

        boolean tmp1;
		if(lhs != rhs	&&	lhs == Level.LOW){
			tmp1 = false;
		}else{
			tmp1 = true;
		}
		
        //  Use push() for compatibility, check() for incompatibility.
		if(tmp1){
            this.types.push(lhs);
        }else{
            this.check(tmp1, ctx, "Assignment of value of type Level." + rhs + " to variable of incompatible type Level." + lhs);
            this.types.push(rhs);
        }

        
    }

    @Override
    public void exitStmtPrint(MiniJavaParser.StmtPrintContext ctx) {
        this.check(!this.secure, ctx, "Unsupported statement inside secure method: println");
    }
    
    @Override
    public void exitStmtAssign(MiniJavaParser.StmtAssignContext ctx) {
        if (!this.secure) {
            return;
        }

        // TODO: Task 3.1
        //this.check(false, ctx, "Unimplemented");
		
		//	my code
		Level lhs = this.identifierLevel(ctx.identifier());
        Level rhs = this.types.pop();
		
		
		//	truth table of value "tmp1"
		// .............................
		// : lhs  : rhs  : compatible  :
		// .............................
		// : HIGH : HIGH : TRUE        :
		// : HIGH : LOW  : TRUE        :
		// : LOW  : LOW  : TRUE        :
		// : LOW  : HIGH : FALSE       :
		// .............................		
		

		/*	I use "tmp1" as judgment variable, 
			if the level is the same, tmp1 will be true
			if the level is different, tmp1 will be false
		*/
        

		boolean tmp1;
		if(lhs != rhs	&&	lhs == Level.LOW){
			tmp1 = false;
		}else{
			tmp1 = true;
		}
		
        //  Use push() for compatibility, check() for incompatibility.
		if(tmp1){
            this.types.push(rhs);
        }else{
            this.check(tmp1, ctx, "Assignment of value of type Level." + rhs + " to variable of incompatible type Level." + lhs);
            this.types.push(lhs);
        }

        

    }

    @Override
    public void exitStmtArrayAssign(MiniJavaParser.StmtArrayAssignContext ctx) {
        this.check(!this.secure, ctx, "Unsupported statement inside secure method: array assignment");
    }

    // Expressions:

    @Override
    public void exitExpConstTrue(MiniJavaParser.ExpConstTrueContext ctx) {
        if (!this.secure) {
            return;
        }

        // TODO: Task 2.1
        //this.check(false, ctx, "Unimplemented");
		
		//	my code
		this.types.push(Level.LOW);
    }

    @Override
    public void exitExpArrayLength(MiniJavaParser.ExpArrayLengthContext ctx) {
        this.check(!this.secure, ctx, "Unsupported expression inside secure method: array length");
    }

    @Override
    public void exitExpBinOp(MiniJavaParser.ExpBinOpContext ctx) {
        if (!this.secure) {
            return;
        }

        // TODO: Task 2.3
        //this.check(false, ctx, "Unimplemented");
		
		// my code | page 34
		Level rhs = this.types.pop();
        Level lhs = this.types.pop();
        String op = ctx.getChild(1).getText();
		
        switch (op) {
            case "&&":
            case "||":
            case "<":
                this.types.push(Level.lub(rhs,lhs));
                break;
            default:
                this.types.push(Level.lub(rhs,lhs));
                break;
        }
		
    }

    @Override
    public void exitExpConstInt(MiniJavaParser.ExpConstIntContext ctx) {
        if (!this.secure) {
            return;
        }

        this.types.push(Level.LOW);
    }

    @Override
    public void exitExpMethodCall(MiniJavaParser.ExpMethodCallContext ctx) {
        this.check(!this.secure, ctx, "Unsupported expression inside secure method: method call");
    }

    @Override
    public void exitExpConstFalse(MiniJavaParser.ExpConstFalseContext ctx) {
        if (!this.secure) {
            return;
        }

        // TODO: Task 2.1
        //this.check(false, ctx, "Unimplemented");
		
		//	my code
		this.types.push(Level.LOW);
    }

    @Override
    public void exitExpArrayIndex(MiniJavaParser.ExpArrayIndexContext ctx) {
        this.check(!this.secure, ctx, "Unsupported expression inside secure method: array index");
    }

    @Override
    public void exitExpNewObject(MiniJavaParser.ExpNewObjectContext ctx) {
        this.check(!this.secure, ctx, "Unsupported expression inside secure method: new object");
    }

    @Override
    public void exitExpNewArray(MiniJavaParser.ExpNewArrayContext ctx) {
        this.check(!this.secure, ctx, "Unsupported expression inside secure method: new array");
    }

    @Override
    public void exitExpNot(MiniJavaParser.ExpNotContext ctx) {
        if (!this.secure) {
            return;
        }

        // TODO: Task 2.4
        //this.check(false, ctx, "Unimplemented");
		
		//	truth table, use "t" as lst parameter and "2nd" as 2nd parameter for Level.glb()
		// ..................................
		// : t     : not t	: return value  :
		// ..................................
		// : HIGH  : HIGH	: HIGH          :
		// : LOW   : LOW    : LOW           :
		// ..................................
			
		
		//	my code
		Level t = this.types.pop();
		
		this.types.push(t);
    }

    @Override
    public void exitExpGroup(MiniJavaParser.ExpGroupContext ctx) {
        if (!this.secure) {
            return;
        }

        // TODO: Task 2.4
        //this.check(false, ctx, "Unimplemented");
		
		//is that do nothing because enter/leave group are the same?
    }

    @Override
    public void exitExpLocalVar(MiniJavaParser.ExpLocalVarContext ctx) {
        if (!this.secure) {
            return;
        }

        // TODO: Task 2.2
        //this.check(false, ctx, "Unimplemented");
		
		// my code
		this.types.push(this.identifierLevel(ctx.identifier()));
		
    }

    @Override
    public void exitExpThis(MiniJavaParser.ExpThisContext ctx) {
        this.check(!this.secure, ctx, "Unsupported expression inside secure method: this");
    }

    // ------------------------------------------------------------------------

    // Helper method to get level of variable.
    private Level identifierLevel(MiniJavaParser.IdentifierContext ctx) {
        String id = ctx.getText();
        if (id.startsWith("h")) {
            return Level.HIGH;
        }
        return Level.LOW;
    }

    // Error logging and recording:

    // Assert condition. Print error if false. Record occurrence of error.
    private void check(boolean condition, ParserRuleContext ctx, String error) {
        if (!condition) {
            System.err.println(error);
            System.err.println("Context: " + ctx.getText());
            this.errors = true;
        }
    }

    // Assert false. Print error. Record occurrence of error.
    private void error(ParserRuleContext ctx, String error) {
        System.err.println(error);
        System.err.println("Context: " + ctx.getText());
        this.errors = true;
    }

    // Throw an exception if an error previously occurred.
    public void die() throws CompilerException {
        if (this.errors) {
            throw new CompilerException();
        }
    }

}

