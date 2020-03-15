grammar ParityGame;

game: ('parity' id=identifier ';')? nodes=node+;
successors: identifier (',' identifier)*;
node: id=identifier priority=identifier owner=player successors (name=STRING_LITERAL)? ';';
player: p=(PLAYER0|PLAYER1);

identifier: NUMBER | PLAYER0 | PLAYER1;

PLAYER0: '0';
PLAYER1: '1';
NUMBER: [0-9]+;
STRING_LITERAL : '"' (~('"' | '\r' | '\n'))* '"';

// Whitespace
WS: [ \n\t\r]+ -> skip;