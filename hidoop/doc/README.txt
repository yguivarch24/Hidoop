    ###############################################
    #                                             #
    #          Comment utiliser Hidoop ?          #
    #                                             #
    ###############################################

Tout les scripts se trouve dans le dossier hidoop/config

Etape 1 :
Lancer le script init_key.sh avec en paramètre le nom d'utilisateur de la session que vous souhaitez utiliser

Exemple :
    /bin/bash init_key.sh yguivarc

Ce script permet de créer des clé ssh pour 5 ordinateurs de l'ENSEEIHT (salameche, piafabec, carapuce, magicarpe et rondoudou)
Cela peremetra d'accélérer l'étape suivante.



Etape 2 :
Lancer le script init_hidoop_server.sh avec en paramètre le nom d'utilisateur de la session que vous souhaitez utiliser et le chemin absolu vers la racine de l'arborescence où se trouve tous les fichiers compilés (.class)

Exemple :
    /bin/bash init_hidoop_server.sh yguivarc /home/yguivarc/2_annee/Projet_Intergiciel/out/production/hidoop

Ce script permet de lancer un NamingNode sur salameche et 4 Serveurs sur les autres ordinateurs.
Cela bloquera le terminal.


Etape 3 :
Lancer le main de AppHidoop en précisant la racine de l'arborescence des fichiers compilés.

Exemple :
    java -cp /home/yguivarc/2_annee/Projet_Intergiciel/out/production/hidoop ordo.AppHidoop <nomFichierEntree> <nomFichierSortie> Line

Note importante :  Il faut lancer AppHidoop dans le même dossier que le fichier à traiter (il faut donner uniquement le nom du fichier sans son chemin).


Etape 4 :
Si vous souhaitez arrêter les Serveurs, fermez le terminal bloqué de l'étape 2 (ou faites un CTRL-C) puis lancez le script clean_all.sh avec le nom d'utilisateur de la session que vous avez utilisé pour lancer les Serveurs.

Exemple :
    /bin/bash clean_all.sh yguivarc

Cela va tuer tous les programmes java sur les machines que nous avons utilisé (Certain kill ne vont pas fonctionner mais c'est normal).


Note : Il est important d'utiliser /bin/bash et non sh car sinon certaine commande ne fonctionne pas.


    #############################################################
    #                                                           #
    #          Comment ajouter ou retirer un serveur ?          #
    #                                                           #
    #############################################################

Dans la classe Project, 2 listes permettent de créer les serveurs HOSTS et HOSTSPORTS.
    
    - HOSTS contient le nom de tous les ordinateurs que l'on utilise comme serveur.
    
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


    #############################################################
    #                                                           #
    #        Comment générer un fichier .txt volumineux ?       #
    #                                                           #
    #############################################################

Pour générer un fichier volumineux, il suffit d'utiliser la commande suivante :
    yes `cat [nom_du_petit_fichier]` | head -c [nombre_de_Go_voulus]GB > [nom\_du\_gros\_fichier]

Exemple : (filesample.txt étant un fichier .txt de ~1Ko)
    yes `cat filesample.txt` | head -c 10GB > filesample_gros.txt

    Cette commande génère le fichier filesample_gros.txt d'un taille de 10Go contenant le contenu
    de filesample répéter un grand nombre de fois.