#! /bin/bash
fileServeur="serveur.config"
fileNamingNode="namingNode.config"
if [ $# = 2 ]; then
  serveurs=$(cat "$fileServeur" | sed -n -e 'H;${x;s/\n/,/g;s/^,//;p;}')
  namingNode=$(cat "$fileNamingNode")
  IFS=':' read -ra host <<< "$namingNode"
  echo "$serveurs"
  ssh "$1"@"${host[0]}" java -cp "$2" config.InitNamingNode "$serveurs" "$namingNode" &
  compteur=0
  while IFS= read -r line
  do
    IFS=':' read -ra host <<< "$line"
    echo "${host[0]}"
    ssh "$1"@"${host[0]}" java -cp "$2" config.InitServeur "$compteur" "$serveurs" "$namingNode" &
    compteur=$(( compteur + 1 ))
  done <"$fileServeur"
else
    echo "Il faut votre login en parametre et le path de la racine de l'arborescence des fichiers compilés de hidoop avec les fichiers Project InitServeur et InitNamingNode dans le dossier config"
fi
