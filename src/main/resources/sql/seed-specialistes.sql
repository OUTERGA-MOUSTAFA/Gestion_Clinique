-- Comptes de test (mot de passe commun : password123)
-- Hash bcrypt : $2a$10$WOspQ3Qv0SPXNdwD47ZKJOnDO9uYVq22HT4D6ETLRmy68mt92sDyy

INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role)
SELECT 'Alami', 'Karim', 'generaliste@clinique.com',
       '$2a$10$WOspQ3Qv0SPXNdwD47ZKJOnDO9uYVq22HT4D6ETLRmy68mt92sDyy', 'GENERALISTE'
WHERE NOT EXISTS (
    SELECT 1 FROM utilisateur WHERE email = 'generaliste@clinique.com'
);

INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role)
SELECT 'Bennani', 'Sara', 'cardiologue@clinique.com',
       '$2a$10$WOspQ3Qv0SPXNdwD47ZKJOnDO9uYVq22HT4D6ETLRmy68mt92sDyy', 'SPECIALISTE'
WHERE NOT EXISTS (
    SELECT 1 FROM utilisateur WHERE email = 'cardiologue@clinique.com'
);

INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role)
SELECT 'Idrissi', 'Omar', 'pneumologue@clinique.com',
       '$2a$10$WOspQ3Qv0SPXNdwD47ZKJOnDO9uYVq22HT4D6ETLRmy68mt92sDyy', 'SPECIALISTE'
WHERE NOT EXISTS (
    SELECT 1 FROM utilisateur WHERE email = 'pneumologue@clinique.com'
);

INSERT INTO specialiste (utilisateur_id, specialite, tarif)
SELECT u.id, 'CARDIOLOGIE', 300
FROM utilisateur u
WHERE u.email = 'cardiologue@clinique.com'
  AND NOT EXISTS (
      SELECT 1 FROM specialiste s WHERE s.utilisateur_id = u.id
  );

INSERT INTO specialiste (utilisateur_id, specialite, tarif)
SELECT u.id, 'PNEUMOLOGIE', 250
FROM utilisateur u
WHERE u.email = 'pneumologue@clinique.com'
  AND NOT EXISTS (
      SELECT 1 FROM specialiste s WHERE s.utilisateur_id = u.id
  );

-- Consultation de test (id 12 pour Thunder Client).
-- Si la table du brief 1 a d'autres colonnes NOT NULL, inserer une consultation existante a la place.
INSERT IGNORE INTO consultation (id) VALUES (12);
