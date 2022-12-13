# Wordy
Kaiyang Yao, Aidan Alls

Wordy is a toy programming language that supports simple arithmetic expressions, conditions, and loops.

--FUNCTION DECLARATION-----------------------------------------------------------------------------------------------------------

We also added functions. The syntax for declaring a function is:

"
Declare function <functionName> that takes parameters (<0 or more parameters>):
    <blockNode for the function Body>
    return <some value or nothing>
End of function.
"
NOTE: The function doesn't require any parameters, but does require the parentheses.

--CALLING FUNCTIONS---------------------------------------------------------------------------------------------------------------

To call a function, use this syntax:

"
the result of calling <functionName> with (<parameters>).
"

Function calls are expressions, so they go anywhere an expression would go. This means they can be assinged to variables, or be passed as arguments to other functions.

example:
"
set x to the result of calling sumNumbers with (the result of calling doubleNumber with (2), 5)
"

--RETURN VALUE---------------------------------------------------------------------------------------------------------------------

The "return" keyword does not break out of the function, so any code after a return statement will still execute. 

example:
"
Declare function test that takes parameters ( x ):
    if x is greater than 10 then set a to 5 else set a to 15.
    Return a.
End of function.
"
This function works as expected, because return a is the last line of code.

"
Declare function test that takes parameters ( x ):
    Set a to 5.
    if x is greater than 10 then return a else set a to 15.
    Set a to a plus 1.
    Return a.
End of function.
"
This function, however will never return 5 even if x is greater than 10, because it will continue on to "set a to a plus 1".
So while using return in multiple places doesn't necisarily break functions, it's best to set values in if statements and return
some value at the end of the function body.


--NO RECURSION---------------------------------------------------------------------------------------------------------------------

While function calls can be nested, there is no lexical capture, so functions cannot reference themselves within their own body.
This means that recursion is impossible.
