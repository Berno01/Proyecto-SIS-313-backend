CREATE TABLE `tiendarepuestos`.`categoria`
(
    `id_categoria` INT NOT NULL AUTO_INCREMENT,
    `nombre_categoria` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id_categoria`)
)
CREATE TABLE `tiendarepuestos`.`repuesto`
(
    `id_repuesto` INT NOT NULL AUTO_INCREMENT,
    `nombre_repuesto` VARCHAR(100) NOT NULL,
    stock_actual INT NOT NULL,
    costo_repuesto DOUBLE NOT NULL,
    precio_unitario_repuesto DOUBLE NOT NULL,
    PRIMARY KEY (`id_repuesto`)
)

CREATE TABLE `tiendarepuestos`.`venta`
(
    id_venta INT NOT NULL AUTO_INCREMENT,
    nombre_cliente VARCHAR(100) NOT NULL,
    fecha_venta DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    descuento_total DOUBLE NOT NULL,
    PRIMARY KEY (`id_venta`)
)

CREATE TABLE `tiendarepuestos`.`compra`
(
    id_compra INT NOT NULL AUTO_INCREMENT,
    nombre_proveedor VARCHAR(100) NOT NULL,
    fecha_compra DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    PRIMARY KEY (`id_compra`)
)

CREATE TABLE `tiendarepuestos`.`detalle_venta`
(
    id_detalle_venta INT NOT NULL AUTO_INCREMENT,
    id_venta INT NOT NULL,
    total DOUBLE NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario_repuesto DOUBLE NOT NULL,
    descuento DOUBLE NOT NULL,
    id_repuesto INT NOT NULL,

    PRIMARY KEY (id_detalle_venta),
    FOREIGN KEY (id_venta) REFERENCES venta(id_venta),
    FOREIGN KEY (id_repuesto) REFERENCES repuesto(id_repuesto)
)

CREATE TABLE `tiendarepuestos`.`detalle_compra`
(
    id_detalle_compra INT NOT NULL AUTO_INCREMENT,
    id_compra INT NOT NULL,
    total DOUBLE NOT NULL,
    cantidad INT NOT NULL,
    costo_repuesto DOUBLE NOT NULL,
    id_repuesto INT NOT NULL,

    PRIMARY KEY (id_detalle_compra),
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra),
    FOREIGN KEY (id_repuesto) REFERENCES repuesto(id_repuesto)
)

CREATE TABLE `tiendarepuestos`.`repuesto-categoria`
(
    id_repuesto_categoria INT NOT NULL AUTO_INCREMENT,
    id_repuesto INT NOT NULL,
    id_categoria INT NOT NULL,
    PRIMARY KEY (id_repuesto_categoria),
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    FOREIGN KEY (id_repuesto) REFERENCES repuesto(id_repuesto),
    UNIQUE KEY `idx_repuesto_categoria_unique` (`id_repuesto`, `id_categoria`)
)

CREATE TABLE `tiendarepuestos`.`usuario`
(
    id_usuario INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY `idx_username_unique` (username)
)

-- Usuario admin por defecto (password: admin123)
-- Hash BCrypt generado para 'admin123': $2a$10$8K1p/a0dL1H3h8MwMvTtFOazLCq7HVMTzL8JJMzQyXKaVCv4Y2Hry
INSERT INTO `tiendarepuestos`.`usuario` (username, password, nombre_completo, rol)
VALUES ('admin', '$2a$10$8K1p/a0dL1H3h8MwMvTtFOazLCq7HVMTzL8JJMzQyXKaVCv4Y2Hry', 'Administrador', 'ADMIN');
