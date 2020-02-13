###############################################
#                                             #
#          Comment utiliser Hidoop ?          #
#                                             #
###############################################

Etape 1 :
Lancé le script init_key.sh avec en paramètre le nom d'utilisateur de la session que vous souhaitez utiliser

Exemple :
    sh init_key.sh yguivarc

Ce script permet de créer des clé ssh pour 5 ordinateurs de l'ENSEEIHT (salameche, piafabec, carapuce, magicarpe et rondoudou)
Cela peremetra d'accélérer l'étape suivante.



Etape 2 :
Lancé le script init_hidoop_server.sh avec en paramètre le nom d'utilisateur de la session que vous souhaitez utiliser et le chemin absolu vers la racine de l'harborescence où se trouve tout les fichiers compilé (.class)

Exemple :
    sh init_hidoop_server.sh yguivarc /home/yguivarc/2_annee/Projet_Intergiciel/out/production/hidoop

Ce script permet de lancer un NamingNode sur salameche et 4 Serveur sur les autres ordinateurs
Cela bloquera le terminal.



Etape 3 :
Lancé le main de AppHidoop en précisant la racine due l'harborescence des fichiers compilé.

Exemple :
    java -cp /home/yguivarc/2_annee/Projet_Intergiciel/out/production/hidoop ordo.AppHidoop <nomFichierEntree> <nomFichierSortie> Line


Note importante :  Il faut lancé AppHidoop dans le même dossier que le fichier à traiter (il faut donner uniquement le nom du fichier sans son chemin)

Etape 4 :
Si vous souhaitez arrêter les Serveurs, fermez le terminal bloqué de l'étape 2 (ou faites un CTRL-C) puis lancé le script clean_all.sh avec le nom d'utilisateur de la session que vous avez utilisé pour lancer les Serveurs

Exemple :
    sh clean_all.sh yguivarc

Cela va tuer tout les programmes java sur les machines que nous avons utilisé.(Certain kill ne vont pas fonctionner mais c'est normal) 

#############################################################
#                                                           #
#          Comment ajouter ou retirer un serveur ?          #
#                                                           #
#############################################################

Dans la classe Project, 2 listes permettent de créer les serveurs HOSTS et HOSTSPORTS.
    
    - HOSTS contient le nom de tout les ordinateurs que l'ont utilise comme serveur.
    
    - HOSTSPORTS contient le port à utiliser pour chaque serveur.

Si vous voulez ajouter/retirer un serveur il faut ajouter/retirer son nom et son port dans les 2 listes.
Ensuite il faut modifier les scripts d'initialisation pour que le serveur puisse être lancé.

init_key.sh :

    Ajouter/Retirer " ssh-copy-id "$1"@<nomOrdinateur> " à l'intérieur du "if"

Exemple :
    ssh-copy-id "$1"@rondoudou


init_hidoop_server.sh :
    
    Ajouter/Retirer " ssh "$1"@<nomOrdinateur> java -cp "$2" config.InitServeur <positionDansHOSTS> & " à l'intérieur du "if"

Exemple :

    Liste HOSTS et HOSTSPORTS dans Project :
                                                0          1          2             3
    public final static String[] HOSTS =  {"piafabec","carapuce","magicarpe", "rondoudou"};
    public final static Integer[] HOSTSPORT = {4010,4011,4012,4013};

    Dans init_hidoop_server.sh :

        ssh "$1"@rondoudou java -cp "$2" config.InitServeur 3 &

        "rondoudou" est à la 4ème position dans HOSTS donc on donne 3 comme paramètre à InitServeur.
        Son port est le 4013


clean_all.sh :
    Ajouter/Retirer " ssh "$1"@<nomOrdinateur> "kill -9 \`ps -C java -o pid=\`" " à l'intérieur du if

Exemple :
    ssh "$1"@rondoudou "kill -9 \`ps -C java -o pid=\`"