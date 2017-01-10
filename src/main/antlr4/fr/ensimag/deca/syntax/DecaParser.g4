parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
    import fr.ensimag.deca.tools.SymbolTable;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
    
    private SymbolTable tableSymboles=new SymbolTable();
    
    public SymbolTable getTable() {
        return tableSymboles;
    }
    
}

prog returns[AbstractProgram tree]
    : list_classes main EOF {
            assert($list_classes.tree != null);
            assert($main.tree != null);
            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type list_decl_var[$l,$type.tree] SEMI
    ;

list_decl_var[ListDeclVar l, AbstractIdentifier t]
    : dv1=decl_var[$t] {
        $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
        }
      )*
    ;

decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
@init   {
    boolean init=false; //modif
        }
    : i=ident {
        assert($i.tree != null); //modif
        }
      (EQUALS e=expr {
        assert($e.tree != null); //modif
        init=true;
        }
      )? {
            if (init) {
                $tree = new DeclVar(t,$i.tree,new Initialization($e.tree)); //modif
            } else {
                $tree = new DeclVar(t,$i.tree,new NoInitialization());
            }
            //setLocation($tree,$e.start);
        }
    ;

list_inst returns[ListInst tree]
@init {
    $tree = new ListInst(); //modif
}
    : (inst {
        assert($inst.tree != null); //modif
        $tree.add($inst.tree); //modif
        }
      )*
    ;

inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree=$e1.tree;
        }
    | SEMI {
        }
    | PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(false,$list_expr.tree); //modifs $tree =
            setLocation($tree,$PRINT);
        }
    | PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(false,$list_expr.tree);
            setLocation($tree,$PRINTLN);
        }
    | PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true,$list_expr.tree);
            setLocation($tree,$PRINTX);
        }
    | PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true,$list_expr.tree);
            setLocation($tree,$PRINTLNX);
        }
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
        }
    | WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE {
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($condition.tree,$body.tree);
        }
    | RETURN expr SEMI {
            assert($expr.tree != null);
            //sans-objet ?
        }
    ;

if_then_else returns[IfThenElse tree]
@init {
}
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
            ListInst currentList=new ListInst();
            $tree=new IfThenElse($condition.tree,$li_if.tree,currentList);
            IfThenElse lastTree=$tree;
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
            ListInst lastList=new ListInst();
            lastTree=new IfThenElse($elsif_cond.tree,$elsif_li.tree,lastList);
            currentList.add(lastTree);
            currentList=lastList;
        }
      )*
      (ELSE OBRACE li_else=list_inst CBRACE {
            lastTree.updateElseBranch($li_else.tree);   
        }
      )?
    ;

list_expr returns[ListExpr tree]
@init   {
            $tree = new ListExpr();
        }
    : (e1=expr {
            assert($e1.tree != null);
            $tree.add($e1.tree);
       }
       (COMMA e2=expr {
            assert($e2.tree != null);
            $tree.add($e2.tree);       
        }
       )* )?
    ;

expr returns[AbstractExpr tree]
    : assign_expr {
            assert($assign_expr.tree != null);
            $tree=$assign_expr.tree; //modif
        }
    ;

assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }
        }
        EQUALS e2=assign_expr {
            assert($e.tree != null);
            assert($e2.tree != null);
            $tree=new Assign( (AbstractLValue) $e.tree,$e2.tree); //modif
            setLocation($tree,$EQUALS);
        }
      | /* epsilon */ {
            assert($e.tree != null);
            $tree=$e.tree; //modif
        }
      )
      
    ;

or_expr returns[AbstractExpr tree]
    : e=and_expr {
            assert($e.tree != null);
            $tree=$e.tree; //modif
        }
    | e1=or_expr OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Or($e1.tree,$e2.tree); //modif
       }
    ;

and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
            assert($e.tree != null);
            $tree=$e.tree; //modif
        }
    |  e1=and_expr AND e2=eq_neq_expr {
            assert($e1.tree != null);                         
            assert($e2.tree != null);
            $tree=new And($e1.tree,$e2.tree); //modif
        }
    ;

eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
            assert($e.tree != null);
            $tree=$e.tree; //modif
        }
    | e1=eq_neq_expr EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Equals($e1.tree,$e2.tree);
        }
    | e1=eq_neq_expr NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new NotEquals($e1.tree,$e2.tree);
        }
    ;

inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
            assert($e.tree != null);
            $tree=$e.tree;
        }
    | e1=inequality_expr LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new LowerOrEqual($e1.tree,$e2.tree);
        }
    | e1=inequality_expr GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new GreaterOrEqual($e1.tree,$e2.tree);
        }
    | e1=inequality_expr GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Greater($e1.tree,$e2.tree);
        }
    | e1=inequality_expr LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Lower($e1.tree,$e2.tree);
        }
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
            //sans-objet
        }
    ;


sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
            assert($e.tree != null);
            $tree=$e.tree;
        }
    | e1=sum_expr PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Plus($e1.tree,$e2.tree);
        }
    | e1=sum_expr MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree=new Minus($e1.tree,$e2.tree);
        }
    ;

mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
            assert($e.tree != null);
            $tree=$e.tree;
        }
    | e1=mult_expr TIMES e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree=new Multiply($e1.tree,$e2.tree);
        }
    | e1=mult_expr SLASH e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree=new Divide($e1.tree,$e2.tree);
        }
    | e1=mult_expr PERCENT e2=unary_expr {
            assert($e1.tree != null);                                                                          
            assert($e2.tree != null);
            $tree=new Modulo($e1.tree,$e2.tree);
        }
    ;

unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree=new UnaryMinus($e.tree);
        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree=new Not($e.tree);
        }
    | select_expr {
            assert($select_expr.tree != null);
            $tree=$select_expr.tree;
        }
    ;

select_expr returns[AbstractExpr tree]
    : e=primary_expr {
            assert($e.tree != null);
            $tree=$e.tree;
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
            //sans-objet
        }
        (o=OPARENT args=list_expr CPARENT {
            // we matched "e1.i(args)"
            assert($args.tree != null);
            //sans-objet
        }
        | /* epsilon */ {
            //sans-objet
            // we matched "e.i"
        }
        )
    ;

primary_expr returns[AbstractExpr tree]
    : ident {
            assert($ident.tree != null);
            $tree=$ident.tree;
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
            //sans-objet
        }
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree=$expr.tree;
        }
    | READINT OPARENT CPARENT {
            $tree=new ReadInt();
        }
    | READFLOAT OPARENT CPARENT {
            $tree=new ReadFloat();
        }
    | NEW ident OPARENT CPARENT {
            assert($ident.tree != null);
            //sans-objet
        }
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
            //sans-objet
        }
    | literal {
            assert($literal.tree != null);
            $tree=$literal.tree;
        }
    ;

type returns[AbstractIdentifier tree]
    : ident {
            assert($ident.tree != null);
            $tree=$ident.tree;
        }
    ;

literal returns[AbstractExpr tree]
    : INT { //modif
            $tree=new IntLiteral( Integer.parseInt($INT.text) );
        }
    | fd=FLOAT { //pas modif
            $tree=new FloatLiteral( Float.parseFloat($fd.text) );
        }
    | str=STRING { //modif
            $tree=new StringLiteral( $str.text );
            setLocation($tree,$str);
        }
    | TRUE {
            $tree=new BooleanLiteral(true);
        }
    | FALSE {
            $tree=new BooleanLiteral(false);
        }
    | THIS {
            //sans-objet
        }
    | NULL {
            //sans-objet
        }
    ;

ident returns[AbstractIdentifier tree]
    : id=IDENT { //modif
            $tree=new Identifier( tableSymboles.create($id.text) ); //bug
            setLocation($tree,$id); //faudra peut etre changer ca
        }
    ;

/****     Class related rules     ****/

list_classes returns[ListDeclClass tree]
    @init {
            $tree=new ListDeclClass(); //m
    }
    :
    (c1=class_decl {
            //sans-objet
        }
    )*
    ;


class_decl
    : CLASS name=ident superclass=class_extension OBRACE class_body CBRACE {
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS ident {
        }
    | /* epsilon */ {
        }
    ;

class_body
    : (m=decl_method {
        }
      | decl_field_set
      )*
    ;

decl_field_set
    : v=visibility t=type list_decl_field
      SEMI
    ;

visibility
    : /* epsilon */ {
        }
    | PROTECTED {
        }
    ;

list_decl_field
    : dv1=decl_field
        (COMMA dv2=decl_field
      )*
    ;

decl_field
    : i=ident {
        }
      (EQUALS e=expr {
        }
      )? {
        }
    ;

decl_method
@init {
}
    : type ident OPARENT params=list_params CPARENT (block {
        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
        }
      ) {
        }
    ;

list_params
    : (p1=param {
        } (COMMA p2=param {
        }
      )*)?
    ;
    
multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param
    : type ident {
        }
    ;
