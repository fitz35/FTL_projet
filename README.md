# FTL_projet
## introduction
Projet de PO1 sur le jeu Faster Than Light. Le projet consiste à reproduire le jeu FTL en java et simplifié. (cf : consigne) Voici, ci-dessous, les changements aportés au projet de base.

## Map de jeu
Une map de jeu de 10 secteurs * 10 secteurs (pouvant être agrandis) a été implémentée. Elle ajoute 3 secteurs différents : les dépots de ressources, les markets et les combats. Quand un joueur en visite 1, il ne peux plus y retourner, déplacez-vous avec sagesse !

### dépots de ressources
Quand le joueur le visite, il obtient 20 pièces, pouvant être dépensées dans le market.

### markets
Quand le joueur le visite, il a le choix d'acheter jusqu'à trois objets générés aléatoirement lors de la création de la map.

### combats
C'est la class World du projet de base, c'est ici que le joueur peux se mesurer à d'autre vaisseau.

## membres d'équipages
Les membres d'équipages possèdent un type correspondants à chaque module existants, ou aucun type. Ils boostent de 2 fois les bonus de leur module associé, et baissent le bonus des autres par 2. Le membre d'équipage ayant comme attribut rien (none), donne un bonus constant de 1.

## modules

## armes
### death gun
Le death gun est une arme endgame, faisant très mal. Elle tire un projectile qui se sépare en 3 arrivé au milieu de l'écran.
### cheat
Une arme uniquement accessible en cheat, hérité de missile pour percer les bouclier, mais avec des missile infinis et des dégats infinis.

## Commandes
Les codes cheats sont activées en appuyant sur les touches précisée les unes après les autres sans en appuyer sur une autre.
### combats
#### gestion des modules
- &, é : respectivement selectionner le module de droite ou de gauche (sous-ligner en vert)
- " : bouton d'action (ajouter ou enlever de l'energie) 
#### gestion des armes
- A, Z : respectivement selectionner l'arme de droite ou de gauche (sous-ligner en vert)
- E : bouton d'action (activer, desactiver ou tirer) 
- fleches : case sur laquelle on tire
#### gestion des membres d'équipages
- W, X : respectivement selectionner le membre de droite ou de gauche (sous-ligner en vert)
- D : bouton d'action (selectionner ou déselectionner) 
- X, V, F, C : selectionner la case de gauche, droite, haut ou bas (respectivement) du membre selectionne
- G : teleporte le membre selectionne sur la case selectionnee si possible
#### cheat
- Y, Y, Y : upgrade tout les modules posséder par le joueur au niveau max.

- V, B, C : Ajoute l'arme Cheat a l'inventaire du joueur.
- V, B, D : Ajoute l'arme Death a l'inventaire du joueur.
- V, B, I : Ajoute l'arme Ion a l'inventaire du joueur.
- V, B, L : Ajoute l'arme Laser a l'inventaire du joueur.
- V, B, M : Ajoute l'arme Missile a l'inventaire du joueur.

### map
#### déplacement
- fleches directionnelles : déplacement du joueur sur la carte
- F1 : afficher/masquer les stats du joueurs
#### cheat
- M, M, M : Ajoute 5000 coins à l'inventaire du joueur.
- A, L, V : Ajoute 5 level au vaisseau enemi
### market

#### cheat

### autres commandes
## autre
### récompenses de fin de combat
A chaque fois que le joueur sort victorieux d'un combat il obtient aléatoirement soit une réparation, une arme ou un membre d'équipage. 
De plus, Le joueur peux choisir un module a ameliorer. En contrepartit, le vaisseau enemie obtient un level de plus, donc la fois suivante, il sera plus fort.
### IA
L'IA implémenté est une ia naive qui attaque aléatoirement les cases avec des armes aléatoires.
## crédits
Ceci est un projet dans le cadre du module PO1 en deuxième année de license informatique à l'université de Rennes 1. Réalisé par Clément Lahoche et Léo Thuillier.


 
