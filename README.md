# Service de télé-expertise médicale (Brief 2)

API REST Jakarta EE (JAX-RS / Jersey) permettant à un généraliste de demander l'avis d'un spécialiste. L'API est **stateless** : chaque requête s'authentifie avec **HTTP Basic** (email + mot de passe dans l'en-tête `Authorization`).

Les appels se font avec **Thunder Client** (VS Code), pas Postman.

## Stack

- Java 17, Maven, Tomcat 10+
- JAX-RS (Jersey), Jackson
- JPA / Hibernate, MySQL
- BCrypt (`jBCrypt`)
- Architecture : resource → service → repository

## Prérequis

1. MySQL avec une base `gestion_clinique` (la même que le brief 1).
2. Adapter l'utilisateur / mot de passe JDBC dans `src/main/resources/META-INF/persistence.xml` si besoin.
3. Démarrer Tomcat 10, puis déployer le WAR.

```bash
./mvnw package
```

Le WAR généré est `target/gestion-clinique.war`. URL de base :

`http://localhost:8080/gestion-clinique/api`

4. Exécuter le script des comptes de test **après** un premier démarrage (Hibernate crée / met à jour les tables) :

`src/main/resources/sql/seed-specialistes.sql`

Si la table `consultation` du brief 1 a des colonnes `NOT NULL` supplémentaires, insérez une consultation existante et utilisez son `id` à la place de `12`.

## Comptes de test (mot de passe : `password123`)

| Rôle         | Email                       | Usage                                      |
|--------------|-----------------------------|--------------------------------------------|
| GENERALISTE  | generaliste@clinique.com    | Lister les spécialistes, créer une demande |
| SPECIALISTE  | cardiologue@clinique.com    | Recevoir / répondre (cardiologie, 300 DH)  |
| SPECIALISTE  | pneumologue@clinique.com    | Deuxième spécialiste (pneumologie, 250 DH) |

## Endpoints

| Méthode | Endpoint | Rôle | Succès |
|---------|----------|------|--------|
| GET | `/api/ping` | public | 200 |
| GET | `/api/me` | GENERALISTE, SPECIALISTE | 200 |
| GET | `/api/specialistes?specialite=CARDIOLOGIE` | GENERALISTE | 200 |
| POST | `/api/demandes` | GENERALISTE | 201 |
| GET | `/api/demandes?statut=EN_ATTENTE` | SPECIALISTE | 200 |
| GET | `/api/demandes?consultationId=12` | GENERALISTE | 200 |
| PUT | `/api/demandes/{id}/reponse` | SPECIALISTE | 200 |

Codes d'erreur : **400** (validation), **401** (identifiants absents / faux), **403** (rôle ou propriétaire), **404** (ressource absente).

L'identité n'est **jamais** lue dans un paramètre de requête : un spécialiste ne voit et ne traite que ses propres demandes (`SecurityContext`).

### POST `/api/demandes`

```json
{
  "consultationId": 12,
  "specialisteId": 1,
  "question": "ECG anormal, suspicion de trouble du rythme. Votre avis ?",
  "priorite": "URGENTE"
}
```

Priorités : `URGENTE`, `NORMALE`, `NON_URGENTE`.  
Spécialités : `CARDIOLOGIE`, `PNEUMOLOGIE`, `DERMATOLOGIE`, `NEUROLOGIE`, `ENDOCRINOLOGIE`.

### PUT `/api/demandes/{id}/reponse`

```json
{
  "avis": "Trouble du rythme probable.",
  "recommandations": "Holter 24h, avis cardiologique rapproché."
}
```

La demande passe au statut `TERMINEE`.

## Thunder Client

1. Installer l'extension **Thunder Client**.
2. Import → fichiers :
   - `thunder-tests/thunder-collection_TeleExpertise.json`
   - `thunder-tests/thunder-environment_Local.json`
3. Activer l'environnement **Tele-expertise Local**.
4. Auth Basic : chaque requête utilise `{{generalisteEmail}}` / `{{cardiologueEmail}}` / `{{pneumologueEmail}}` et le mot de passe de l'environnement.

Parcours de démo : ping → `/me` → liste CARDIOLOGIE → créer une demande → lister `EN_ATTENTE` (cardiologue) → répondre → revoir la demande avec `consultationId=12`. Vérifier aussi 401 sans identifiants, 403 avec le mauvais rôle, 400 / 404 sur les cas d'erreur.

## User stories

- **US0** : `BasicAuthFilter` (bcrypt) + `SecurityContext` + `@RolesAllowed`
- **US1** : liste des spécialistes filtrée / triée par tarif (Stream API)
- **US2** : création `EN_ATTENTE` avec validation 400 / 404
- **US3** : listes filtrées par statut, tri `URGENTE` en premier, identité dans le `SecurityContext`
- **US4** : réponse + 403 si la demande appartient à un autre spécialiste
