#! /bin/bash

# Auteur : gl58
# Version initiale : 01/01/2017

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh

cd "$(dirname "$0")"/../../.. || exit 1

#PATH=./src/test/script/launchers:"$PATH"

cd src/test/deca/syntax/valid/homebrew/ || exit 1

PATH=../../../../../main/bin:"$PATH"

for cas_de_test in * ../idempotent/*
do
    echo "$cas_de_test :"

    result=$(decac -p $cas_de_test)
    
    #ATTENDU = cat '../idempotent/"$cas_de_test"'
    attendu=$(cat ../idempotent/"$cas_de_test")

    if [ "$result" == "$attendu" ]
    then
        echo "Test réussi"
    else
        echo "Echec"
        exit 1
    fi
done

cd ../classes/homebrew/ || exit 1

PATH=../../../../../../main/bin:"$PATH"

for cas_de_test in * ../idempotent/*
do
    echo "$cas_de_test :"

    result=$(decac -p $cas_de_test)
    
    #ATTENDU = cat '../idempotent/"$cas_de_test"'
    attendu=$(cat ../idempotent/"$cas_de_test")

    if [ "$result" == "$attendu" ]
    then
        echo "Test réussi"
    else
        echo "Echec"
        exit 1
    fi
done

