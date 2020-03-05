#! /bin/bash

fileServeur="serveur.config"
fileNamingNode="namingNode.config"
if [ $# = 1 ]; then
    ssh-keygen -t rsa -b 4096

    namingNode=$(cat "$fileNamingNode")
    IFS=':' read -ra host <<< "$namingNode"
    ssh-copy-id "$1"@"${host[0]}"
    while IFS= read -r line
    do
      IFS=':' read -ra host <<< "$line"
      ssh-copy-id "$1"@"${host[0]}"
    done <"$fileServeur"

else
    echo "Il faut votre login en parametre"
fi
