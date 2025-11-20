# tableorganizer

SUB_COMMAND	1
SUB_COMMAND_GROUP	2	Transparent to the user; a way to organize your commands in code
STRING	3
INTEGER	4	Any integer between -2^53 and 2^53
BOOLEAN	5
USER	6
CHANNEL	7	Includes all channel types + categories
ROLE	8
MENTIONABLE	9	Includes all users/roles
NUMBER	10	Any floating point number between -2^53 and 2^53
ATTACHMENT	11	File attachment

# Use case

US : 
 - soumettre une table
 - soumettre des dispos personnelles
   -> signaler la non dispo sur une liste de dates jj/mm
 - relancer une demande de dispo pour une table
 - confirmer une date
 - confirmer une occupation de dispo
 - rappels auto 24/48/72h avant


Data :
 - table : 
   - nom campagne
   - système JDR
   - MJ
   - liste de joueurs
   - liste de dates proposées
   - prochaine date confirmée
 - joueur extends utilisateur
 - MJ extends utilisateur
 - utilisateur
   - id
   - nom
   - liste de jours pris


US utilisateur :
 - créer une table (et en devenir MJ)
 - signaler la non dispo sur une liste de dates jj/mm


US joueur :
 - valider une date
   -> verrouille automatiquement une date

US MJ :
 - éditer les informations d'une table
 - ajouter des joueurs à une table
 - relancer les joueurs d'une table
 - proposer une date
 - confirmer une date
 - dé-confirmer une date
 - programmer des rappels


