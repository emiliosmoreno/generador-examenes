-- Borrado de todas las tablas para inicializar los datos
DELETE FROM resultado;
DELETE FROM examen;
DELETE FROM pregunta;
DELETE FROM verbo;
DELETE FROM tiempo_verbal;

-- Poblar la base de datos con datos de ejemplo para el generador de exámenes de inglés

-- Verbos (solo los de lista_verbos.txt)
INSERT INTO verbo (infinitivo, pasado_simple, participio_pasado, significado) VALUES
('be', 'was/were', 'been', 'ser/estar'),
('become', 'became', 'become', 'llegar a ser'),
('begin', 'began', 'begun', 'empezar'),
('break', 'broke', 'broken', 'romper'),
('bring', 'brought', 'brought', 'traer'),
('buy', 'bought', 'bought', 'comprar'),
('catch', 'caught', 'caught', 'coger/agarrar'),
('choose', 'chose', 'chosen', 'elegir'),
('come', 'came', 'come', 'venir'),
('cut', 'cut', 'cut', 'cortar'),
('do', 'did', 'done', 'hacer'),
('drink', 'drank', 'drunk', 'beber'),
('drive', 'drove', 'driven', 'conducir'),
('eat', 'ate', 'eaten', 'comer'),
('fall', 'fell', 'fallen', 'caer/caerse'),
('feed', 'fed', 'fed', 'alimentar'),
('feel', 'felt', 'felt', 'sentir'),
('find', 'found', 'found', 'encontrar'),
('fly', 'flew', 'flown', 'volar'),
('forget', 'forgot', 'forgotten', 'olvidar'),
('freeze', 'froze', 'frozen', 'congelar'),
('get', 'got', 'got', 'conseguir/recibir'),
('give', 'gave', 'given', 'dar'),
('go', 'went', 'gone', 'ir'),
('grow', 'grew', 'grown', 'crecer/cultivar'),
('hang', 'hung', 'hung', 'colgar'),
('have', 'had', 'had', 'tener'),
('hear', 'heard', 'heard', 'oir'),
('hit', 'hit', 'hit', 'golpear'),
('keep', 'kept', 'kept', 'guardar'),
('know', 'knew', 'known', 'saber o conocer'),
('lay', 'laid', 'laid', 'poner/extender'),
('learn', 'learnt', 'learnt', 'aprender'),
('leave', 'left', 'left', 'dejar marcharse'),
('let', 'let', 'let', 'permitir/dejar'),
('build', 'built', 'built', 'construir'),
('meet', 'met', 'met', 'conocer a/reunirse con'),
('pay', 'paid', 'paid', 'pagar'),
('put', 'put', 'put', 'poner'),
('read', 'read', 'read', 'leer'),
('ride', 'rode', 'ridden', 'montar'),
('ring', 'rang', 'rung', 'llamar'),
('run', 'ran', 'run', 'correr'),
('say', 'said', 'said', 'decir'),
('see', 'saw', 'seen', 'ver'),
('sell', 'sold', 'sold', 'vender'),
('send', 'sent', 'sent', 'enviar'),
('shine', 'shone', 'shone', 'brillar'),
('sing', 'sang', 'sung', 'cantar'),
('sit', 'sat', 'sat', 'sentarse'),
('sleep', 'slept', 'slept', 'dormir'),
('speak', 'spoke', 'spoken', 'hablar'),
('spend', 'spent', 'spent', 'gastar/emplear'),
('sweep', 'swept', 'swept', 'barrer'),
('swim', 'swam', 'swum', 'nadar'),
('take', 'took', 'taken', 'coger'),
('teach', 'taught', 'taught', 'enseñar'),
('tell', 'told', 'told', 'decir/contar'),
('think', 'thought', 'thought', 'pensar'),
('understand', 'understood', 'understood', 'comprender/entender'),
('win', 'won', 'won', 'ganar'),
('write', 'wrote', 'written', 'escribir');

-- Tiempos verbales
INSERT INTO tiempo_verbal (nombre, descripcion) VALUES
('Presente Simple', 'Acciones habituales o verdades generales'),
('Pasado Simple', 'Acciones completadas en el pasado'),
('Presente Perfecto', 'Acciones que ocurrieron en un tiempo no especificado antes de ahora'),
('Futuro Simple', 'Acciones que ocurrirán en el futuro'),
('Presente Continuo', 'Acciones que están ocurriendo ahora'),
('Pasado Continuo', 'Acciones que estaban ocurriendo en un momento específico del pasado');

-- Examen de ejemplo (frases)
INSERT INTO examen (fecha_creacion, tipo) VALUES (NOW(), 'FRASES');
-- Examen de ejemplo (verbos 3 tiempos)
INSERT INTO examen (fecha_creacion, tipo) VALUES (NOW(), 'VERBO3');
-- Examen de ejemplo (verbos 2 tiempos)
INSERT INTO examen (fecha_creacion, tipo) VALUES (NOW(), 'VERBO2');

-- Eliminar todas las preguntas existentes
delete from pregunta;

-- Insertar 50 nuevas preguntas alternando el hueco en la primera o segunda frase
INSERT INTO pregunta (frase1, frase2, tiempo_verbal1_id, tiempo_verbal2_id, afirmativa, verbo_id, explicacion_correcta) VALUES
('I ___ to the gym every morning.', 'No, I didn''t go yesterday.', 1, 2, 1, 10, '<b>Gramática:</b> Presente simple y pasado simple negativo. <b>Respuesta correcta:</b> go.'),
('She eats breakfast every day.', 'Yesterday, she didn''t ___ breakfast.', 1, 2, 1, 13, '<b>Gramática:</b> Presente simple y pasado simple negativo. <b>Respuesta correcta:</b> eat.'),
('They ___ football on Sundays.', 'Last Sunday, they didn''t play.', 1, 2, 1, 60, '<b>Gramática:</b> Presente simple y pasado simple negativo. <b>Respuesta correcta:</b> play.'),
('He ___ a letter now.', 'He wrote one yesterday.', 5, 2, 1, 62, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> write.'),
('We ___ a movie last night.', 'We watch movies every week.', 2, 1, 1, 26, '<b>Gramática:</b> Pasado simple y presente simple. <b>Respuesta correcta:</b> watch.'),
('You ___ English very well.', 'Did you speak French in Paris?', 1, 2, 1, 51, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> speak.'),
('She ___ to school by bus.', 'Yesterday, she went by car.', 1, 2, 1, 10, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> go.'),
('I ___ my homework now.', 'I finished it yesterday.', 5, 2, 1, 25, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> do.'),
('They ___ a new car last month.', 'They buy cars every year.', 2, 1, 1, 6, '<b>Gramática:</b> Pasado simple y presente simple. <b>Respuesta correcta:</b> buy.'),
('He ___ his keys every day.', 'He lost them yesterday.', 1, 2, 1, 39, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> lose.'),
('We ___ to music now.', 'We listened to music yesterday.', 5, 2, 1, 28, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> listen.'),
('She ___ her friends every weekend.', 'She met them last Saturday.', 1, 2, 1, 36, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> meet.'),
('I ___ a book now.', 'I read it last week.', 5, 2, 1, 44, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> read.'),
('They ___ to the beach every summer.', 'They went last year.', 1, 2, 1, 10, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> go.'),
('He ___ a song now.', 'He sang yesterday.', 5, 2, 1, 48, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> sing.'),
('We ___ dinner at 8pm.', 'We ate at 7pm yesterday.', 1, 2, 1, 13, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> eat.'),
('She ___ TV now.', 'She watched TV last night.', 5, 2, 1, 26, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> watch.'),
('I ___ my bike every day.', 'I rode it yesterday.', 1, 2, 1, 38, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> ride.'),
('They ___ a cake now.', 'They baked one yesterday.', 5, 2, 1, 6, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> bake.'),
('He ___ his homework every afternoon.', 'He did it yesterday.', 1, 2, 1, 25, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> do.'),
('We ___ to the park now.', 'We went there yesterday.', 5, 2, 1, 10, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> go.'),
('She ___ her room every Saturday.', 'She cleaned it yesterday.', 1, 2, 1, 24, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> clean.'),
('I ___ a letter now.', 'I wrote one yesterday.', 5, 2, 1, 62, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> write.'),
('They ___ football every weekend.', 'They played last Sunday.', 1, 2, 1, 60, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> play.'),
('He ___ to music now.', 'He listened yesterday.', 5, 2, 1, 28, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> listen.'),
('We ___ our friends every Friday.', 'We met them last week.', 1, 2, 1, 36, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> meet.'),
('She ___ a book now.', 'She read it last night.', 5, 2, 1, 44, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> read.'),
('I ___ to the gym every morning.', 'No, I didn''t go ayer.', 1, 2, 1, 10, '<b>Gramática:</b> Presente simple y pasado simple negativo. <b>Respuesta correcta:</b> go.'),
('They ___ breakfast every day.', 'Yesterday, they didn''t eat.', 1, 2, 1, 13, '<b>Gramática:</b> Presente simple y pasado simple negativo. <b>Respuesta correcta:</b> eat.'),
('He ___ football on Sundays.', 'Last Sunday, he didn''t play.', 1, 2, 1, 60, '<b>Gramática:</b> Presente simple y pasado simple negativo. <b>Respuesta correcta:</b> play.'),
('We ___ a letter now.', 'We wrote one yesterday.', 5, 2, 1, 62, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> write.'),
('She ___ a movie last night.', 'She watches movies every week.', 2, 1, 1, 26, '<b>Gramática:</b> Pasado simple y presente simple. <b>Respuesta correcta:</b> watch.'),
('You ___ English very well.', 'Did you speak French in Paris?', 1, 2, 1, 51, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> speak.'),
('I ___ to school by bus.', 'Yesterday, I went by car.', 1, 2, 1, 10, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> go.'),
('They ___ their homework now.', 'They finished it yesterday.', 5, 2, 1, 25, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> do.'),
('He ___ a new car last month.', 'He buys cars every year.', 2, 1, 1, 6, '<b>Gramática:</b> Pasado simple y presente simple. <b>Respuesta correcta:</b> buy.'),
('We ___ our keys every day.', 'We lost them yesterday.', 1, 2, 1, 39, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> lose.'),
('She ___ to music now.', 'She listened to music yesterday.', 5, 2, 1, 28, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> listen.'),
('I ___ my friends every weekend.', 'I met them last Saturday.', 1, 2, 1, 36, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> meet.'),
('They ___ a book now.', 'They read it last week.', 5, 2, 1, 44, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> read.'),
('He ___ to the beach every summer.', 'He went last year.', 1, 2, 1, 10, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> go.'),
('We ___ a song now.', 'We sang yesterday.', 5, 2, 1, 48, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> sing.'),
('She ___ dinner at 8pm.', 'She ate at 7pm ayer.', 1, 2, 1, 13, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> eat.'),
('I ___ TV now.', 'I watched TV last night.', 5, 2, 1, 26, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> watch.'),
('They ___ my bike every day.', 'They rode it yesterday.', 1, 2, 1, 38, '<b>Gramática:</b> Presente simple y pasado simple. <b>Respuesta correcta:</b> ride.'),
('He ___ a cake now.', 'He baked one yesterday.', 5, 2, 1, 6, '<b>Gramática:</b> Presente continuo y pasado simple. <b>Respuesta correcta:</b> bake.');