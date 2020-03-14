grammar ParityGame;
game: ('parity' id=NUMBER)? nodes=node+;
node: id=NUMBER priority=NUMBER owner=player successors (name=STRING_LITERAL)? ';';
player: p=(PLAYER0|PLAYER1);
successors: NUMBER (',' NUMBER)*;

NUMBER : [0-9]+;
PLAYER0 : '0';
PLAYER1 : '1';
STRING_LITERAL : '"' (~('"' | '\r' | '\n'))* '"';

// Whitespace
WS: [ \n\t\r]+ -> skip;