#! /bin/bash



if [ $# = 1 ]; then
    ssh-keygen -t rsa -b 4096
    ssh-copy-id "$1"@salameche
    ssh-copy-id "$1"@piafabec
    ssh-copy-id "$1"@carapuce
    ssh-copy-id "$1"@magicarpe
    ssh-copy-id "$1"@rondoudou

else
    echo "Il faut votre login en parametre"
fi
