#! /bin/bash



if [ $# = 1 ]; then
    ssh-keygen -t rsa -b 4096
    ssh-copy-id $1@fluor
    ssh-copy-id $1@phenix
    ssh-copy-id $1@beatles
    ssh-copy-id $1@bouba
    ssh-copy-id $1@bravoos

else
    echo "Il faut votre login en parametre"
fi
