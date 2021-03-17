# Situation Achat de crédit:
- Entrez numéro bénéficiaire avec montant et date
- Si compte débiteur alors créditer son compte: Nouveau Solde = abs()
- Envoies de notification

# Situation Achat de forfait:
- Choisir forfait
- Choisir mode de paiement (mobile money, crédit)
- Entrez numéro bénéficiaire et date
- Vérifier que son solde mobile money ou crédit est suffisant et que la limite d'utilisation du forfait n'est pas dépassé

# Situation Abonnement:
- Choisir abonnement
- Mobile money
- Mon numéro et date
- Vérifier que son solde mobile money est suffisant,
- Vérifier à chaque fin de période d'utilisation et débité (analana) le compte de l'utilisateur

# Situation désabonnement:
- Liste des abonnements
- clique sur désabonnement
- Vérifier que l'utilisateur ne s'est pas déjà désabonné
- en fonction du nombre de jours de préavis, désabonnement effectif de l'utilisateur

# Situation transfert de crédit:
- numéro bénéficiaire et numéro déficitaire
- Vérifier solde crédit du déficitaire
- Créditer (Ampitombona) crédit bénéficiaire
- Débiter (Analana) crédit déficitaire

# Situation transfert de mobile money:
- numéro bénéficiaire et numéro déficitaire
- Vérifier solde mobile money du déficitaire
- Créditer (Ampitombona) mobile_money bénéficiaire
- Débiter (Analana) mobile_money déficitaire + Frais du transfert

# Situation achat d'offre pour autre numéro:
- numéro bénéficiaire et numéro déficitaire
- Vérifier solde crédit du déficitaire
- Créditer (Ampitombona) offre bénéficiaire
- Débiter (Analana) crédit, mobile_money déficitaire + Frais du transfert

# Situation Appel
- Sélectionner contact
- Date d'appel
- Entrez durée d'appel
- Vérifier si numéro destinataire est interne ou externe

# Situation Message:
- Sélectionner contact
- Date d'envoi
- Entrez message
- Vérifier si numéro destinataire est interne ou externe

# Situation Internet:
- Sélectionner site internet
- Date de début et Date de fin
- Montant méga consomé