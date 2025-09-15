#!/bin/bash

# Deployment script for Pensionados Backend Application
# Usage: ./deploy.sh [environment] [version]
# Environment: dev, staging, production
# Version: optional, defaults to latest

set -e

# Configuration
ENVIRONMENT=${1:-dev}
VERSION=${2:-latest}
APP_NAME="back-pensionados"
COMPOSE_PROJECT_NAME="pensionados-${ENVIRONMENT}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        log_error "Docker is not running. Please start Docker and try again."
        exit 1
    fi
    log_success "Docker is running"
}

# Check if docker-compose is available
check_docker_compose() {
    if ! command -v docker-compose > /dev/null 2>&1; then
        log_error "docker-compose is not installed. Please install it and try again."
        exit 1
    fi
    log_success "docker-compose is available"
}

# Validate environment
validate_environment() {
    case $ENVIRONMENT in
        dev|staging|production)
            log_info "Deploying to environment: $ENVIRONMENT"
            ;;
        *)
            log_error "Invalid environment: $ENVIRONMENT. Use dev, staging, or production."
            exit 1
            ;;
    esac
}

# Create secrets directory if it doesn't exist
setup_secrets() {
    if [ ! -d "secrets" ]; then
        log_warning "Secrets directory not found. Creating example secrets..."
        mkdir -p secrets
        
        echo "change_this_root_password" > secrets/mysql_root_password.txt
        echo "change_this_user_password" > secrets/mysql_password.txt
        echo "change_this_jwt_secret_with_at_least_32_characters" > secrets/jwt_secret.txt
        
        log_warning "Please update the secrets in the secrets/ directory before deploying to production!"
    fi
}

# Deploy development environment
deploy_dev() {
    log_info "Deploying development environment..."
    
    # Stop existing containers
    docker-compose -p $COMPOSE_PROJECT_NAME down -v || true
    
    # Build and start services
    docker-compose -p $COMPOSE_PROJECT_NAME up -d --build
    
    # Wait for services to be healthy
    log_info "Waiting for services to be healthy..."
    sleep 30
    
    # Check service health
    check_service_health
}

# Deploy staging environment
deploy_staging() {
    log_info "Deploying staging environment..."
    
    # Use production compose with staging overrides
    docker-compose -f docker-compose.prod.yml -p $COMPOSE_PROJECT_NAME down || true
    docker-compose -f docker-compose.prod.yml -p $COMPOSE_PROJECT_NAME up -d
    
    # Wait for services to be healthy
    log_info "Waiting for services to be healthy..."
    sleep 60
    
    # Check service health
    check_service_health
}

# Deploy production environment
deploy_production() {
    log_info "Deploying production environment..."
    
    # Validate secrets exist
    if [ ! -f "secrets/mysql_root_password.txt" ] || [ ! -f "secrets/mysql_password.txt" ] || [ ! -f "secrets/jwt_secret.txt" ]; then
        log_error "Production secrets not found. Please create all required secret files."
        exit 1
    fi
    
    # Backup database if exists
    backup_database
    
    # Deploy with production configuration
    docker-compose -f docker-compose.prod.yml -p $COMPOSE_PROJECT_NAME down || true
    docker-compose -f docker-compose.prod.yml -p $COMPOSE_PROJECT_NAME up -d
    
    # Wait for services to be healthy
    log_info "Waiting for services to be healthy..."
    sleep 90
    
    # Check service health
    check_service_health
    
    # Run post-deployment checks
    post_deployment_checks
}

# Check service health
check_service_health() {
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        log_info "Health check attempt $attempt/$max_attempts..."
        
        if curl -f http://localhost:8080/api/actuator/health > /dev/null 2>&1; then
            log_success "Application is healthy!"
            return 0
        fi
        
        sleep 10
        ((attempt++))
    done
    
    log_error "Application health check failed after $max_attempts attempts"
    show_logs
    exit 1
}

# Backup database
backup_database() {
    if docker ps --format "table {{.Names}}" | grep -q "${COMPOSE_PROJECT_NAME}.*mysql"; then
        log_info "Creating database backup..."
        
        local backup_file="backup_$(date +%Y%m%d_%H%M%S).sql"
        
        docker exec "${COMPOSE_PROJECT_NAME}_mysql_1" mysqldump -u root -p"$(cat secrets/mysql_root_password.txt)" cuotapartes_pensionados_db > "backups/$backup_file"
        
        log_success "Database backup created: backups/$backup_file"
    else
        log_info "No existing database found to backup"
    fi
}

# Post-deployment checks
post_deployment_checks() {
    log_info "Running post-deployment checks..."
    
    # Check API endpoints
    if curl -f http://localhost:8080/api/actuator/health > /dev/null 2>&1; then
        log_success "Health endpoint is accessible"
    else
        log_error "Health endpoint is not accessible"
    fi
    
    if curl -f http://localhost:8080/api/swagger-ui/index.html > /dev/null 2>&1; then
        log_success "Swagger UI is accessible"
    else
        log_warning "Swagger UI is not accessible"
    fi
    
    # Check database connectivity
    if docker exec "${COMPOSE_PROJECT_NAME}_app_1" curl -f http://localhost:8080/api/actuator/health > /dev/null 2>&1; then
        log_success "Database connectivity verified"
    else
        log_error "Database connectivity check failed"
    fi
}

# Show container logs
show_logs() {
    log_info "Showing recent container logs..."
    docker-compose -p $COMPOSE_PROJECT_NAME logs --tail=50
}

# Show status
show_status() {
    log_info "Container status:"
    docker-compose -p $COMPOSE_PROJECT_NAME ps
    
    log_info "Application URLs:"
    echo "  - API: http://localhost:8080/api"
    echo "  - Health: http://localhost:8080/api/actuator/health"
    echo "  - Swagger: http://localhost:8080/api/swagger-ui/index.html"
    
    if [ "$ENVIRONMENT" == "dev" ]; then
        echo "  - Adminer: http://localhost:8081"
    fi
}

# Cleanup function
cleanup() {
    log_info "Cleaning up..."
    docker-compose -p $COMPOSE_PROJECT_NAME down
    docker system prune -f
}

# Main deployment logic
main() {
    log_info "Starting deployment of $APP_NAME"
    log_info "Environment: $ENVIRONMENT"
    log_info "Version: $VERSION"
    
    # Pre-deployment checks
    check_docker
    check_docker_compose
    validate_environment
    
    # Create backup directory
    mkdir -p backups
    
    # Deploy based on environment
    case $ENVIRONMENT in
        dev)
            setup_secrets
            deploy_dev
            ;;
        staging)
            setup_secrets
            deploy_staging
            ;;
        production)
            deploy_production
            ;;
    esac
    
    # Show deployment status
    show_status
    
    log_success "Deployment completed successfully!"
}

# Handle script arguments
case "${1:-}" in
    --help|-h)
        echo "Usage: $0 [environment] [version]"
        echo "Environments: dev, staging, production"
        echo "Version: optional, defaults to latest"
        echo ""
        echo "Examples:"
        echo "  $0 dev                 # Deploy development environment"
        echo "  $0 staging v1.0.0      # Deploy staging with specific version"
        echo "  $0 production latest   # Deploy production with latest version"
        echo ""
        echo "Additional commands:"
        echo "  $0 status              # Show current status"
        echo "  $0 logs                # Show container logs"
        echo "  $0 cleanup             # Stop and cleanup containers"
        exit 0
        ;;
    status)
        show_status
        exit 0
        ;;
    logs)
        show_logs
        exit 0
        ;;
    cleanup)
        cleanup
        exit 0
        ;;
    *)
        main
        ;;
esac