# Wordy
Kaiyang Yao, Aidan Alls

Wordy is a toy programming language that supports simple arithmetic expressions, conditions, and loops. It is a Paul Cantrell invention specifically for Macalester’s Programming Languages course. More descriptions can be found [here](/docs/wordy.md).

![Wordy Example](/src/wordy/demo/Wordy_IDE_Example.png)
Besides the features described above, we brought functions to Wordy.

Three new AST nodes and a set of new parser rules are added to Wordy to support functions. The new AST nodes include: FunctionNode, FunctionCallNode, and FunctionReturnNode. 

## Declaring a Function
Function declaration is implemented in FunctionNode. We can declare a new function using the following syntax:

```
Declare function <functionName> that takes parameters (<0 or more parameters>):
    <BlockNode for the function body>
    return <ExpressionNode | "nothing">.
End of function.
```

NOTE: The function doesn't require any parameters, but does require the parentheses.

## Calling a Function
To call a function declared before, we can use this syntax:

```
the result of calling <functionName> with (<0 or more parameters>).
```

Function calls are expressions, so they go anywhere an expression would go. This means they can be assigned to variables, or be passed as arguments to other functions.

Example:
```
Set x to the result of calling plusOne with (2).
Set y to the result of calling sumNumbers with (the result of calling plueOne with (1), 5)
```

## Function Return
Similar to other languages, explicitly stating `return` will break the function and return the evaluated statement after the return keyword.

Example:
```
Declare function compare that takes parameters ( x, y ):
    If x is greater than y then return 1 else return 0.
End of function.
```

This function will return 1 if `x > y` and 0 otherwise.

## Function Recursion
While function calls can be nested, the lexical capture is not implemented. This means there is no way to access local scope in a function and functions cannot reference themselves within their own body.
So recursion is not supported in this version of Wordy.
