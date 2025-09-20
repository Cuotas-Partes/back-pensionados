DROP TABLE IF EXISTS PERIODO CASCADE;
DROP TABLE IF EXISTS CUOTA_PARTE CASCADE;
DROP TABLE IF EXISTS TRABAJO CASCADE;
DROP TABLE IF EXISTS SUCESOR CASCADE;
DROP TABLE IF EXISTS PENSIONADO CASCADE;
DROP TABLE IF EXISTS IPC CASCADE;
DROP TABLE IF EXISTS ENTIDAD CASCADE;
DROP TABLE IF EXISTS PERSONA CASCADE;
DROP TABLE IF EXISTS USUARIO CASCADE;
DROP TABLE IF EXISTS ROL CASCADE;
DROP TABLE IF EXISTS ROL_ACCION CASCADE;
DROP TABLE IF EXISTS LOG_CAMBIO CASCADE;

-- Tabla ROL
CREATE TABLE ROL (
                     id BIGSERIAL PRIMARY KEY,
                     nombre VARCHAR(60) NOT NULL UNIQUE,
                     activo BOOLEAN NOT NULL DEFAULT TRUE,
                     creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     actualizado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla USUARIO
CREATE TABLE USUARIO (
                         id BIGSERIAL PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         apellido VARCHAR(100) NOT NULL,
                         username VARCHAR(100) NOT NULL,
                         password VARCHAR(200) NOT NULL,
                         rol_id BIGINT NOT NULL REFERENCES ROL(id)
);

-- Tabla PERSONA
CREATE TABLE PERSONA (
                         numeroIdPersona BIGINT PRIMARY KEY,
                         tipoIdPersona VARCHAR(50) NOT NULL,
                         nombrePersona VARCHAR(50) NOT NULL,
                         apellidosPersona VARCHAR(50) NOT NULL,
                         fechaNacimientoPersona DATE NOT NULL,
                         fechaExpedicionDocumentoIdPersona DATE NOT NULL,
                         estadoPersona VARCHAR(50) NOT NULL,
                         generoPersona VARCHAR(50),
                         fechaDefuncionPersona DATE
);

-- Tabla ENTIDAD
CREATE TABLE ENTIDAD (
                         nitEntidad BIGINT PRIMARY KEY,
                         nombreEntidad VARCHAR(100) NOT NULL,
                         direccionEntidad VARCHAR(100) NOT NULL,
                         emailEntidad VARCHAR(100) NOT NULL,
                         telefonoEntidad BIGINT NOT NULL,
                         estadoEntidad VARCHAR(50) NOT NULL
);

-- Tabla IPC
CREATE TABLE IPC (
                     fechaIPC INT PRIMARY KEY,
                     valorIPC NUMERIC(5,2) NOT NULL
);

-- Tabla PENSIONADO
CREATE TABLE PENSIONADO (
                            numeroIdPersona BIGINT PRIMARY KEY REFERENCES PERSONA(numeroIdPersona),
                            nitEntidad BIGINT NOT NULL REFERENCES ENTIDAD(nitEntidad),
                            fechaInicioPension DATE,
                            valorInicialPension NUMERIC (19,0) NOT NULL,
                            resolucionPension  VARCHAR(200) NOT NULL,
                            totalDiasTrabajo BIGINT,
                            aplicarIPCPrimerPeriodo BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tabla TRABAJO
CREATE TABLE TRABAJO (
                         idTrabajo BIGSERIAL PRIMARY KEY,
                         numeroIdPersona BIGINT NOT NULL REFERENCES PENSIONADO(numeroIdPersona),
                         nitEntidad BIGINT NOT NULL REFERENCES ENTIDAD(nitEntidad),
                         diasDeServicio BIGINT NOT NULL
);

-- Tabla SUCESOR
CREATE TABLE SUCESOR (
                         numeroIdPersona BIGINT PRIMARY KEY REFERENCES PERSONA(numeroIdPersona),
                         numeroIdPensionado BIGINT NOT NULL REFERENCES PENSIONADO(numeroIdPersona),
                         fechaInicioSucesion DATE NOT NULL,
                         porcentajePension NUMERIC(5,2) NOT NULL
);

-- Tabla CUOTA_PARTE
CREATE TABLE CUOTA_PARTE (
                             idCuotaParte BIGSERIAL PRIMARY KEY,
                             idTrabajo BIGINT NOT NULL REFERENCES TRABAJO(idTrabajo),
                             valorCuotaParte NUMERIC (19,2) NOT NULL,
                             porcentajeCuotaParte NUMERIC(5,4) NOT NULL,
                             fechaGeneracion DATE,
                             notas VARCHAR(200) NOT NULL,
                             cuotaParteTotal NUMERIC (19,2)
);

-- Tabla PERIODO
CREATE TABLE PERIODO (
                         idPeriodo BIGSERIAL PRIMARY KEY,
                         fechaIPC INT NOT NULL REFERENCES IPC(fechaIPC),
                         idCuotaParte BIGINT NOT NULL REFERENCES CUOTA_PARTE(idCuotaParte),
                         fechaInicioPeriodo DATE NOT NULL,
                         fechaFinPeriodo DATE NOT NULL,
                         numeroMesadas NUMERIC(5,2) NOT NULL,
                         valorPension NUMERIC (19,0) NOT NULL,
                         cuotaParteMensual NUMERIC (19,0) NOT NULL,
                         cuotaParteTotalAnio NUMERIC (19,0) NOT NULL,
                         incrementoLey476 NUMERIC (19,2)
);

-- Tabla ROL_ACCION (ENUM convertido a VARCHAR con CHECK)
CREATE TABLE ROL_ACCION (
                            rol_id BIGINT NOT NULL REFERENCES ROL(id) ON UPDATE CASCADE ON DELETE CASCADE,
                            accion VARCHAR(50) NOT NULL CHECK (accion IN (
                                                                          'EJECUCION_PAGOS',
                                                                          'REGISTRO_PENSIONADO',
                                                                          'PAGO_CUOTA_PARTE',
                                                                          'GENERAR_REPORTE',
                                                                          'CONSULTAR_HISTORIAL'
                                )),
                            PRIMARY KEY (rol_id, accion)
);

-- Tabla LOG_CAMBIO
CREATE TABLE LOG_CAMBIO (
                            id BIGSERIAL PRIMARY KEY,
                            entidad VARCHAR(80) NOT NULL,
                            accion VARCHAR(20) NOT NULL CHECK (accion IN ('CREAR','ACTUALIZAR','ELIMINAR','CONSULTAR')),
                            valor_anterior JSONB,
                            valor_nuevo JSONB,
                            fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            usuario_id BIGINT NOT NULL REFERENCES USUARIO(id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_log_usuario ON LOG_CAMBIO(usuario_id);
CREATE INDEX idx_log_fecha ON LOG_CAMBIO(fecha);
