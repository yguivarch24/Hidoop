#! /bin/bash
if [ $# = 1 ]; then
      ssh "$1"@salameche "kill -9 \`ps -C java -o pid=\`"


          ssh "$1"@piafabec "kill -9 \`ps -C java -o pid=\`"

          ssh "$1"@carapuce "kill -9 \`ps -C java -o pid=\`"

          ssh "$1"@magicarpe "kill -9 \`ps -C java -o pid=\`"

          ssh "$1"@rondoudou "kill -9 \`ps -C java -o pid=\`"
else
    echo "Il faut votre login en parametre"
fi