/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     7/04/2025 8:05:52 p. m.                      */
/*==============================================================*/

-- 1️⃣ Desactiva validaciones de claves foráneas por si hay relaciones abiertas
SET FOREIGN_KEY_CHECKS = 0;

-- 2️⃣ Elimina la base de datos completa (sin tocar information_schema)
DROP DATABASE IF EXISTS cuotapartes_pensionados_db;

-- 3️⃣ La vuelves a crear vacía
CREATE DATABASE cuotapartes_pensionados_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 4️⃣ Actívala
USE cuotapartes_pensionados_db;

-- 5️⃣ Reactiva validaciones
SET FOREIGN_KEY_CHECKS = 1;


/*==============================================================*/
/* Table: ROL                                               */
/*==============================================================*/
CREATE TABLE ROL (
   id BIGINT NOT NULL AUTO_INCREMENT,
   nombre VARCHAR(60) NOT NULL,
   activo BOOLEAN NOT NULL DEFAULT TRUE,
   creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   actualizado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (id),
   UNIQUE (nombre)
);
/*==============================================================*/
/* Table: USUARIO                                               */
/*==============================================================*/
CREATE TABLE USUARIO (
   id BIGINT NOT NULL AUTO_INCREMENT,
   nombre VARCHAR(100) NOT NULL,
   apellido VARCHAR(100) NOT NULL,
   /*email VARCHAR(100) NOT NULL,*/
   username VARCHAR(100) NOT NULL,
   password VARCHAR(200) NOT NULL,


   rol_id BIGINT NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (rol_id) REFERENCES ROL(id)

);

/*==============================================================*/
/* Table: PERSONA                                               */
/*==============================================================*/
CREATE TABLE PERSONA (

   idPersona BIGINT NOT NULL AUTO_INCREMENT,              -- 1. Nuevo ID para la persona (llave primaria)
   numeroIdentificacion BIGINT NOT NULL,                  -- 2. Renombrado desde numeroIdPersona
   tipoIdentificacion VARCHAR(50) NOT NULL,               -- 3. Renombrado desde tipoIdPersona
   nombrePersona VARCHAR(300) NOT NULL,
   apellidosPersona VARCHAR(300) NOT NULL,
   cargo VARCHAR(300),
   dependencia VARCHAR(300),
   fechaNacimientoPersona DATE NOT NULL,
   fechaExpedicionDocumentoIdPersona DATE NOT NULL,
   estadoPersona VARCHAR(50) NOT NULL,
   generoPersona VARCHAR(50),
   telefonoPersona BIGINT,
   estadoCivil VARCHAR(50) NOT NULL,                      -- 4. Nuevo campo Estado Civil
   fechaDefuncionPersona DATE,
   PRIMARY KEY (idPersona),                               -- 5. Se establece el nuevo ID como llave primaria
   UNIQUE KEY uk_identificacion (tipoIdentificacion, numeroIdentificacion) -- 6. Se unifican los campos de identificacion
);

/*==============================================================*/
/* Table: ENTIDAD                                               */
/*==============================================================*/
CREATE TABLE ENTIDAD (
   idEntidad BIGINT NOT NULL AUTO_INCREMENT,
   nitEntidad BIGINT NOT NULL,
   idPersonaEncargado BIGINT,
   tipoEntidad VARCHAR(50) DEFAULT 'PÚBLICA',  -- Ej: 'PÚBLICA', 'PRIVADA', 'FONDO', 'UNIVERSIDAD'
   nombreEntidad VARCHAR(100) NOT NULL,
   direccionEntidad VARCHAR(100) NOT NULL,
   emailEntidad VARCHAR(100) NOT NULL,
   telefonoEntidad BIGINT NOT NULL,
   estadoEntidad VARCHAR(50) NOT NULL,
   PRIMARY KEY (idEntidad),
   UNIQUE KEY uk_nit (nitEntidad, tipoEntidad),
   FOREIGN KEY (idPersonaEncargado) REFERENCES PERSONA(idPersona)
);

/*==============================================================*/
/* Table: IPC                                                   */
/*==============================================================*/
CREATE TABLE IPC (
   fechaIPC INT NOT NULL,
   valorIPC DECIMAL(5,2) NOT NULL,
   PRIMARY KEY (fechaIPC)
);

/*==============================================================*/
/* Table: PENSIONADO                                            */
/*==============================================================*/
CREATE TABLE PENSIONADO (
   idPensionado BIGINT NOT NULL AUTO_INCREMENT,
   idPersona BIGINT NOT NULL, -- Cambiado de numeroIdPersona
   idEntidad BIGINT NOT NULL,
   fechaInicioPension DATE,
   fechaIngreso DATE,
   valorInicialPension DECIMAL (19,0) NOT NULL,
   resolucionPension  VARCHAR(200) NOT NULL,
   tipoPension VARCHAR(50) DEFAULT 'VEJEZ', -- Ej: 'VEJEZ', 'INVALIDEZ', 'SOBREVIVIENTES'
   totalDiasTrabajo BIGINT,
   aplicarIPCPrimerPeriodo BOOLEAN NOT NULL DEFAULT FALSE,
   PRIMARY KEY (idPensionado), -- Cambiado de numeroIdPersona
   FOREIGN KEY (idPersona) REFERENCES PERSONA(idPersona), -- Apunta a la nueva llave primaria
   FOREIGN KEY (idEntidad) REFERENCES ENTIDAD(idEntidad)
);


/*==============================================================*/
/* Table: TRABAJO
👉 TRABAJO No debe representar un contrato puntual, por ello se creo la tabla CONTRATOS
Esta tabla representa una relación global entre una persona y una entidad 
en la que haya prestado servicio, que agrupa sus contratos y resume los días totales   
| idTrabajo | idPersona | nitEntidad | diasDeServicio | periodoInicio | periodoFin |
| --------- | --------- | ---------- | -------------- | ------------- | ---------- |
| 1         | 45        | 800123456  | 3200           | 1987-01-01    | 2002-12-31 |
En ese rango, la persona puede haber tenido 10 contratos diferentes, 
pero todos suman esos días con esa entidad. 
Esta tabla se debe llenar por cálculo o trigger con base en los contratos 
No se relaciona directamente con cada contrato, sino que se actualiza a partir de ellos.                                         */
/*==============================================================*/
CREATE TABLE TRABAJO (
   idTrabajo BIGINT NOT NULL AUTO_INCREMENT,
   idPersona BIGINT NOT NULL, -- Cambiado de numeroIdPersona
   idEntidad BIGINT NOT NULL,
   fechaInicio DATE NOT NULL,
   fechaFin DATE,
   diasDeServicio BIGINT NOT NULL,
   ultimoCargo VARCHAR(100),
   observaciones VARCHAR(200),
   PRIMARY KEY (idTrabajo),
   FOREIGN KEY (idPersona) REFERENCES PERSONA(idPersona), -- Apunta a la nueva llave primaria de Persona
   FOREIGN KEY (idEntidad) REFERENCES ENTIDAD(idEntidad),
   UNIQUE KEY uk_trabajo_persona_entidad (idPersona, idEntidad, fechaInicio)
);

/*==============================================================*/
/* Table: SUCESOR                                               */
/*==============================================================*/
CREATE TABLE SUCESOR (
   idPersona BIGINT NOT NULL, -- Cambiado de numeroIdPersona
   idPensionado BIGINT NOT NULL, -- Renombrado por claridad (era numeroIdPensionado)
   fechaInicioSucesion DATE NOT NULL,
   porcentajePension DOUBLE NOT NULL,
   PRIMARY KEY (idPersona), -- Cambiado de numeroIdPersona
   FOREIGN KEY (idPersona) REFERENCES PERSONA(idPersona), -- Apunta a la nueva llave primaria
   FOREIGN KEY (idPensionado) REFERENCES PENSIONADO(idPensionado) -- Apunta a la llave primaria de Pensionado
);

/*==============================================================*/
/* Table: CUOTA_PARTE                                           */
/*==============================================================*/
CREATE TABLE CUOTA_PARTE (
   idCuotaParte BIGINT NOT NULL AUTO_INCREMENT,
   idTrabajo BIGINT NOT NULL,
   idEntidad BIGINT NOT NULL,
   idPensionado BIGINT NOT NULL,
   valorCuotaParte DECIMAL (19,2) NOT NULL,
   porcentajeCuotaParte DECIMAL(5,4) NOT NULL,
   fechaGeneracion DATE NOT NULL,
   notas VARCHAR(200) NOT NULL,
   estadoCuotaParte VARCHAR(50) DEFAULT 'PENDIENTE', -- Nuevo: estado (pendiente, pagada, anulada, etc.)
   cuotaParteTotal DECIMAL (19,2),
   PRIMARY KEY (idCuotaParte),
   FOREIGN KEY (idTrabajo) REFERENCES TRABAJO(idTrabajo),
   FOREIGN KEY (idEntidad) REFERENCES ENTIDAD(idEntidad),
   FOREIGN KEY (idPensionado) REFERENCES PENSIONADO(idPensionado),
   UNIQUE KEY uk_cuota_trabajo_entidad (idTrabajo, idEntidad)
);

/*==============================================================*/
/* Table: PERIODO                                               */
/*==============================================================*/
CREATE TABLE PERIODO (
   idPeriodo BIGINT NOT NULL AUTO_INCREMENT,
   fechaIPC INT NOT NULL,
   idCuotaParte BIGINT NOT NULL,
   fechaInicioPeriodo DATE NOT NULL,
   fechaFinPeriodo DATE NOT NULL,
   resolucionPeriodo VARCHAR(300),
   fechaExpedicion DATE,
   estado VARCHAR(50) DEFAULT 'ACTIVO',
   numeroMesadas DECIMAL(5,2) NOT NULL,
   valorPension DECIMAL (19,0) NOT NULL,
   cuotaParteMensual DECIMAL (19,0) NOT NULL,
   cuotaParteTotalAnio DECIMAL (19,0) NOT NULL,
   incrementoLey476 DECIMAL (19,2),
   PRIMARY KEY (idPeriodo),
   FOREIGN KEY (fechaIPC) REFERENCES IPC(fechaIPC),
   FOREIGN KEY (idCuotaParte) REFERENCES CUOTA_PARTE(idCuotaParte)
);



/*==============================================================*/
/* Table: ROL_ACCION                                               */
/*==============================================================*/
CREATE TABLE ROL_ACCION (
   rol_id BIGINT NOT NULL,
   accion ENUM(
     'EJECUCION_PAGOS',
     'REGISTRO_PENSIONADO',
     'PAGO_CUOTA_PARTE',
     'GENERAR_REPORTE',
     'CONSULTAR_HISTORIAL'
   ) NOT NULL,
   PRIMARY KEY (rol_id, accion),
   FOREIGN KEY (rol_id) REFERENCES ROL(id)
     ON UPDATE CASCADE
     ON DELETE CASCADE
);
/*==============================================================*/
/* Table: LOG_CAMBIO                                               */
/*==============================================================*/
CREATE TABLE LOG_CAMBIO (
   id BIGINT NOT NULL AUTO_INCREMENT,
   entidad VARCHAR(80) NOT NULL,
   accion ENUM('CREAR','ACTUALIZAR','ELIMINAR','CONSULTAR') NOT NULL,
   valor_anterior JSON NULL,
   valor_nuevo JSON NULL,
   fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   usuario_id BIGINT NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (usuario_id) REFERENCES USUARIO(id)
     ON UPDATE CASCADE
     ON DELETE CASCADE,
   INDEX idx_log_usuario (usuario_id),
   INDEX idx_log_fecha (fecha)
);

CREATE TABLE EVENTO (
   idEvento BIGINT NOT NULL AUTO_INCREMENT,         -- Identificador único del evento
   idPersona BIGINT,                                -- Persona afectada (puede ser pensionado o sucesor)
   idPensionado BIGINT,                             -- Pensionado relacionado con el evento
   tipoEvento VARCHAR(50) NOT NULL,                 -- Ej: DEFUNCION, SUSTITUCION, RELIQUIDACION
   fechaEvento DATETIME NOT NULL,                   -- Fecha y hora del evento
   usuario VARCHAR(100) NOT NULL,                   -- Usuario que ejecutó la acción
   tablaAfectada VARCHAR(100) NOT NULL,             -- Ej: "PENSIONADO", "CUOTAPARTE"
   descripcion TEXT,                                -- Detalles adicionales del evento
   PRIMARY KEY (idEvento),
   FOREIGN KEY (idPersona) REFERENCES PERSONA(idPersona),
   FOREIGN KEY (idPensionado) REFERENCES PENSIONADO(idPensionado)
);
CREATE TABLE HISTORICO_LIQUIDACION_POR_COBRAR (

    idLiquidacion BIGINT NOT NULL AUTO_INCREMENT,
    idPensionado BIGINT NOT NULL,
    idEntidad BIGINT NOT NULL,
    fechaInicioPeriodo DATE NOT NULL,
    fechaFinPeriodo DATE NOT NULL,
    numeroMesadas DECIMAL(5,2) NOT NULL,
    valorPension DECIMAL(19,2) NOT NULL,
    valorCuotaParteMensual DECIMAL(19,2) NOT NULL,
    cuotaParteTotalAnio DECIMAL(19,2) NOT NULL,
    porcentajeCuotaParte DECIMAL(5,2) NOT NULL,
    porcentajeIncrementoAnual DECIMAL(5,2),
    incrementoAdicionalLey476 DECIMAL(19,2),
    valorInicialPension DECIMAL(19,2) NOT NULL,
    fechaInicioPension DATE,
    resolucionPension VARCHAR(200),
    fechaFallecimientoPensionado DATE,
    nombreSustituto VARCHAR(150),
    documentoSustituto VARCHAR(20),
    notas VARCHAR(500),
    estadoLiquidacion ENUM('PENDIENTE','COBRADA','ANULADA','EN_TRAMITE') NOT NULL,
    fechaRegistro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuarioRegistro VARCHAR(50) NOT NULL,
    PRIMARY KEY (idLiquidacion),
    CONSTRAINT fk_historico_pensionado
    FOREIGN KEY (idPensionado) REFERENCES PENSIONADO(idPensionado),
    CONSTRAINT fk_historico_entidad
    FOREIGN KEY (idEntidad) REFERENCES ENTIDAD(idEntidad)
);
