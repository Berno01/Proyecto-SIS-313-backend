-- Script de migración para agregar campos de auditoría a la tabla venta
-- Ejecutar este script en la base de datos tiendarepuestos

ALTER TABLE `tiendarepuestos`.`venta`
ADD COLUMN `created_at` DATETIME NULL COMMENT 'Fecha y hora de creación del registro',
ADD COLUMN `created_by` INT NULL COMMENT 'ID del usuario que creó el registro',
ADD COLUMN `updated_at` DATETIME NULL COMMENT 'Fecha y hora de última actualización',
ADD COLUMN `updated_by` INT NULL COMMENT 'ID del usuario que actualizó el registro';

-- Opcional: Agregar índices para mejorar consultas de auditoría
CREATE INDEX idx_venta_created_at ON `tiendarepuestos`.`venta` (created_at);
CREATE INDEX idx_venta_updated_at ON `tiendarepuestos`.`venta` (updated_at);

-- Nota: Los campos created_at y created_by están protegidos en el código (updatable = false)
-- para prevenir modificaciones accidentales después de la creación inicial.
