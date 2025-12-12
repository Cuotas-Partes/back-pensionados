

-- Insertar roles (solo si no existen)
INSERT INTO rol (nombre, activo, creado_en, actualizado_en) 
SELECT 'ADMIN', TRUE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'ADMIN');

INSERT INTO rol (nombre, activo, creado_en, actualizado_en) 
SELECT 'INVITADO', TRUE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'INVITADO');

-- Acciones para rol ADMIN (solo si no existen)
INSERT INTO rol_accion (rol_id, accion) 
SELECT r.id, 'CREAR' FROM rol r 
WHERE r.nombre='ADMIN' 
AND NOT EXISTS (SELECT 1 FROM rol_accion ra WHERE ra.rol_id = r.id AND ra.accion = 'CREAR');

INSERT INTO rol_accion (rol_id, accion) 
SELECT r.id, 'CONSULTAR' FROM rol r 
WHERE r.nombre='ADMIN' 
AND NOT EXISTS (SELECT 1 FROM rol_accion ra WHERE ra.rol_id = r.id AND ra.accion = 'CONSULTAR');

INSERT INTO rol_accion (rol_id, accion) 
SELECT r.id, 'ACTUALIZAR' FROM rol r 
WHERE r.nombre='ADMIN' 
AND NOT EXISTS (SELECT 1 FROM rol_accion ra WHERE ra.rol_id = r.id AND ra.accion = 'ACTUALIZAR');

INSERT INTO rol_accion (rol_id, accion) 
SELECT r.id, 'ELIMINAR' FROM rol r 
WHERE r.nombre='ADMIN' 
AND NOT EXISTS (SELECT 1 FROM rol_accion ra WHERE ra.rol_id = r.id AND ra.accion = 'ELIMINAR');

-- Acciones para rol INVITADO (solo si no existen)
INSERT INTO rol_accion (rol_id, accion) 
SELECT r.id, 'CONSULTAR' FROM rol r 
WHERE r.nombre='INVITADO' 
AND NOT EXISTS (SELECT 1 FROM rol_accion ra WHERE ra.rol_id = r.id AND ra.accion = 'CONSULTAR');

-- Insertar Usuario por Defecto (solo si no existen)
INSERT INTO usuario (apellido, nombre, password, username, rol_id, createdAt, updatedAt, estado) 
SELECT 'unicauca', 'admin', '$2a$10$MIrmvVP1vJ9bbEJjtufrR.5nx2fcrLNJFT5PJyT7SpoxcKYtgblCK', 'admin@unicauca.edu.co', 1,
  NOW(),
  NOW(),
  'Activo'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'admin@unicauca.edu.co');

INSERT INTO usuario (apellido, nombre, password, username, rol_id, createdAt, updatedAt, estado) 
SELECT 'unicauca', 'invitado', '$2a$10$9PhCjFGoYcGm2C4/QlpsSOdt6iEG9e/Srme3WlTDPBJ35CO2EcLI.', 'invitado@unicauca.edu.co', 2,
  NOW(),
  NOW(),
  'Activo'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'invitado@unicauca.edu.co');

-- Entidades (solo si no existen)
INSERT INTO entidad (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
SELECT 8911500319, 'Universidad del Cauca', 'Calle 5 No. 4-70 (Popayán - Cauca)', 'rectoria@unicauca.edu.co', 8209900, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nitEntidad = 8911500319);

INSERT INTO entidad (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
SELECT 9004567281, 'Hospital San José', 'Carrera 10 No. 15-45, Popayán', 'contacto@hsanjose.com', 8200972, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nitEntidad = 9004567281);

INSERT INTO entidad (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
SELECT 8600123456, 'Alcaldía de Popayán', 'Calle 8 No. 7-30, Popayán', 'alcaldia@popayan.gov.co', 3214965013, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nitEntidad = 8600123456);

INSERT INTO entidad (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
SELECT 9001234567, 'Gobernación del Cauca', 'Calle 4 No. 3-52, Popayán', 'info@cauca.gov.co', 3145261209, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nitEntidad = 9001234567);

INSERT INTO entidad (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
SELECT 8300123123, 'Colegio La Salle', 'Avenida 2 No. 12-40, Popayán', 'secretaria@lasalle.edu.co', 8201548, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nitEntidad = 8300123123);

INSERT INTO entidad (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
SELECT 8300159161, 'FONCEP', 'Carrera 30 No. 25-90, Bogotá D.C.', 'atencionalciudadano@foncep.gov.co', 6013358000, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nitEntidad = 8300159161);

INSERT INTO entidad (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
SELECT 8903990011, 'Universidad del Valle', 'Calle 13 No. 100-00, Cali', 'comunicaciones@correounivalle.edu.co', 6023212100, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nitEntidad = 8903990011);

INSERT INTO entidad (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
SELECT 8915002154, 'Hospital Universitario de Caldas', 'Calle 48 No. 27A-80, Manizales', 'info@hospitalcaldas.gov.co', 6068782500, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM entidad WHERE nitEntidad = 8915002154);

-- INSERTAR DATOS IPC (solo si no existen)
-- Datos IPC 31/12/1955 - 30/4/2025
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1955, 2.03 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1955);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1956, 7.91 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1956);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1957, 20.69 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1957);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1958, 7.98 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1958);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1959, 7.81 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1959);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1960, 7.35 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1960);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1961, 5.74 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1961);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1962, 6.30 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1962);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1963, 33.60 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1963);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1964, 8.80 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1964);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1965, 14.44 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1965);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1966, 12.86 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1966);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1967, 7.17 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1967);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1968, 6.51 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1968);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1969, 8.63 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1969);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1970, 6.58 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1970);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1971, 14.03 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1971);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1972, 13.99 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1972);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1973, 24.08 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1973);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1974, 26.35 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1974);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1975, 17.77 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1975);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1976, 25.76 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1976);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1977, 28.71 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1977);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1978, 18.42 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1978);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1979, 28.80 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1979);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1980, 25.85 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1980);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1981, 26.36 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1981);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1982, 24.03 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1982);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1983, 16.64 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1983);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1984, 18.28 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1984);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1985, 22.45 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1985);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1986, 20.95 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1986);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1987, 24.02 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1987);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1988, 28.12 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1988);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1989, 26.12 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1989);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1990, 32.36 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1990);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1991, 26.82 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1991);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1992, 25.13 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1992);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1993, 22.60 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1993);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1994, 22.59 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1994);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1995, 19.46 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1995);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1996, 21.63 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1996);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1997, 17.68 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1997);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1998, 16.70 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1998);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 1999, 9.23 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 1999);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2000, 8.75 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2000);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2001, 7.65 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2001);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2002, 6.99 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2002);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2003, 6.49 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2003);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2004, 5.50 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2004);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2005, 4.85 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2005);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2006, 4.48 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2006);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2007, 5.69 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2007);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2008, 7.67 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2008);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2009, 2.00 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2009);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2010, 3.17 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2010);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2011, 3.73 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2011);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2012, 2.44 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2012);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2013, 1.94 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2013);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2014, 3.66 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2014);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2015, 6.77 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2015);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2016, 5.75 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2016);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2017, 4.09 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2017);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2018, 3.18 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2018);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2019, 3.80 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2019);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2020, 1.61 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2020);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2021, 5.62 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2021);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2022, 13.12 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2022);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2023, 9.28 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2023);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2024, 5.20 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2024);
INSERT INTO ipc (fechaIPC, valorIPC) 
SELECT 2025, 5.16 WHERE NOT EXISTS (SELECT 1 FROM ipc WHERE fechaIPC = 2025);