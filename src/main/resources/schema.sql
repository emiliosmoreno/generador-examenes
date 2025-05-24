-- Esquema para generador de exámenes de inglés (verbos y tiempos verbales)
CREATE TABLE verbo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    infinitivo VARCHAR(64) NOT NULL,
    pasado_simple VARCHAR(64) NOT NULL,
    participio_pasado VARCHAR(64) NOT NULL,
    significado VARCHAR(128)
);

CREATE TABLE tiempo_verbal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(64) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE pregunta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    frase1 VARCHAR(255) NOT NULL,
    frase2 VARCHAR(255) NOT NULL,
    tiempo_verbal1_id INT NOT NULL,
    tiempo_verbal2_id INT NOT NULL,
    afirmativa BOOLEAN NOT NULL,
    verbo_id INT NOT NULL,
    explicacion_correcta VARCHAR(255),
    FOREIGN KEY (tiempo_verbal1_id) REFERENCES tiempo_verbal(id),
    FOREIGN KEY (tiempo_verbal2_id) REFERENCES tiempo_verbal(id),
    FOREIGN KEY (verbo_id) REFERENCES verbo(id)
);

-- Opcional: tabla para almacenar exámenes generados
CREATE TABLE examen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE examen_pregunta (
    examen_id INT NOT NULL,
    pregunta_id INT NOT NULL,
    PRIMARY KEY (examen_id, pregunta_id),
    FOREIGN KEY (examen_id) REFERENCES examen(id),
    FOREIGN KEY (pregunta_id) REFERENCES pregunta(id)
);

CREATE TABLE resultado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    examen_id INT NOT NULL,
    pregunta_id INT NOT NULL,
    respuesta_usuario VARCHAR(255) NOT NULL,
    correcta BOOLEAN NOT NULL,
    explicacion VARCHAR(255),
    explicacion_correcta VARCHAR(255),
    FOREIGN KEY (examen_id) REFERENCES examen(id),
    FOREIGN KEY (pregunta_id) REFERENCES pregunta(id)
);
