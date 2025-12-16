-- ============================================
-- INSERTAR ROLES (solo si no existen)
-- ============================================
INSERT INTO rol (nombre, activo, creado_en, actualizado_en)
SELECT 'ADMIN', TRUE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'ADMIN');

INSERT INTO rol (nombre, activo, creado_en, actualizado_en)
SELECT 'INVITADO', TRUE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'INVITADO');

-- ============================================
-- ACCIONES PARA ROLES (solo si no existen)
-- ============================================
-- Acciones para rol ADMIN
INSERT INTO rolAccion (rol_id, accion)
SELECT r.id, 'CREAR' FROM rol r
WHERE r.nombre='ADMIN'
  AND NOT EXISTS (SELECT 1 FROM rolAccion ra WHERE ra.rol_id = r.id AND ra.accion = 'CREAR');

INSERT INTO rolAccion (rol_id, accion)
SELECT r.id, 'CONSULTAR' FROM rol r
WHERE r.nombre='ADMIN'
  AND NOT EXISTS (SELECT 1 FROM rolAccion ra WHERE ra.rol_id = r.id AND ra.accion = 'CONSULTAR');

INSERT INTO rolAccion (rol_id, accion)
SELECT r.id, 'ACTUALIZAR' FROM rol r
WHERE r.nombre='ADMIN'
  AND NOT EXISTS (SELECT 1 FROM rolAccion ra WHERE ra.rol_id = r.id AND ra.accion = 'ACTUALIZAR');

INSERT INTO rolAccion (rol_id, accion)
SELECT r.id, 'ELIMINAR' FROM rol r
WHERE r.nombre='ADMIN'
  AND NOT EXISTS (SELECT 1 FROM rolAccion ra WHERE ra.rol_id = r.id AND ra.accion = 'ELIMINAR');

-- Acciones para rol INVITADO
INSERT INTO rolAccion (rol_id, accion)
SELECT r.id, 'CONSULTAR' FROM rol r
WHERE r.nombre='INVITADO'
  AND NOT EXISTS (SELECT 1 FROM rolAccion ra WHERE ra.rol_id = r.id AND ra.accion = 'CONSULTAR');

-- ============================================
-- USUARIOS POR DEFECTO (solo si no existen)
-- ============================================
INSERT INTO usuario (apellido, nombre, password, username, rol_id, createdAt, updatedAt, estado)
SELECT 'Unicauca', 'Administrador', '$2a$10$MIrmvVP1vJ9bbEJjtufrR.5nx2fcrLNJFT5PJyT7SpoxcKYtgblCK', 'admin@unicauca.edu.co',
       (SELECT id FROM rol WHERE nombre = 'ADMIN'),
       NOW(),
       NOW(),
       'Activo'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'admin@unicauca.edu.co');

INSERT INTO usuario (apellido, nombre, password, username, rol_id, createdAt, updatedAt, estado)
SELECT 'Unicauca', 'Invitado', '$2a$10$9PhCjFGoYcGm2C4/QlpsSOdt6iEG9e/Srme3WlTDPBJ35CO2EcLI.', 'invitado@unicauca.edu.co',
       (SELECT id FROM rol WHERE nombre = 'INVITADO'),
       NOW(),
       NOW(),
       'Activo'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'invitado@unicauca.edu.co');

-- ============================================
-- ENTIDADES (solo si no existen)
-- ============================================
INSERT INTO entidad (nit, name, address, email, phone, responsible_officer, officer_position, estado, created_at, updated_at)
SELECT '8911500319', 'Universidad del Cauca', 'Calle 5 No. 4-70 (Popayán - Cauca)', 'rectoria@unicauca.edu.co', '8209900', 'Rector', 'Rector', 'Activo', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nit = '8911500319');

INSERT INTO entidad (nit, name, address, email, phone, responsible_officer, officer_position, estado, created_at, updated_at)
SELECT '9004567281', 'Hospital San José', 'Carrera 10 No. 15-45, Popayán', 'contacto@hsanjose.com', '8200972', 'Director', 'Director Administrativo', 'Activo', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nit = '9004567281');

INSERT INTO entidad (nit, name, address, email, phone, responsible_officer, officer_position, estado, created_at, updated_at)
SELECT '8600123456', 'Alcaldía de Popayán', 'Calle 8 No. 7-30, Popayán', 'alcaldia@popayan.gov.co', '3214965013', 'Alcalde', 'Alcalde Municipal', 'Activo', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nit = '8600123456');

INSERT INTO entidad (nit, name, address, email, phone, responsible_officer, officer_position, estado, created_at, updated_at)
SELECT '9001234567', 'Gobernación del Cauca', 'Calle 4 No. 3-52, Popayán', 'info@cauca.gov.co', '3145261209', 'Gobernador', 'Gobernador del Cauca', 'Activo', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nit = '9001234567');

INSERT INTO entidad (nit, name, address, email, phone, responsible_officer, officer_position, estado, created_at, updated_at)
SELECT '8300123123', 'Colegio La Salle', 'Avenida 2 No. 12-40, Popayán', 'secretaria@lasalle.edu.co', '8201548', 'Rector', 'Rector del Colegio', 'Activo', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nit = '8300123123');

INSERT INTO entidad (nit, name, address, email, phone, responsible_officer, officer_position, estado, created_at, updated_at)
SELECT '8300159161', 'FONCEP', 'Carrera 30 No. 25-90, Bogotá D.C.', 'atencionalciudadano@foncep.gov.co', '6013358000', 'Director General', 'Director General', 'Activo', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nit = '8300159161');

INSERT INTO entidad (nit, name, address, email, phone, responsible_officer, officer_position, estado, created_at, updated_at)
SELECT '8903990011', 'Universidad del Valle', 'Calle 13 No. 100-00, Cali', 'comunicaciones@correounivalle.edu.co', '6023212100', 'Rector', 'Rector', 'Activo', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nit = '8903990011');

INSERT INTO entidad (nit, name, address, email, phone, responsible_officer, officer_position, estado, created_at, updated_at)
SELECT '8915002154', 'Hospital Universitario de Caldas', 'Calle 48 No. 27A-80, Manizales', 'info@hospitalcaldas.gov.co', '6068782500', 'Director', 'Director Médico', 'Activo', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nit = '8915002154');

-- ============================================
-- PERSONAS (solo si no existen)
-- ============================================
INSERT INTO persona (numeroIdentificacion, tipoIdentificacion, nombrePersona, apellidosPersona, estadoCivil, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona, discapacidad)
SELECT 123456789, 'CEDULA_CIUDADANIA', 'Juan', 'Pérez', 'SOLTERO', '1980-05-10', '2000-01-01', 'Activo', 'MASCULINO', NULL
    WHERE NOT EXISTS (SELECT 1 FROM persona WHERE numeroIdentificacion = 123456789);

-- ============================================
-- PENSIONADOS (solo si no existen)
-- ============================================

-- Pensionado 1: Con resoluciones y sustituto
INSERT INTO pensionado (cedula, fecha_expedicion_cedula, nombre, apellidos, fecha_nacimiento, telefono, correo, entidad_jubilacion, entity_nit, entity_id, dias_trabajados_entidad, dias_totales_trabajados, porcentaje_cuota, tipo_jubilacion, valor_pension, estado, tiene_sustituto, cuotas_pendientes, total_pendiente, created_at, updated_at)
SELECT '1234567892', '1995-06-10', 'Juan', 'Pérez Gómez', '1960-03-15', '3124567890', 'juan.perez@email.com', 'Universidad del Cauca', '8911500319', 1, 8500, 12000, 75.50, 'Vejez', 3500000.00, 'Activo', true, 0, 0.00, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM pensionado WHERE cedula = '1234567892');

-- Pensionado 2: Fallecido con 2 sustitutos activos
INSERT INTO pensionado (cedula, fecha_expedicion_cedula, nombre, apellidos, fecha_nacimiento, telefono, correo, entidad_jubilacion, entity_nit, entity_id, dias_trabajados_entidad, dias_totales_trabajados, porcentaje_cuota, tipo_jubilacion, valor_pension, estado, fecha_fallecimiento, tiene_sustituto, cuotas_pendientes, total_pendiente, created_at, updated_at)
SELECT '10234567', '1975-03-15', 'Carlos Alberto', 'Rodríguez Gómez', '1950-08-20', '3201234567', 'carlos.rodriguez@email.com', 'Universidad del Cauca', '8911500319', 1, 10950, 12775, 85.50, 'Vejez', 4500000.00, 'Fallecido', '2023-05-10', TRUE, 0, 0.00, '2020-01-15 10:30:00', '2023-05-10 14:20:00'
    WHERE NOT EXISTS (SELECT 1 FROM pensionado WHERE cedula = '10234567');

-- Pensionado 3: Activo sin sustitutos
INSERT INTO pensionado (cedula, fecha_expedicion_cedula, nombre, apellidos, fecha_nacimiento, telefono, correo, entidad_jubilacion, entity_nit, entity_id, dias_trabajados_entidad, dias_totales_trabajados, porcentaje_cuota, tipo_jubilacion, valor_pension, estado, tiene_sustituto, cuotas_pendientes, total_pendiente, created_at, updated_at)
SELECT '20345678', '1980-06-22', 'María Elena', 'Pérez Martínez', '1955-12-10', '3109876543', 'maria.perez@email.com', 'Universidad del Cauca', '8911500319', 1, 11680, 11680, 100.00, 'Vejez', 5200000.00, 'Activo', FALSE, 0, 0.00, '2018-03-20 09:15:00', '2024-12-01 16:45:00'
    WHERE NOT EXISTS (SELECT 1 FROM pensionado WHERE cedula = '20345678');

-- Pensionado 4: Fallecido con 1 sustituto activo
INSERT INTO pensionado (cedula, fecha_expedicion_cedula, nombre, apellidos, fecha_nacimiento, telefono, correo, entidad_jubilacion, entity_nit, entity_id, dias_trabajados_entidad, dias_totales_trabajados, porcentaje_cuota, tipo_jubilacion, valor_pension, estado, fecha_fallecimiento, tiene_sustituto, cuotas_pendientes, total_pendiente, created_at, updated_at)
SELECT '30456789', '1978-09-10', 'Jorge Luis', 'Sánchez Rojas', '1952-04-25', '3157654321', 'jorge.sanchez@email.com', 'Universidad del Cauca', '8911500319', 1, 9125, 13140, 69.45, 'Vejez', 3800000.00, 'Fallecido', '2024-02-18', TRUE, 0, 0.00, '2019-06-10 11:00:00', '2024-02-18 10:30:00'
    WHERE NOT EXISTS (SELECT 1 FROM pensionado WHERE cedula = '30456789');

-- Pensionado 5: Fallecido con sustituto que también falleció
INSERT INTO pensionado (cedula, fecha_expedicion_cedula, nombre, apellidos, fecha_nacimiento, telefono, correo, entidad_jubilacion, entity_nit, entity_id, dias_trabajados_entidad, dias_totales_trabajados, porcentaje_cuota, tipo_jubilacion, valor_pension, estado, fecha_fallecimiento, tiene_sustituto, cuotas_pendientes, total_pendiente, created_at, updated_at)
SELECT '50678901', '1976-02-14', 'Roberto', 'Díaz Castro', '1948-11-03', '3145678901', 'roberto.diaz@email.com', 'Universidad del Cauca', '8911500319', 1, 12410, 12410, 100.00, 'Vejez', 5500000.00, 'Fallecido', '2022-08-15', TRUE, 0, 0.00, '2017-09-01 10:00:00', '2022-08-15 15:45:00'
    WHERE NOT EXISTS (SELECT 1 FROM pensionado WHERE cedula = '50678901');

-- ============================================
-- RESOLUCIONES (solo si no existen)
-- ============================================

-- Resoluciones para Pensionado 1 (Juan - cedula 1234567892)
INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2020-001', '2020-01-15', 3000000.00, 'VIGENTE', 'PENSION_INICIAL', p.idPersona,
       JSON_OBJECT('tipoPension', 'VEJEZ', 'entidadJubilacion', 'Universidad del Cauca', 'diasTrabajados', 8500, 'porcentajeCuota', 75.50),
       NOW()
FROM pensionado p
WHERE p.cedula = '1234567892'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2020-001');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2023-014', '2023-05-10', 3500000.00, 'VIGENTE', 'AJUSTE_PENSION', p.idPersona,
       JSON_OBJECT('valorAnterior', 3000000.00, 'valorNuevo', 3500000.00, 'incremento', 500000.00, 'porcentajeIncremento', 16.67, 'motivo', 'Ajuste por IPC'),
       NOW()
FROM pensionado p
WHERE p.cedula = '1234567892'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2023-014');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2023-015', '2023-06-01', 1750000.00, 'VIGENTE', 'NOMBRAMIENTO_SUSTITUTO', p.idPersona,
       JSON_OBJECT('nombreSustituto', 'María Pérez Gómez', 'cedulaSustituto', '987654321', 'porcentajeSustituto', 50.00, 'parentesco', 'Cónyuge'),
       NOW()
FROM pensionado p
WHERE p.cedula = '1234567892'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2023-015');

-- Resoluciones para Pensionado 2 (Carlos - cedula 10234567)
INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2020-002', '2020-01-15', 4000000.00, 'VIGENTE', 'PENSION_INICIAL', p.idPersona,
       JSON_OBJECT('tipoPension', 'VEJEZ', 'entidadJubilacion', 'Universidad del Cauca', 'diasTrabajados', 10950, 'porcentajeCuota', 85.50),
       NOW()
FROM pensionado p
WHERE p.cedula = '10234567'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2020-002');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2021-045', '2021-06-10', 4500000.00, 'VIGENTE', 'AUMENTO_PENSION', p.idPersona,
       JSON_OBJECT('valorAnterior', 4000000.00, 'valorNuevo', 4500000.00, 'incremento', 500000.00, 'porcentajeIncremento', 12.50, 'motivo', 'Ajuste por IPC anual'),
       NOW()
FROM pensionado p
WHERE p.cedula = '10234567'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2021-045');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2023-078', '2023-06-01', 2250000.00, 'VIGENTE', 'NOMBRAMIENTO_SUSTITUTO', p.idPersona,
       JSON_OBJECT('nombreSustituto', 'Gloria Patricia Rodríguez de López', 'cedulaSustituto', '1098765432', 'porcentajeSustituto', 50.00, 'parentesco', 'Cónyuge', 'motivoSustitucion', 'Fallecimiento del pensionado'),
       NOW()
FROM pensionado p
WHERE p.cedula = '10234567'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2023-078');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2023-079', '2023-06-01', 2250000.00, 'VIGENTE', 'NOMBRAMIENTO_SUSTITUTO', p.idPersona,
       JSON_OBJECT('nombreSustituto', 'Diana Carolina Rodríguez Jiménez', 'cedulaSustituto', '1087654321', 'porcentajeSustituto', 50.00, 'parentesco', 'Hija', 'motivoSustitucion', 'Fallecimiento del pensionado'),
       NOW()
FROM pensionado p
WHERE p.cedula = '10234567'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2023-079');

-- Resoluciones para Pensionado 3 (María - cedula 20345678)
INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2018-032', '2018-03-20', 4800000.00, 'VIGENTE', 'PENSION_INICIAL', p.idPersona,
       JSON_OBJECT('tipoPension', 'VEJEZ', 'entidadJubilacion', 'Universidad del Cauca', 'diasTrabajados', 11680, 'porcentajeCuota', 100.00),
       NOW()
FROM pensionado p
WHERE p.cedula = '20345678'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2018-032');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2022-104', '2022-01-15', 5200000.00, 'VIGENTE', 'AUMENTO_PENSION', p.idPersona,
       JSON_OBJECT('valorAnterior', 4800000.00, 'valorNuevo', 5200000.00, 'incremento', 400000.00, 'porcentajeIncremento', 8.33, 'motivo', 'Ajuste por IPC acumulado'),
       NOW()
FROM pensionado p
WHERE p.cedula = '20345678'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2022-104');

-- Resoluciones para Pensionado 4 (Jorge - cedula 30456789)
INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2019-067', '2019-06-10', 3500000.00, 'VIGENTE', 'PENSION_INICIAL', p.idPersona,
       JSON_OBJECT('tipoPension', 'VEJEZ', 'entidadJubilacion', 'Universidad del Cauca', 'diasTrabajados', 9125, 'porcentajeCuota', 69.45),
       NOW()
FROM pensionado p
WHERE p.cedula = '30456789'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2019-067');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2020-112', '2020-12-01', 3800000.00, 'VIGENTE', 'AUMENTO_PENSION', p.idPersona,
       JSON_OBJECT('valorAnterior', 3500000.00, 'valorNuevo', 3800000.00, 'incremento', 300000.00, 'porcentajeIncremento', 8.57, 'motivo', 'Ajuste anual por IPC'),
       NOW()
FROM pensionado p
WHERE p.cedula = '30456789'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2020-112');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2024-055', '2024-03-15', 3800000.00, 'VIGENTE', 'NOMBRAMIENTO_SUSTITUTO', p.idPersona,
       JSON_OBJECT('nombreSustituto', 'Luz Marina Sánchez de Ramírez', 'cedulaSustituto', '52876543', 'porcentajeSustituto', 100.00, 'parentesco', 'Cónyuge', 'motivoSustitucion', 'Fallecimiento del pensionado'),
       NOW()
FROM pensionado p
WHERE p.cedula = '30456789'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2024-055');

-- Resoluciones para Pensionado 5 (Roberto - cedula 50678901)
INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2017-024', '2017-09-01', 5000000.00, 'VIGENTE', 'PENSION_INICIAL', p.idPersona,
       JSON_OBJECT('tipoPension', 'VEJEZ', 'entidadJubilacion', 'Universidad del Cauca', 'diasTrabajados', 12410, 'porcentajeCuota', 100.00),
       NOW()
FROM pensionado p
WHERE p.cedula = '50678901'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2017-024');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2019-156', '2019-11-15', 5500000.00, 'VIGENTE', 'AUMENTO_PENSION', p.idPersona,
       JSON_OBJECT('valorAnterior', 5000000.00, 'valorNuevo', 5500000.00, 'incremento', 500000.00, 'porcentajeIncremento', 10.00, 'motivo', 'Ajuste por IPC acumulado'),
       NOW()
FROM pensionado p
WHERE p.cedula = '50678901'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2019-156');

INSERT INTO resolucion (numeroResolucion, fechaResolucion, valorResolucion, estado, tipoResolucion, pensionado_id, datos_especificos, createdAt)
SELECT 'RES-2022-095', '2022-09-10', 5500000.00, 'VIGENTE', 'NOMBRAMIENTO_SUSTITUTO', p.idPersona,
       JSON_OBJECT('nombreSustituto', 'Teresa Díaz de González', 'cedulaSustituto', '41765432', 'porcentajeSustituto', 100.00, 'parentesco', 'Cónyuge', 'motivoSustitucion', 'Fallecimiento del pensionado'),
       NOW()
FROM pensionado p
WHERE p.cedula = '50678901'
  AND NOT EXISTS (SELECT 1 FROM resolucion WHERE numeroResolucion = 'RES-2022-095');

-- ============================================
-- SUSTITUTOS/SUCESORES (solo si no existen)
-- ============================================

-- Sustituto 1: María Pérez Gómez (ya existe la persona)
INSERT INTO sucesor (idPersona, numero_documento, tipo_identificacion, nombre_completo, telefono, estado, fecha_inicio, porcentaje_pension, pensionado_sustituido_id, resolucion_nombramiento_id)
SELECT
    per.idPersona,
    987654321,
    'CEDULA_CIUDADANIA',
    'María Pérez Gómez',
    '3101234567',
    'Activo',
    '2023-06-01',
    50.00,
    p.idPersona,
    (SELECT id FROM resolucion WHERE numeroResolucion = 'RES-2023-015')
FROM pensionado p
         CROSS JOIN persona per
WHERE p.cedula = '1234567892'
  AND per.numeroIdentificacion = 123456789
  AND NOT EXISTS (SELECT 1 FROM sucesor WHERE numero_documento = 987654321);

-- Sustituto 2: Gloria Patricia (necesita crear persona primero)
INSERT INTO persona (numeroIdentificacion, tipoIdentificacion, nombrePersona, apellidosPersona, estadoCivil, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona)
SELECT 1098765432, 'CEDULA_CIUDADANIA', 'Gloria Patricia', 'Rodríguez López', 'CASADO', '1975-08-15', '1993-06-10', 'Activo', 'FEMENINO'
    WHERE NOT EXISTS (SELECT 1 FROM persona WHERE numeroIdentificacion = 1098765432);

INSERT INTO sucesor (idPersona, numero_documento, tipo_identificacion, nombre_completo, telefono, estado, fecha_inicio, porcentaje_pension, pensionado_sustituido_id, resolucion_nombramiento_id)
SELECT
    per.idPersona,
    1098765432,
    'CEDULA_CIUDADANIA',
    'Gloria Patricia Rodríguez de López',
    '3209876543',
    'Activo',
    '2023-06-01',
    50.00,
    p.idPersona,
    (SELECT id FROM resolucion WHERE numeroResolucion = 'RES-2023-078')
FROM pensionado p
         CROSS JOIN persona per
WHERE p.cedula = '10234567'
  AND per.numeroIdentificacion = 1098765432
  AND NOT EXISTS (SELECT 1 FROM sucesor WHERE numero_documento = 1098765432);

-- Sustituto 3: Diana Carolina
INSERT INTO persona (numeroIdentificacion, tipoIdentificacion, nombrePersona, apellidosPersona, estadoCivil, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona)
SELECT 1087654321, 'CEDULA_CIUDADANIA', 'Diana Carolina', 'Rodríguez Jiménez', 'SOLTERO', '1995-02-20', '2013-05-15', 'Activo', 'FEMENINO'
    WHERE NOT EXISTS (SELECT 1 FROM persona WHERE numeroIdentificacion = 1087654321);

INSERT INTO sucesor (idPersona, numero_documento, tipo_identificacion, nombre_completo, telefono, estado, fecha_inicio, porcentaje_pension, pensionado_sustituido_id, resolucion_nombramiento_id)
SELECT
    per.idPersona,
    1087654321,
    'CEDULA_CIUDADANIA',
    'Diana Carolina Rodríguez Jiménez',
    '3158765432',
    'Activo',
    '2023-06-01',
    50.00,
    p.idPersona,
    (SELECT id FROM resolucion WHERE numeroResolucion = 'RES-2023-079')
FROM pensionado p
         CROSS JOIN persona per
WHERE p.cedula = '10234567'
  AND per.numeroIdentificacion = 1087654321
  AND NOT EXISTS (SELECT 1 FROM sucesor WHERE numero_documento = 1087654321);

-- Sustituto 4: Luz Marina
INSERT INTO persona (numeroIdentificacion, tipoIdentificacion, nombrePersona, apellidosPersona, estadoCivil, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona)
SELECT 52876543, 'CEDULA_CIUDADANIA', 'Luz Marina', 'Sánchez Ramírez', 'CASADO', '1960-11-30', '1978-07-20', 'Activo', 'FEMENINO'
    WHERE NOT EXISTS (SELECT 1 FROM persona WHERE numeroIdentificacion = 52876543);

INSERT INTO sucesor (idPersona, numero_documento, tipo_identificacion, nombre_completo, telefono, estado, fecha_inicio, porcentaje_pension, pensionado_sustituido_id, resolucion_nombramiento_id)
SELECT
    per.idPersona,
    52876543,
    'CEDULA_CIUDADANIA',
    'Luz Marina Sánchez de Ramírez',
    '3167654321',
    'Activo',
    '2024-03-15',
    100.00,
    p.idPersona,
    (SELECT id FROM resolucion WHERE numeroResolucion = 'RES-2024-055')
FROM pensionado p
         CROSS JOIN persona per
WHERE p.cedula = '30456789'
  AND per.numeroIdentificacion = 52876543
  AND NOT EXISTS (SELECT 1 FROM sucesor WHERE numero_documento = 52876543);

-- Sustituto 5: Teresa (fallecida)
INSERT INTO persona (numeroIdentificacion, tipoIdentificacion, nombrePersona, apellidosPersona, estadoCivil, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona)
SELECT 41765432, 'CEDULA_CIUDADANIA', 'Teresa', 'Díaz González', 'VIUDO', '1952-06-10', '1970-03-15', 'Fallecido', 'FEMENINO'
    WHERE NOT EXISTS (SELECT 1 FROM persona WHERE numeroIdentificacion = 41765432);

INSERT INTO sucesor (idPersona, numero_documento, tipo_identificacion, nombre_completo, telefono, estado, fecha_inicio, fecha_fin, porcentaje_pension, pensionado_sustituido_id, resolucion_nombramiento_id)
SELECT
    per.idPersona,
    41765432,
    'CEDULA_CIUDADANIA',
    'Teresa Díaz de González',
    '3145432109',
    'Fallecido',
    '2022-09-10',
    '2024-11-05',
    100.00,
    p.idPersona,
    (SELECT id FROM resolucion WHERE numeroResolucion = 'RES-2022-095')
FROM pensionado p
         CROSS JOIN persona per
WHERE p.cedula = '50678901'
  AND per.numeroIdentificacion = 41765432
  AND NOT EXISTS (SELECT 1 FROM sucesor WHERE numero_documento = 41765432);

-- ============================================
-- IPC (Índice de Precios al Consumidor)
-- ============================================
INSERT INTO ipc (year, ipc, resolution, resolution_date, estado, resolution_details, createdAt, updatedAt)
SELECT 2018, 3.18, 'Resolución DANE 001-2019', '2019-01-15', TRUE, 'IPC año 2018 según DANE', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE year = 2018);

INSERT INTO ipc (year, ipc, resolution, resolution_date, estado, resolution_details, createdAt, updatedAt)
SELECT 2019, 3.80, 'Resolución DANE 001-2020', '2020-01-15', TRUE, 'IPC año 2019 según DANE', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE year = 2019);

INSERT INTO ipc (year, ipc, resolution, resolution_date, estado, resolution_details, createdAt, updatedAt)
SELECT 2020, 1.61, 'Resolución DANE 001-2021', '2021-01-15', TRUE, 'IPC año 2020 según DANE', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE year = 2020);

INSERT INTO ipc (year, ipc, resolution, resolution_date, estado, resolution_details, createdAt, updatedAt)
SELECT 2021, 5.62, 'Resolución DANE 001-2022', '2022-01-15', TRUE, 'IPC año 2021 según DANE', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE year = 2021);

INSERT INTO ipc (year, ipc, resolution, resolution_date, estado, resolution_details, createdAt, updatedAt)
SELECT 2022, 13.12, 'Resolución DANE 001-2023', '2023-01-15', TRUE, 'IPC año 2022 según DANE', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE year = 2022);

INSERT INTO ipc (year, ipc, resolution, resolution_date, estado, resolution_details, createdAt, updatedAt)
SELECT 2023, 9.28, 'Resolución DANE 001-2024', '2024-01-15', TRUE, 'IPC año 2023 según DANE', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE year = 2023);

INSERT INTO ipc (year, ipc, resolution, resolution_date, estado, resolution_details, createdAt, updatedAt)
SELECT 2024, 5.81, 'Resolución DANE 001-2025', '2025-01-15', TRUE, 'IPC año 2024 según DANE', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE year = 2024);

-- ============================================
-- DTF (Depósito a Término Fijo)
-- ============================================
INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-01', 12.50, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-01');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-02', 12.75, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-02');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-03', 12.80, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-03');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-04', 12.90, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-04');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-05', 13.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-05');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-06', 13.10, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-06');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-07', 13.15, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-07');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-08', 13.20, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-08');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-09', 13.05, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-09');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-10', 12.95, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-10');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-11', 12.85, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-11');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2023-12', 12.80, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2023-12');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-01', 12.60, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-01');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-02', 12.45, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-02');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-03', 12.30, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-03');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-04', 12.15, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-04');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-05', 12.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-05');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-06', 11.85, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-06');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-07', 11.70, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-07');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-08', 11.60, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-08');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-09', 11.50, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-09');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-10', 11.40, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-10');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-11', 11.30, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-11');

INSERT INTO dtf (periodo, valor, estado, createdAt, updatedAt)
SELECT '2024-12', 11.25, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dtf WHERE periodo = '2024-12');

-- ============================================
-- SMMLV Histórico (Salario Mínimo Mensual Legal Vigente)
-- ============================================
INSERT INTO smmlvHistorico (ano, valor, estado, createdAt, updatedAt)
SELECT 2018, 781242.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM smmlvHistorico WHERE ano = 2018);

INSERT INTO smmlvHistorico (ano, valor, estado, createdAt, updatedAt)
SELECT 2019, 828116.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM smmlvHistorico WHERE ano = 2019);

INSERT INTO smmlvHistorico (ano, valor, estado, createdAt, updatedAt)
SELECT 2020, 877803.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM smmlvHistorico WHERE ano = 2020);

INSERT INTO smmlvHistorico (ano, valor, estado, createdAt, updatedAt)
SELECT 2021, 908526.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM smmlvHistorico WHERE ano = 2021);

INSERT INTO smmlvHistorico (ano, valor, estado, createdAt, updatedAt)
SELECT 2022, 1000000.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM smmlvHistorico WHERE ano = 2022);

INSERT INTO smmlvHistorico (ano, valor, estado, createdAt, updatedAt)
SELECT 2023, 1160000.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM smmlvHistorico WHERE ano = 2023);

INSERT INTO smmlvHistorico (ano, valor, estado, createdAt, updatedAt)
SELECT 2024, 1300000.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM smmlvHistorico WHERE ano = 2024);

INSERT INTO smmlvHistorico (ano, valor, estado, createdAt, updatedAt)
SELECT 2025, 1423500.00, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM smmlvHistorico WHERE ano = 2025);
