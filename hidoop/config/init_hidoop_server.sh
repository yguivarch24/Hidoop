#! /bin/bash



if [ $# = 2 ]; then
    ssh $1@fluor java $2/InitNamingNode
    

    ssh $1@phenix java $2/InitServeur 0
      
    ssh $1@beatles java $2/InitServeur 1

    ssh $1@bouba java $2/InitServeur 2

    ssh $1@bravoos java $2/InitServeur 3

else
    echo "Il faut votre login en parametre et le path du fichier de config avec les fichiers Project InitServeur et InitNamingNode"
fi
