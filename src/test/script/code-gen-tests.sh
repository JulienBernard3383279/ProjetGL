#! /bin/bash

# Auteur : gl58
# Version initiale : 01/01/2017

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh

cd "$(dirname "$0")"/../../.. || exit 1

#PATH=./src/test/script/launchers:"$PATH"

cd src/test/deca/codegen/valid/ours/ || exit 1

PATH=../../../../../main/bin:"$PATH"
rm *.ass
rm result.data
rien=$("")
for cas_de_test in *.deca 
do

    decac  $cas_de_test
    echo "$cas_de_test : a compilé"
done
for program in *.ass
do
    echo "execution de $program:"
    ima $program>result.data
    result=$(grep FAIL result.data)
    if [ "$result" == "$rien" ]
    then 
	echo "PASS"
    else 
	echo "FAILED"
	exit 1
    fi
done

