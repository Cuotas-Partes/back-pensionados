-- Initial database setup for pensionados application
CREATE DATABASE IF NOT EXISTS cuotapartes_pensionados_db;
USE cuotapartes_pensionados_db;

-- Create user for application
CREATE USER IF NOT EXISTS 'pensionados_user'@'%' IDENTIFIED BY 'pensionados_pass';
GRANT ALL PRIVILEGES ON cuotapartes_pensionados_db.* TO 'pensionados_user'@'%';

-- Set timezone
SET time_zone = '-05:00';  -- Colombia timezone

-- Flush privileges
FLUSH PRIVILEGES;