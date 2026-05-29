CREATE TABLE IF NOT EXISTS gameservers (
    server_id      INT NOT NULL COMMENT 'ID unic al GameServer-ului',
    hexid          VARBINARY(32) NOT NULL COMMENT 'Cheie binară de autentificare (256-bit)',
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data la care serverul a fost înregistrat',

    PRIMARY KEY (server_id),

    -- Index pentru căutare rapidă după hexid (autentificare GameServer)
    UNIQUE KEY idx_hexid (hexid),

    -- Index pentru filtrare după data creării (monitorizare, audit)
    KEY idx_created_at (created_at)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
COMMENT='Registry pentru GameServer-ele autorizate să se conecteze la LoginServer';
