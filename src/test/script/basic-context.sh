#! /bin/sh

# Auteur : gl58
# Version initiale : 01/01/2017

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

if test_context src/test/deca/context/invalid/provided/affect-incompatible.deca 2>&1 | \
    grep -q -e 'affect-incompatible.deca:15:'
then
    echo "Check incompatible assign : OK"
else
    echo "Check incompatible assing : FAIL"
    exit 1
fi

if test_context src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e 'hello-world.deca:[0-9]'
then
    echo "Test hello-world : FAIL"
    exit 1
else
    echo "Test hello-world : OK"
fi

if test_context src/test/deca/context/valid/pierre/testAllTrees.deca 2>&1 | grep -q -e 'testAllTrees.deca:[0-9][0-9]*'
then
    echo "Test assign, cmp, while, if, modulo, boolOp, print : FAIL"
    exit 1
else
    echo "Test assign, cmp, while, if, modulo, boolOp, print : OK"
fi

if test_context src/test/deca/context/invalid/pierre/sum_affect.deca 2>&1 | grep -q -e 'sum_affect.deca:5:'
then
    echo "Check invalid type of sum : OK"
else 
    echo "Check invalid type of sum : FAIL"
    exit 1
fi

if test_context src/test/deca/context/invalid/pierre/condition_not_bool.deca 2>&1 | grep -q -e 'condition_not_bool.deca:2:'
then 
    echo "Check for bool condition : OK"
else
    echo "Check for bool condition : FAIL"
    exit 1
fi

if test_context src/test/deca/context/invalid/pierre/incomp.deca 2>&1 | grep -q -e 'incomp.deca:4:'
then
    echo "Check incompatible comparison : OK"
else 
    echo "Check incompatible comparison : FAIL"
    exit 1
fi

if test_context src/test/deca/context/invalid/pierre/undefined_type.deca 2>&1 | grep -q -e 'undefined_type.deca:2:'
then 
    echo "Check for undefined type : OK "
else 
    echo "Check for undefined type : FAIL"
    exit 1
fi

if test_context src/test/deca/context/invalid/pierre/void.deca 2>&1 | grep -q -e 'void.deca:2:'
then
    echo "Check for void variable : OK "
else
    echo "Check for void variable : FAIL "
    exit 1
fi

if test_context src/test/deca/context/invalid/pierre/print-arguments.deca 2>&1 | grep -q -e 'print-arguments.deca:7:'
then 
    echo "Check print arguments : OK"
else
    echo "Check print arguments : FAIL"
    exit 1
fi

if test_context src/test/deca/context/invalid/pierre/op-arith.deca 2>&1 | grep -q -e 'op-arith.deca:3:'
then 
    echo "Check operands of +,-,*,/ : OK"
else
    echo "Check operands of +,-,*,/ : FAIL"
    exit 1
fi

if test_context src/test/deca/context/invalid/pierre/modulo.deca 2>&1 | grep -q -e 'modulo.deca:2:'
then 
    echo "Check for float in % : OK"
else
    echo "Check for float in % : FAIL"
    exit 1 
fi

if test_context src/test/deca/context/invalid/pierre/minus.deca 2>&1 | grep -q -e 'minus.deca:2:'
then
    echo "Check type of minus : OK "
else
    echo "Check type of minus : FAIL "
    exit 1 
fi

if test_context src/test/deca/context/invalid/pierre/not.deca 2>&1 | grep -q -e 'not.deca:2:'
then
    echo "Check not for boolean : OK "
else 
    echo "Check not for boolean : FAIL "
    exit 1
fi
