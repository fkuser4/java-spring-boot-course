INSERT INTO restaurant (name, address, phone_number, email, is_open, rating, delivery_time, max_orders, michelin_stars, description)
VALUES ('Le Gourmet', 'Ulica Pariz 123', '+33 1 23 45 67 89', 'kontakt@legourmet.fr', TRUE, 5, 30, 50, 5, 'Fine dining with French cuisine.'),
       ('Pizzeria Napoli', 'Piazza Roma 56', '+39 081 123 4567', 'info@napoli.it', TRUE, 4, 25, 100, 4, 'Authentic Neapolitan pizza straight from Italy.'),
       ('Sushi House', 'Sakura Street 89', '+81 3-1234-5678', 'sushi@tokyo.jp', FALSE, 5, 20, 80, 3, 'Fresh sushi and sashimi with traditional Japanese experience.'),
       ('Burger Mania', 'Main Street 5', '+1 555 123 4567', 'burger@mania.com', TRUE, 3, 15, 150, 2, 'Juicy burgers with a variety of toppings.'),
       ('Curry Palace', 'Spice Road 7', '+91 22 1234 5678', 'contact@currypalace.in', TRUE, 4, 40, 70, 1, 'Traditional Indian curries with rich spices.');

INSERT INTO working_hour (day_of_week, opening_time, closing_time, restaurant_id)
VALUES ('MONDAY', '09:00:00', '17:00:00', (SELECT id FROM restaurant WHERE name = 'Le Gourmet')),
       ('TUESDAY', '09:00:00', '17:00:00', (SELECT id FROM restaurant WHERE name = 'Le Gourmet')),
       ('MONDAY', '11:00:00', '23:00:00', (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli')),
       ('FRIDAY', '12:00:00', '22:00:00', (SELECT id FROM restaurant WHERE name = 'Sushi House')),
       ('SATURDAY', '10:00:00', '18:00:00', (SELECT id FROM restaurant WHERE name = 'Burger Mania')),
       ('SUNDAY', '12:00:00', '20:00:00', (SELECT id FROM restaurant WHERE name = 'Curry Palace'));

INSERT INTO user_role (role) VALUES ('ROLE_ADMIN');
INSERT INTO user_role (role) VALUES ('ROLE_USER');

INSERT INTO user_info (username, password, name, surname) VALUES
       ('admin', '$2a$10$.5zkpegcPtA6RMEojtbRQeoudnXcjMn5L4i7NougLYghw6Lf6J382', 'Admin', 'Adminovic'),
       ('user',  '$2a$10$IiYdtLHg76p9EQQRD2XbW.NrhGe42StOf.0OihyX3fVyjvEPrsVie', 'User', 'Userovic');

INSERT INTO users_authority (user_id, authority_id)
SELECT u.id, r.id FROM user_info u, user_role r WHERE u.username = 'admin' AND r.role = 'ROLE_ADMIN';

INSERT INTO users_authority (user_id, authority_id)
SELECT u.id, r.id FROM user_info u, user_role r WHERE u.username = 'user' AND r.role = 'ROLE_USER';

INSERT INTO review (title, text, rating, restaurant_id, user_id)
VALUES ('Amazing Experience', 'One of the best meals I ever had.', 5, (SELECT id FROM restaurant WHERE name = 'Le Gourmet'), (SELECT id FROM user_info WHERE username = 'admin')),
       ('Great Pizza', 'Authentic Napoli pizza, really good!', 4, (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli'), (SELECT id FROM user_info WHERE username = 'admin')),
       ('Fresh Sushi', 'Excellent fish and rice balance.', 5, (SELECT id FROM restaurant WHERE name = 'Sushi House'), (SELECT id FROM user_info WHERE username = 'admin')),
       ('Burger Heaven', 'Massive portions, great fries.', 3, (SELECT id FROM restaurant WHERE name = 'Burger Mania'), (SELECT id FROM user_info WHERE username = 'admin'));

INSERT INTO review (title, text, rating, restaurant_id, user_id)
VALUES ('Best Curry in Town', 'Spicy and flavorful.', 4, (SELECT id FROM restaurant WHERE name = 'Curry Palace'), (SELECT id FROM user_info WHERE username = 'user')),
       ('Perfect Service', 'Staff was friendly and quick.', 5, (SELECT id FROM restaurant WHERE name = 'Le Gourmet'), (SELECT id FROM user_info WHERE username = 'user')),
       ('Good Value', 'Decent prices for big portions.', 4, (SELECT id FROM restaurant WHERE name = 'Burger Mania'), (SELECT id FROM user_info WHERE username = 'user'));
