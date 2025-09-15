# 🚀 Automatización con GitHub Actions y Docker

Este documento describe la configuración completa de automatización para el proyecto back-pensionados, incluyendo GitHub Actions para CI/CD y containerización con Docker.

## 📋 Tabla de Contenidos

1. [Visión General](#-visión-general)
2. [GitHub Actions Workflows](#-github-actions-workflows)
3. [Containerización con Docker](#-containerización-con-docker)
4. [Despliegue Automatizado](#-despliegue-automatizado)
5. [Configuración de Entornos](#️-configuración-de-entornos)
6. [Monitoreo y Logging](#-monitoreo-y-logging)
7. [Seguridad](#-seguridad)
8. [Guía de Uso](#-guía-de-uso)

---

## 🔍 Visión General

La automatización implementada incluye:

- **CI/CD Pipeline completo** con GitHub Actions
- **Containerización** con Docker y Docker Compose
- **Escaneo de seguridad** automatizado
- **Despliegue multi-entorno** (desarrollo, staging, producción)
- **Monitoreo** con Spring Boot Actuator
- **Gestión de secretos** para producción

---

## 🔄 GitHub Actions Workflows

### 1. CI/CD Pipeline Principal (`.github/workflows/ci-cd.yml`)

**Triggers:**
- Push a `main` o `develop`
- Pull requests a `main` o `develop`
- Releases publicados

**Jobs:**
- **Test**: Ejecuta pruebas unitarias e integración
- **Security**: Escaneo de seguridad con OWASP y CodeQL
- **Build**: Construcción de JAR y imagen Docker
- **Deploy Staging**: Despliegue automático a staging (rama develop)
- **Deploy Production**: Despliegue automático a producción (releases)
- **Notify**: Notificaciones de resultados

### 2. Actualizaciones de Dependencias (`.github/workflows/dependency-updates.yml`)

**Triggers:**
- Programado: Lunes a las 9 AM UTC
- Manual

**Funciones:**
- Detecta actualizaciones de dependencias
- Crea PR automático con actualizaciones seguras
- Ejecuta auditoría de seguridad
- Crea issues para vulnerabilidades

### 3. Seguridad Docker (`.github/workflows/docker-security.yml`)

**Triggers:**
- Cambios en Dockerfile o docker-compose
- Programado: Diario a las 2 AM UTC
- Manual

**Funciones:**
- Escaneo de vulnerabilidades con Trivy
- Validación de Dockerfile con Hadolint
- Pruebas de docker-compose

---

## 🐳 Containerización con Docker

### Estructura de Archivos

```
├── Dockerfile                 # Imagen principal de la aplicación
├── docker-compose.yml         # Desarrollo local
├── docker-compose.prod.yml    # Producción
├── deploy.sh                  # Script de despliegue
├── docker/
│   ├── mysql/
│   │   ├── init/
│   │   │   └── 01-init.sql    # Inicialización de BD
│   │   └── production.cnf     # Configuración MySQL producción
│   └── nginx/
│       └── nginx.conf         # Configuración proxy reverso
└── secrets/                   # Secretos para producción (git-ignored)
    ├── mysql_root_password.txt
    ├── mysql_password.txt
    └── jwt_secret.txt
```

### Características del Docker Setup

**Multi-stage Build:**
- Etapa de construcción con Maven
- Imagen runtime optimizada con JRE
- Usuario no-root para seguridad

**Servicios incluidos:**
- **App**: Aplicación Spring Boot
- **MySQL**: Base de datos
- **Adminer**: Gestión de BD (desarrollo)
- **Nginx**: Proxy reverso (producción)

---

## 🚀 Despliegue Automatizado

### Script de Despliegue (`deploy.sh`)

```bash
# Desarrollo
./deploy.sh dev

# Staging
./deploy.sh staging

# Producción
./deploy.sh production

# Ver estado
./deploy.sh status

# Ver logs
./deploy.sh logs

# Limpiar
./deploy.sh cleanup
```

### Características del Script

- **Validaciones previas**: Docker, docker-compose, entorno
- **Backup automático**: Base de datos antes de despliegue
- **Health checks**: Verificación de salud de servicios
- **Rollback automático**: En caso de falla
- **Logging**: Registro detallado de operaciones

---

## ⚙️ Configuración de Entornos

### Desarrollo (`docker-compose.yml`)

- MySQL sin SSL
- Logs detallados habilitados
- Adminer para gestión de BD
- Secrets como variables de entorno

### Producción (`docker-compose.prod.yml`)

- MySQL con SSL
- Logs optimizados
- Nginx como proxy reverso
- Secrets desde archivos seguros
- Configuraciones de rendimiento

### Variables de Entorno

| Variable | Desarrollo | Producción | Descripción |
|----------|------------|------------|-------------|
| `SPRING_PROFILES_ACTIVE` | default | production | Perfil de Spring |
| `SPRING_JPA_SHOW_SQL` | true | false | Mostrar SQL en logs |
| `JAVA_OPTS` | básico | optimizado | Opciones JVM |

---

## 📊 Monitoreo y Logging

### Spring Boot Actuator

Endpoints habilitados:
- `/api/actuator/health` - Estado de la aplicación
- `/api/actuator/info` - Información de la aplicación
- `/api/actuator/metrics` - Métricas de rendimiento
- `/api/actuator/prometheus` - Métricas para Prometheus

### Health Checks

**Docker:**
```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/api/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 5
```

**GitHub Actions:**
- Tests automáticos con MySQL
- Reportes de cobertura
- Notificaciones de fallas

---

## 🔒 Seguridad

### Medidas Implementadas

1. **Escaneo de Dependencias**: OWASP Dependency Check
2. **Análisis de Código**: GitHub CodeQL
3. **Escaneo de Contenedores**: Trivy
4. **Validación de Dockerfile**: Hadolint
5. **Gestión de Secretos**: Archivos seguros para producción
6. **Usuario no-root**: En contenedores
7. **Proxy reverso**: Nginx con headers de seguridad

### Configuración de Secretos

**Para producción**, crear archivos en `secrets/`:

```bash
# Contraseña root de MySQL
echo "super_secure_password" > secrets/mysql_root_password.txt

# Contraseña de usuario de aplicación
echo "app_user_password" > secrets/mysql_password.txt

# Secreto JWT (mínimo 32 caracteres)
echo "jwt_secret_very_long_and_secure" > secrets/jwt_secret.txt
```

---

## 📚 Guía de Uso

### Configuración Inicial

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Cuotas-Partes/back-pensionados.git
   cd back-pensionados
   ```

2. **Configurar secretos (solo producción)**
   ```bash
   mkdir secrets
   # Crear archivos de secretos como se describe arriba
   ```

3. **Despliegue local de desarrollo**
   ```bash
   ./deploy.sh dev
   ```

### Flujo de Desarrollo

1. **Crear rama de feature**
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```

2. **Desarrollar y hacer commit**
   ```bash
   git add .
   git commit -m "feat: nueva funcionalidad"
   ```

3. **Push y crear PR**
   ```bash
   git push origin feature/nueva-funcionalidad
   # Crear PR en GitHub
   ```

4. **GitHub Actions ejecutará automáticamente:**
   - Tests
   - Escaneo de seguridad
   - Build
   - Validaciones

### Despliegue a Producción

1. **Merge a main**
2. **Crear release**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   # Crear release en GitHub
   ```
3. **GitHub Actions desplegará automáticamente**

### URLs de Acceso

**Desarrollo:**
- API: http://localhost:8080/api
- Swagger: http://localhost:8080/api/swagger-ui/index.html
- Health: http://localhost:8080/api/actuator/health
- Adminer: http://localhost:8081

**Producción:**
- API: https://your-domain.com/api
- Swagger: https://your-domain.com/api/swagger-ui/index.html
- Health: https://your-domain.com/api/actuator/health

### Comandos Útiles

```bash
# Ver estado de contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f app

# Conectar a base de datos
docker-compose exec mysql mysql -u root -p

# Ejecutar tests
./mvnw test

# Construir JAR local
./mvnw clean package

# Escaneo de seguridad
./mvnw org.owasp:dependency-check-maven:check
```

### Solución de Problemas

**Problema: Aplicación no inicia**
```bash
# Ver logs detallados
./deploy.sh logs

# Verificar salud de servicios
./deploy.sh status
```

**Problema: Base de datos no conecta**
```bash
# Verificar que MySQL esté corriendo
docker-compose ps mysql

# Verificar logs de MySQL
docker-compose logs mysql
```

**Problema: Puertos ocupados**
```bash
# Limpiar contenedores
./deploy.sh cleanup

# Verificar puertos
netstat -tulpn | grep :8080
```

---

## 🤝 Contribución

1. Fork del repositorio
2. Crear rama de feature
3. Hacer cambios siguiendo las convenciones
4. Ejecutar tests localmente
5. Crear Pull Request
6. Esperar aprobación de CI/CD

---

## 📞 Soporte

Para problemas o preguntas:

1. Revisar logs con `./deploy.sh logs`
2. Verificar estado con `./deploy.sh status`
3. Consultar documentación de Spring Boot
4. Crear issue en GitHub con detalles completos

---

**¡La automatización está lista! 🎉**

El proyecto ahora cuenta con un pipeline completo de CI/CD, containerización y despliegue automatizado.