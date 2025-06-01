-- Insertar Usuario por Defecto
INSERT INTO USUARIO (id, apellido, nombre, password, username)
VALUES (1, 'Unicauca', 'pensiones', '$2a$10$R6MoChK7sDHuSTrON3BC5.jENlU5fn/tMRP0v7frkmiCMfDafgKJK', 'pensiones@unicauca.edu.co');

-- Insertar Entidades
INSERT INTO ENTIDAD (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
VALUES (8911500319, 'Universidad del Cauca', 'Calle 5 No. 4-70 (Popayán - Cauca', 'rectoria@unicauca.edu.co', 8209900, "Activa");

INSERT INTO ENTIDAD (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
VALUES 
(9004567281, 'Hospital San José', 'Carrera 10 No. 15-45, Popayán', 'contacto@hsanjose.com', 8220011, 'Activa'),
(8600123456, 'Alcaldía de Popayán', 'Calle 8 No. 7-30, Popayán', 'alcaldia@popayan.gov.co', 8220022, 'Activa'),
(9001234567, 'Gobernación del Cauca', 'Calle 4 No. 3-52, Popayán', 'info@cauca.gov.co', 8220033, 'Activa'),
(8300123123, 'Colegio La Salle', 'Avenida 2 No. 12-40, Popayán', 'secretaria@lasalle.edu.co', 8220044, 'Activa');

-- INSERTAR DATOS IPC
-- Datos IPC 31/12/1955 - 30/4/2025
INSERT INTO IPC (fechaIPC, valorIPC) VALUES
(1955, 2.03), (1956, 7.91), (1957, 20.69), (1958, 7.98), (1959, 7.81),(1960, 7.35), (1961, 5.74), (1962, 6.30), (1963, 33.60), (1964, 8.80),
(1965, 14.44), (1966, 12.86), (1967, 7.17), (1968, 6.51), (1969, 8.63),(1970, 6.58), (1971, 14.03), (1972, 13.99), (1973, 24.08), (1974, 26.35),
(1975, 17.77), (1976, 25.76), (1977, 28.71), (1978, 18.42), (1979, 28.80),(1980, 25.85), (1981, 26.36), (1982, 24.03), (1983, 16.64), (1984, 18.28),
(1985, 22.45), (1986, 20.95), (1987, 24.02), (1988, 28.12), (1989, 26.12),(1990, 32.36), (1991, 26.82), (1992, 25.13), (1993, 22.60), (1994, 22.59),
(1995, 19.46), (1996, 21.63), (1997, 17.68), (1998, 16.70), (1999, 9.23),(2000, 8.75), (2001, 7.65), (2002, 6.99), (2003, 6.49), (2004, 5.50),
(2005, 4.85), (2006, 4.48), (2007, 5.69), (2008, 7.67), (2009, 2.00),(2010, 3.17), (2011, 3.73), (2012, 2.44), (2013, 1.94), (2014, 3.66),
(2015, 6.77), (2016, 5.75), (2017, 4.09), (2018, 3.18), (2019, 3.80),(2020, 1.61), (2021, 5.62), (2022, 13.12), (2023, 9.28), (2024, 5.20), (2025, 5.16);

-- Insertar Pensionado
INSERT INTO PERSONA (numeroIdPersona, tipoIdPersona, nombrePersona, apellidosPersona, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona, fechaDefuncionPersona)
VALUES (1, 'CC', 'Juan', 'Perez', '1960-01-10', '1980-02-15', 'Activo', 'Masculino', NULL);

INSERT INTO PENSIONADO (numeroIdPersona, fechaInicioPension, valorInicialPension, resolucionPension, totalDiasTrabajo, aplicarIPCPrimerPeriodo, nitEntidad)
VALUES (1, '2020-07-01', 2500000.00, 'RES-001-2020', 12500, false, 8911500319);

-- Insertar Trabajos del Pensionado
INSERT INTO TRABAJO (diasDeServicio, numeroIdPersona, nitEntidad)
VALUES (7300, 1, 8911500319); -- Universidad del Cauca (20 años)

INSERT INTO TRABAJO (diasDeServicio, numeroIdPersona, nitEntidad)
VALUES (1825, 1, 9004567281); -- Hospital San José (5 años)

INSERT INTO TRABAJO (diasDeServicio, numeroIdPersona, nitEntidad)
VALUES (3300, 1, 8600123456); -- Alcaldía de Popayán (aproximadamente 9 años)

-- Insertar Sucesor
INSERT INTO PERSONA (numeroIdPersona, tipoIdPersona, nombrePersona, apellidosPersona, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona, fechaDefuncionPersona)
VALUES (123456789, 'CC', 'Maria', 'Lopez', '1985-05-15', '2003-06-20', 'Activo', 'Femenino', NULL);

INSERT INTO SUCESOR (numeroIdPersona, fechaInicioSucesion, porcentajePension, numeroIdPensionado)
VALUES (123456789, '2023-01-01', 50.0, 1);
