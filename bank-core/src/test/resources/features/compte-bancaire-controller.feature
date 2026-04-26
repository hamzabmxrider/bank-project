# language: fr

Fonctionnalité: Feature 1

  Scénario: Dépôt d'argent sur un compte bancaire met à jour le compte le nouveau solde
    Soit Créer un compte bancaire avec un montant de "70.00"
    Quand j'effectue un dépôt sur ce compte avec un montant de "30.00"
    Alors la réponse HTTP doit être 200
    Et ce compte bancaire doit avoir un solde de "100.00"

  Scénario: Retrait d'argent depuis un compte bancaire met à jour le compte le nouveau solde
    Soit Créer un compte bancaire avec un montant de "70.00"
    Quand j'effectue un retrait de ce compte avec le montant "30.00"
    Alors la réponse HTTP doit être 200
    Et ce compte bancaire doit avoir un solde de "40.00"
