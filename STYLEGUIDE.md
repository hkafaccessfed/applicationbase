# AAF source code format guide

Adopted from http://code.google.com/p/google-styleguide/

## Overview
Every AAF project should use the same style guide. It is much easier to understand a large codebase(s) when all the code in it is in a consistent style.

We're getting better at this over time. As the AAF team grows and more proactive code reviews are being undertaken this should become even more relevant and well thrashed out. If you find a violation fix it up and submit a pull request.

## Comments

Though a pain to write, comments are absolutely vital to keeping code readable. The following rules describe what you should comment and where. But remember: while comments are very important, the best code is self-documenting. Giving sensible names to types and variables is much better than using obscure names that you must then explain through comments.

When writing your comments, write for your audience: the next contributor who will need to understand your code. Be generous — the next one may be you!

Comment Style
  Use either the // or /* */ syntax, as long as you are consistent.

Punctuation, Spelling and Grammar
  Pay attention to punctuation, spelling, and grammar; it is easier to read well-written comments than badly written ones.

Class Comments
  Every class definition should have an accompanying comment that describes what it is for and how it should be used.

## Formatting
Line Length
  Each line of text in your code should be at most 80 characters long.

Non-ASCII Characters
  Non-ASCII characters should be rare, and must use UTF-8 formatting.

Spaces vs. Tabs
  Use only spaces, and indent 2 spaces at a time.

Conditionals
  Prefer no spaces inside parentheses. The else keyword belongs on a new line.
