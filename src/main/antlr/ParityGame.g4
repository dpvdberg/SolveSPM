grammar ParityGame;
game:   ('parity' IDENTIFIER)?                          #trueFormula
    |   'false'                                         #falseFormula
    |   '(' left=formula op=(OR|AND) right=formula ')'  #orAndFormula
    |   '<' label=LABEL '>' formula                     #diamondFormula
    |   '[' label=LABEL ']' formula                     #boxFormula
    |   'mu' variable=VARIABLE '.' formula              #muFormula
    |   'nu' variable=VARIABLE '.' formula              #nuFormula
    |   variable=VARIABLE                               #varFormula
    ;
IDENTIFIER : [0-9]*;
LABEL : [a-z][a-z0-9_]*;
OR : '||';
AND : '&&';

// Whitespace and line comments
WS: [ \n\t\r]+ -> skip;
LINE_COMMENT: '%' ~[\r\n]* -> skip;