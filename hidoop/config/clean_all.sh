#! /bin/bash

fileServeur="serveur.config"
fileNamingNode="namingNode.config"

if [ $# = 1 ]; then


    namingNode=$(cat "$fileNamingNode")
    IFS=':' read -ra host <<< "$namingNode"
    ssh "$1"@"${host[0]}" "kill -9 \`ps -C java -o pid=\`"

    while IFS= read -r line
    do
      IFS=':' read -ra host <<< "$line"
      ssh "$1"@"${host[0]}" "kill -9 \`ps -C java -o pid=\`" < /dev/null
    done <"$fileServeur"

else
    echo "Il faut votre login en parametre"
fi