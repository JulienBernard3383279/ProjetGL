lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}


// which packages should be imported?
@header {
    import java.util.*;
}


@members {
    String parseString(String s) {
        return s.substring(s.indexOf("\"")+1,s.lastIndexOf("\""));                
    }
}

// Deca lexer rules.

// Regles sur les caracteres de formatages
ESPACE: ' ' {skip();};
fragment TAB: '\t';
fragment FIN_DE_LIGNE: '\n';
RETOUR_CHARIOT: '\r'{skip();};
COMMENT: ('/*' .*? '*/' | '//' .*? ('\n' | EOF) ) {skip();};
EOL: (TAB | FIN_DE_LIGNE) {skip();};

// Regles sur les mots reserves
ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
FALSE: 'false';
IF: 'if';
INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTLNX: 'printlnx';
PRINTX: 'printx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
TRUE: 'true';
WHILE: 'while';

// Regles sur les int, float et identificateurs
fragment DIGIT: '0'..'9';
fragment POSITIVE_DIGIT: '1'..'9';
INT: '0' | POSITIVE_DIGIT DIGIT*;
fragment NUM: DIGIT+;
fragment SIGN: '+' | '-' | ;
fragment EXP: ('E' | 'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC | DEC EXP) ('F' | 'f' | );
fragment DIGITHEX: '0'..'9' | 'A'..'F' | 'a'..'f';
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | );
FLOAT: FLOATDEC | FLOATHEX;
fragment LETTER: 'a'..'z' | 'A'..'Z';
IDENT: (LETTER | '$' | '_') ( LETTER | DIGIT | '$' | '_' )*;

// Regles sur les symboles speciaux
LT: '<';
GT: '>';
EQUALS: '=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';
DOT: '.';
COMMA: ',';
OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';
EXCLAM: '!';
SEMI: ';' ;
EQEQ: '==';
NEQ: '!=';
GEQ: '>=';
LEQ: '<=';
AND: '&&';
OR: '||';

// Regles sur les chaines de caracteres
fragment STRING_CAR: ~('"' | '\\' | '\t' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

// Regles sur les includes
fragment FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;

INCLUDE: '#include' (' ')* '"' FILENAME '"' {
doInclude(getText());
skip(); 
};