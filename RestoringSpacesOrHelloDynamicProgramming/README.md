## Task about restoring spaces in string

Suppose, we have string where all spaces and other delimiters were removed. 
Input data includes big dictionary to cover _all original words_ from string and even more. 
Assuming all chars are in lower-case, do not care about upepr case and ther delimiters.
The task sounds like restore spaces. In case if two and  more solutions can be revealed, shoose just any one.
In case if no solutions found just return original string.

The project contains implementation of this task. It is based on dynamic programming 
and runs algorithm with complexity ``` O(n^2) ``` where n is length of string
