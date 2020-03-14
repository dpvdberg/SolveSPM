grammar ParityGame;
game: ('parity' NUMBER)? node+;
node: id=NUMBER priority=NUMBER owner=(PLAYER0|PLAYER1) successors (name=STRING_LITERAL)? ';';
successors: NUMBER (',' NUMBER)*;

NUMBER : [0-9]+;
PLAYER0 : '0';
PLAYER1 : '1';
STRING_LITERAL : '"' (~('"' | '\r' | '\n'))* '"';

// Whitespace
WS: [ \n\t\r]+ -> skip;