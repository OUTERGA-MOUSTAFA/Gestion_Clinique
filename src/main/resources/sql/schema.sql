CREATE TABLE specialiste (
    id BIGINT NOT NULL AUTO_INCREMENT,
    utilisateur_id BIGINT NOT NULL,
    specialite VARCHAR(50) NOT NULL,
    tarif DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_specialiste_utilisateur UNIQUE (utilisateur_id),
    CONSTRAINT fk_specialiste_utilisateur
        FOREIGN KEY (utilisateur_id) REFERENCES utilisateur (id)
);

CREATE TABLE demande_expertise (
    id BIGINT NOT NULL AUTO_INCREMENT,
    consultation_id BIGINT NOT NULL,
    specialiste_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    priorite VARCHAR(20),
    statut VARCHAR(20),
    avis TEXT,
    recommandations TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_demande_expertise_specialiste
        FOREIGN KEY (specialiste_id) REFERENCES specialiste (id)
);
