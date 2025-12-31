-- Inserir usuário admin (ignora se já existir)
-- Nome: Administrador
-- Email: admin@admin.com
-- Senha: admin123 (BCrypt)

INSERT IGNORE INTO `users` (
  `id`,
  `name`,
  `email`,
  `password`,
  `perfil`
) VALUES (
  'admin',
  'Administrador',
  'admin@admin.com',
  '$2a$10$hI46iyrwXjxOsvR6VcE6h.vVQMcNzDrQjvGFJgkQdO6y4nCQ7l/gG',
  'ADMIN'
);
