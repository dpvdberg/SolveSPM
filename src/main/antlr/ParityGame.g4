grammar ParityGame;
game: ('parity' id=NUMBER ';')? nodes=node+;
node: id=NUMBER priority=NUMBER owner=(PLAYER0|PLAYER1) successors (name=STRING_LITERAL)? ';';
successors: NUMBER (',' NUMBER)*;

PLAYER0 : '0';
PLAYER1 : '1';
NUMBER : [0-9]+;
STRING_LITERAL : '"' (~('"' | '\r' | '\n'))* '"';

// Whitespace
WS: [ \n\t\r]+ -> skip;