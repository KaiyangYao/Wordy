# Wordy
Kaiyang Yao, Aidan Alls

Wordy is a toy programming language that supports simple arithmetic expressions, conditions, and loops.

We also added functions. The syntax for declaring a function is:

"
Declare function <functionName> that takes parameters (<0 or more parameters>):
    <blockNode for the function Body>
    return <some value or nothing>
End of function.
"

To call a function, use this syntax:

"
the result of calling <functionName> with (<parameters>).
"

function calls can be nested, passing the result of one function as a parameter to another

example:
"
set x to the result of calling sumNumbers with (the result of calling doubleNumber with (2), 5)
"
could sum 4 and 5, after doubling 2 to get 4.