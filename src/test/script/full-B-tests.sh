#! /bin/bash

# Auteur : gl58
# Version originale : 19/01/2017

# Tests pour la verification contextuelle avec objets.

cd "$(dirname "$0")"/../../.. || exit 1

#PATH=./src/test/script/launchers:"$PATH"

echo "/****************************/"
echo "/*******Invalid deca*********/"
echo "/****************************/"

for cas_de_test in src/test/deca/context/invalid/pierre/objects/*
do
    echo "$cas_de_test : "

    if src/test/script/launchers/test_context $cas_de_test 2>&1 | grep -q -e $cas_de_test:[0-9][0-9]*
    then 
        echo "Test reussi"
    else
        echo "Echec"
        exit 1
    fi
done

echo "/****************************/"
echo "/*******Valid deca***********/"
echo "/****************************/"

for cas_de_test in src/test/deca/context/valid/pierre/objects/*
do
    echo "$cas_de_test : "

    if src/test/script/launchers/test_context $cas_de_test 2>&1 | grep -q -e $cas_de_test:[0-9][0-9]*
    then 
        echo "Echec"
        exit 1
    else 
        echo "Test reussi"
    fi
done
