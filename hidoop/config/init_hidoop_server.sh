#! /bin/bash



if [ $# = 2 ]; then
    ssh $1@salameche java -cp $2 config.InitNamingNode
    

    ssh $1@piafabec java -cp $2 config.InitServeur 0
      
    ssh $1@carapuce java -cp $2 config.InitServeur 1

    ssh $1@magicarpe java -cp $2 config.InitServeur 2

    ssh $1@rondoudou java -cp $2 config.InitServeur 3

else
    echo "Il faut votre login en parametre et le path de la racine de l'arborescence des fichiers compilés de hidoop avec les fichiers Project InitServeur et InitNamingNode dans le dossier config"
fi
