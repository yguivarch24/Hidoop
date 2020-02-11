#! /bin/bash



if [ $# = 2 ]; then
    ssh "$1"@salameche java -cp "$2" config.InitNamingNode &
    echo "Naming Node Lancé"

    ssh "$1"@piafabec java -cp "$2" config.InitServeur 0 &
    echo "Serveur 0 Lancé"

    ssh "$1"@carapuce java -cp "$2" config.InitServeur 1 &
    echo "Serveur 1 Lancé"

    ssh "$1"@magicarpe java -cp "$2" config.InitServeur 2 &
    echo "Serveur 2 Lancé"

    ssh "$1"@rondoudou java -cp "$2" config.InitServeur 3 &
    echo "Serveur 3 Lancé"

else
    echo "Il faut votre login en parametre et le path de la racine de l'arborescence des fichiers compilés de hidoop avec les fichiers Project InitServeur et InitNamingNode dans le dossier config"
fi
