#! /bin/bash



if [ $# = 2 ]; then
    ssh $1@fluor
    java $2/InitNamingNode
    logout

    ssh $1@phenix
    java $2/InitServeur 0
    logout
    
    ssh $1@beatles
    java $2/InitServeur 1
    logout

    ssh $1@bouba
    java $2/InitServeur 2
    logout

    ssh $1@albator
    java $2/InitServeur 3
    logout

else
    echo "Il faut votre login en parametre et le path du fichier de config"
fi