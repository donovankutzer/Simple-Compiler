MUST ADD input.esp IN BUILD CONFIGURATION --> ARGUMENTS TO RUN

--------------------------------
UndefinedVariableException Class
--------------------------------

    Creates a custom Exception that is thrown when calling a Variable object
      that has not been initialized yet


--------------
Variable Class
--------------

    Creates an object named Variable
    Contains integer value and boolean initialized
    If the variable hasn't been initialized and gets called, 
      will throw UndefinedVariableException()

    setValue(int v) 
	Sets the value of the Variable and initialized = true

    getValue()
	Returns value if it's been initialized
	Else will throw UndefinedVariableException
    isInitialized()
	Only used in getValue for sake of checking if it's been initialized

--------------------
Variable_Table Class
--------------------

    Creates an object named Variable_Table
    Contains an array of Variable Objects, which then uses for loops to create
      Variable objects from array locations ['a'] to ['z'] and ['A'] to ['Z']

    setVar(char var, int num)
	Initializes the Variable at array location [var] with the value num

    exists(char var)
	Returns bool value if Varable at array location [var] has been
	  initialized

    getValue(char var)
	Calls exists method.
	If exists, returns value at array location [var]

--------------
Compiler Class
--------------

    main method:
	Creates Variable_Table to access among all other methods
	File made which accesses the file at args[0]
	Scanner made to access the values in the File
	Scanner userIn also made for read method. Is within the main method 
	  in the cases where read is called more than once. Scanner only made once
	While the file has more tokens to take in, it will loop.
	If the initial token equals "read", "print", or is a variable, it will 	  	  execute a method, else it will throw an Exception
	At the end of the main method, it will finally close all scanners, and 	  exit

    read(Variable_Table table, Scanner userIn, String var):
	Takes the initial token, and if its not 1 character long, throws an
	  Exception
	Converts the String to a char, request an integer from the user,
	  and set the Variable in the array at [var] to the integer

    varEdit(Variable_Table table, String var, String command):
	Coverts var to char
	If the first char in command string is not '=', throws Exception
	Will then remove "= " from the String, and push that to inToPostFix method
	  which gets pushed to evalPostFix, which then gets assigned to Variable
	  at location [var]

    inToPostFix(Variable_Table table, String command):
	Creates empty string pfix, boolean isValid, and character stack symStack
	Then converts command to a Scanner to more easily evaluate the tokens
	Will then loop until the scanner has no more tokens
	  If token is a letter, will grab value of the variable and add to pfix, 	    	    unless it is not initialized or length() > 1 in which case will throw 		    Exception
	  If token is digit, will take the whole digit and add to pfix
	  If token is "(", will push to stack.
	  If token is ")", will pop all symbols in stack until peek() = "(", and
 	    will pop one more time
	  If token is any operator, will compare precedence to precedence in 
	    peek(), and will pop() if less than, and will push() once = or >
	  All of the if statements will change isValid to true. If none have
	    occured, isValid will be false and will throw Exception
	  Once there are no more tokens left, the stack will empty into pfix, and 
	    will return pfix

    evalPostFix(String postfix, Variable_Table table):
	Creates stack of integers intStack
	Evaluates every character of postfix string
	  If blankspace, will skip the iteration
	  If digit, will push to stack
	  If operator, pops top 2 in stack, and will perform the operator on them
	    and return back to stack
	Pops final integer in the stack as the result

    precedence(char symbol):
	If symbol is '+' or '-', returns 1
	If symbol is '*', '/', or '%', returns 2
	Default case returns -1. Will never occur because intoPostFix() removes
 	  all illegal characters

    print(Variable_Table table, String input):
	Evaluates equation in line, and prints result