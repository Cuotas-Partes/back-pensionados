--Insertar Usuario por Defecto
INSERT INTO USUARIO (id, apellido, email, nombre, password, username)
VALUES (1, 'Unicauca', 'pensiones@unicauca.edu.co', 'pensiones', '$2a$10$R6MoChK7sDHuSTrON3BC5.jENlU5fn/tMRP0v7frkmiCMfDafgKJK', 'pensiones@unicauca.edu.co');



INSERT INTO ENTIDAD (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
VALUES (8911500319, 'Unicauca', 'Calle 5 No. 4-70 (Popayán - Cauca', 'rectoria@unicauca.edu.co', 8209900, "Activa");

-- Insertar más entidades para el historial laboral
INSERT INTO ENTIDAD (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
VALUES 
(9004567281, 'Hospital San José', 'Carrera 10 No. 15-45, Popayán', 'contacto@hsanjose.com', 8220011, 'Activa'),
(8600123456, 'Alcaldía de Popayán', 'Calle 8 No. 7-30, Popayán', 'alcaldia@popayan.gov.co', 8220022, 'Activa'),
(9001234567, 'Gobernación del Cauca', 'Calle 4 No. 3-52, Popayán', 'info@cauca.gov.co', 8220033, 'Activa'),
(8300123123, 'Colegio La Salle', 'Avenida 2 No. 12-40, Popayán', 'secretaria@lasalle.edu.co', 8220044, 'Activa');

-- Insertar 5 personas
INSERT INTO PERSONA (numeroIdPersona, tipoIdPersona, nombrePersona, apellidosPersona, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona)
VALUES 
(1001000000, 'Cedula', 'Carlos', 'Gomez Perez', '1955-03-12', '2010-05-20', 'Activo', 'Masculino'),
(1002000000, 'Cedula', 'Maria', 'Lopez Ruiz', '1960-07-25', '2012-08-15', 'Activo', 'Femenino'),
(1003000000, 'Cedula', 'Jorge', 'Martinez Diaz', '1948-11-05', '2005-10-30', 'Activo', 'Masculino'),
(1004000000, 'Cedula', 'Ana', 'Rodriguez Soto', '1952-09-18', '2008-04-22', 'Activo', 'Femenino'),
(1005000000, 'Cedula', 'Luis', 'Sanchez Mora', '1958-02-14', '2015-03-10', 'Activo', 'Masculino');

-- Insertar 5 pensionados asociados a la entidad Unicauca (nit 8911500319)
INSERT INTO PENSIONADO (numeroIdPersona, nitEntidad, fechaInicioPension, valorInicialPension, resolucionPension, totalDiasTrabajo)
VALUES 
(1001000000, 8911500319, '2018-01-01', 2500000, 'RES-2018-001', 6935),
(1002000000, 8300123123, '2019-05-15', 1800000, 'RES-2019-045', 4015),
(1003000000, 8911500319, '2015-03-20', 3000000, 'RES-2015-012', 7665),
(1004000000, 9004567281, '2020-10-10', 2100000, 'RES-2020-087', 4745),
(1005000000, 9001234567, '2017-12-05', 2750000, 'RES-2017-032', 3650);

-- Insertar registros de trabajo para los pensionados
-- Cada pensionado tiene su trabajo principal en Unicauca (ya registrado en la tabla PENSIONADO)
-- Y trabajos adicionales en otras entidades a lo largo de su vida laboral

-- Trabajos para Carlos Gomez (1001000000)
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1001000000, 9004567281, 1825), -- Trabajó ~5 años en el Hospital San José
(1001000000, 8600123456, 2190); -- Trabajó ~6 años en la Alcaldía de Popayán

-- Trabajos para Maria Lopez (1002000000)
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1002000000, 8300123123, 2555), -- Trabajó ~7 años en el Colegio La Salle
(1002000000, 9001234567, 1460); -- Trabajó ~4 años en la Gobernación del Cauca

-- Trabajos para Jorge Martinez (1003000000) 
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1003000000, 8600123456, 2190), -- Trabajó ~6 años en la Alcaldía de Popayán
(1003000000, 8911500319, 2920), -- Trabajó ~8 años en Unicauca
(1003000000, 9001234567, 1460), -- Trabajó ~4 años en la Gobernación del Cauca
(1003000000, 8300123123, 1095); -- Trabajó ~3 años en el Colegio La Salle

-- Trabajos para Ana Rodriguez (1004000000)
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1004000000, 9004567281, 2920), -- Trabajó ~8 años en el Hospital San José
(1004000000, 8300123123, 1825); -- Trabajó ~5 años en el Colegio La Salle

-- Trabajos para Luis Sanchez (1005000000)
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1005000000, 9001234567, 2555), -- Trabajó ~7 años en la Gobernación del Cauca
(1005000000, 9004567281, 1095); -- Trabajó ~3 años en el Hospital San José

--INSERTAR DATOS IPC
--Datos IPC 31/12/1955 - 30/4/2025
INSERT INTO IPC (fechaIPC, valorIPC) VALUES
(1955, 2.03), (1956, 7.91), (1957, 20.69), (1958, 7.98), (1959, 7.81),(1960, 7.35), (1961, 5.74), (1962, 6.30), (1963, 33.60), (1964, 8.80),
(1965, 14.44), (1966, 12.86), (1967, 7.17), (1968, 6.51), (1969, 8.63),(1970, 6.58), (1971, 14.03), (1972, 13.99), (1973, 24.08), (1974, 26.35),
(1975, 17.77), (1976, 25.76), (1977, 28.71), (1978, 18.42), (1979, 28.80),(1980, 25.85), (1981, 26.36), (1982, 24.03), (1983, 16.64), (1984, 18.28),
(1985, 22.45), (1986, 20.95), (1987, 24.02), (1988, 28.12), (1989, 26.12),(1990, 32.36), (1991, 26.82), (1992, 25.13), (1993, 22.60), (1994, 22.59),
(1995, 19.46), (1996, 21.63), (1997, 17.68), (1998, 16.70), (1999, 9.23),(2000, 8.75), (2001, 7.65), (2002, 6.99), (2003, 6.49), (2004, 5.50),
(2005, 4.85), (2006, 4.48), (2007, 5.69), (2008, 7.67), (2009, 2.00),(2010, 3.17), (2011, 3.73), (2012, 2.44), (2013, 1.94), (2014, 3.66),
(2015, 6.77), (2016, 5.75), (2017, 4.09), (2018, 3.18), (2019, 3.80),(2020, 1.61), (2021, 5.62), (2022, 13.12), (2023, 9.28), (2024, 5.20), (2025, 5.16);

-- Insertar un periodo
INSERT INTO PERIODO (idPeriodo, fechaIPC, fechaInicioPeriodo, fechaFinPeriodo, numeroMesadas, cuotaParteTotalPeriodo) VALUES(1, 2023, '2023-01-01', '2023-12-31', 2, 10.145);

INSERT INTO PERIODO (idPeriodo, fechaIPC, fechaInicioPeriodo, fechaFinPeriodo, numeroMesadas, cuotaParteTotalPeriodo) VALUES(2, 1983, '1983-01-01', '1983-12-31', 2, 14.145);