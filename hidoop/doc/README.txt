    ###############################################
    #                                             #
    #          Comment utiliser Hidoop ?          #
    #                                             #
    ###############################################

Tout les scripts et fichier .config se trouve dans le dossier hidoop/config
Pour le bon fonctionnement des scripts il faut les lancer depuis le même repertoire où se trouve les fichiers .config
Il ne faut pas utiliser "sh" mais plutôt "/bin/bash" pour lancé les scripts sinon certain scripts ne fonctionneron

Etape 1 :

Lancer le script init_key.sh avec en paramètre le nom d'utilisateur de la session que vous souhaitez utiliser

Exemple :
    /bin/bash init_key.sh yguivarc

Ce script permet de créer des clé ssh pour les ordinateurs de l'ENSEEIHT utilisé pour hidoop.
Cela peremetra d'accélérer l'étape suivante.



Etape 2 :
Lancer le script init_hidoop_server.sh avec en paramètre le nom d'utilisateur de la session que vous souhaitez utiliser et le chemin absolu vers la racine de l'arborescence où se trouve tous les fichiers compilés (.class)

Exemple :
    /bin/bash init_hidoop_server.sh yguivarc /home/yguivarc/2_annee/Projet_Intergiciel/out/production/hidoop

Ce script permet de lancer le NamingNode et les Serveurs sur les autres ordinateurs.


Etape 3 :
Lancer le main de AppHidoop avec comme paramètre :
    -l'arborescence des fichiers compilés (pour le -cp de java)
    -le namingNode utilisé
    -le fichier en entré
    -le fichier en sortie
    -le type de traitement à utiliser (par défault : Line)
    

Exemple :
    java -cp /home/yguivarc/2_annee/Projet_Intergiciel/out/production/hidoop ordo.AppHidoop salameche:12344 test.txt sortie.txt Line




Etape 4 :
Si vous souhaitez arrêter les Serveurs, lancez le script clean_all.sh avec le nom d'utilisateur de la session que vous avez utilisé pour lancer les Serveurs.

Exemple :
    /bin/bash clean_all.sh yguivarc

Cela va tuer tous les programmes java sur les machines que nous avons utilisé (Certain kill ne vont pas fonctionner mais c'est normal) et supprimer les potentiels fichiers restant.



    #############################################################
    #                                                           #
    #          Comment ajouter ou retirer un serveur ?          #
    #                                                           #
    #############################################################

Si vous voulez ajouter ou supprimer un serveur il suffit de modifier le fichier serveur.config

Exemple : 
Voilà à quoi ressemble le fichier serveur.config
#################
piafabec:4010
carapuce:4011
magicarpe:4012
rondoudou:4013
################
Une ligne est représenté par le nom du pc utilisé, de ":", de son port, et d'un retour à la ligne.
Si vous voulez ajouter ou retirer un seveur il suffit d'ajouter ou de retirer une ligne. 


Si vous voulez modifier le namingNode il suffit de modifier le fichier namingNode.config

Exemple : 
Voilà à quoi ressemble le fichier namingNode.config
################
salameche:12344
################
Comme pour le fichier serveur.config la ligne représente le nom du pc utilisé, de ":", de son port, et d'un retour à la ligne.
Si vous voulez modifier le namingNode il suffit de modifier la ligne.

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
