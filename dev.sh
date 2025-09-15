#!/bin/bash

# Quick development script for Pensionados Backend
# Usage: ./dev.sh [command]

set -e

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

# Show help
show_help() {
    echo "🚀 Pensionados Backend Development Script"
    echo ""
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build        - Build the application with Maven"
    echo "  test         - Run all tests"
    echo "  run          - Run the application locally"
    echo "  docker       - Build Docker image"
    echo "  docker-run   - Run application in Docker"
    echo "  clean        - Clean build artifacts"
    echo "  format       - Format code (if formatter available)"
    echo "  security     - Run security checks"
    echo "  deps         - Show dependency tree"
    echo "  help         - Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 build     # Build the application"
    echo "  $0 test      # Run tests"
    echo "  $0 docker    # Build Docker image"
}

# Build the application
build() {
    log_info "Building application with Maven..."
    ./mvnw clean compile -B
    log_success "Build completed successfully!"
}

# Run tests
test() {
    log_info "Running tests..."
    ./mvnw test -B
    log_success "Tests completed successfully!"
}

# Package the application
package() {
    log_info "Packaging application..."
    ./mvnw clean package -DskipTests -B
    log_success "Package created successfully!"
}

# Run application locally
run() {
    log_info "Starting application locally..."
    log_warning "Make sure MySQL is running on localhost:3306"
    ./mvnw spring-boot:run
}

# Build Docker image
docker_build() {
    log_info "Building Docker image..."
    
    # First, build the JAR
    package
    
    # Then build Docker image
    docker build -t pensionados-app:latest .
    log_success "Docker image built successfully!"
    
    # Show image info
    docker images | grep pensionados-app
}

# Run Docker container
docker_run() {
    log_info "Running application in Docker..."
    
    # Stop existing container if running
    docker stop pensionados-app 2>/dev/null || true
    docker rm pensionados-app 2>/dev/null || true
    
    # Run new container
    docker run -d \
        --name pensionados-app \
        -p 8080:8080 \
        -e SPRING_PROFILES_ACTIVE=docker \
        pensionados-app:latest
    
    log_success "Docker container started!"
    log_info "Application will be available at: http://localhost:8080/api"
    log_info "View logs with: docker logs -f pensionados-app"
}

# Clean build artifacts
clean() {
    log_info "Cleaning build artifacts..."
    ./mvnw clean -B
    
    # Clean Docker artifacts
    docker system prune -f 2>/dev/null || true
    
    log_success "Clean completed!"
}

# Run security checks
security() {
    log_info "Running security checks..."
    ./mvnw org.owasp:dependency-check-maven:check -B
    log_success "Security scan completed! Check target/dependency-check-report.html"
}

# Show dependency tree
deps() {
    log_info "Showing dependency tree..."
    ./mvnw dependency:tree -B
}

# Format code (placeholder)
format() {
    log_warning "Code formatting not configured yet."
    log_info "You can add a formatter like google-java-format or spotless to pom.xml"
}

# Main script logic
main() {
    # Make mvnw executable if needed
    chmod +x mvnw 2>/dev/null || true
    
    case "${1:-help}" in
        build)
            build
            ;;
        test)
            test
            ;;
        package)
            package
            ;;
        run)
            run
            ;;
        docker)
            docker_build
            ;;
        docker-run)
            docker_run
            ;;
        clean)
            clean
            ;;
        security)
            security
            ;;
        deps)
            deps
            ;;
        format)
            format
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            log_error "Unknown command: $1"
            echo ""
            show_help
            exit 1
            ;;
    esac
}

# Run main function
main "$@"