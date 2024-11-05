-- Roles
INSERT INTO roles (role_id, name) VALUES (default, 'ROLE_USER');
INSERT INTO roles (role_id, name) VALUES (default, 'ROLE_ADMIN');

-- Users
-- Users
INSERT INTO users (user_id, username, password) VALUES (default, 'user', '$2a$12$8LegtLQWe717tIPvZeivjuqKnaAs5.bm0Q05.5GrAmcKzXw2NjoUO');
INSERT INTO users (user_id, username, password) VALUES (default, 'admin', '$2a$12$8LegtLQWe717tIPvZeivjuqKnaAs5.bm0Q05.5GrAmcKzXw2NjoUO');

-- Roles_Users 
INSERT INTO roles_users (role_id, user_id) VALUES (1, 1);  
INSERT INTO roles_users (role_id, user_id) VALUES (2, 2);  

-- Profiles
INSERT INTO profiles (email, user_id) VALUES
('user@mail.com', 1),
('admin@mail.com', 2);

-- EVENTS
INSERT INTO events (event_id, title, description, image_url, event_datetime, max_participants, is_available, is_featured) VALUES
(default, 'Beach Volleyball Tournament', 'Join our exciting beach volleyball tournament and compete with teams from around the world.', 'https://example.com/images/volleyball.jpg', '2024-09-20 10:00:00', 16, true, true),
(default, 'Wine Tasting Evening', 'Savor a selection of exquisite wines and learn about their unique characteristics.', 'https://example.com/images/wine.jpg', '2024-10-10 19:00:00', 40, true, false),
(default, 'Photography Workshop', 'Enhance your photography skills with our professional workshop.', 'https://example.com/images/photography.jpg', '2024-09-30 14:00:00', 20, true, true),
(default, 'Cooking Class with Chef Maria', 'Learn to cook delicious dishes with renowned Chef Maria.', 'https://example.com/images/cooking.jpg', '2024-10-15 11:00:00', 15, true, false),
(default, 'Yoga Retreat', 'Relax and rejuvenate at our serene yoga retreat.', 'https://example.com/images/yoga.jpg', '2024-11-10 07:00:00', 30, true, true),
(default, 'Music Festival', 'Enjoy a weekend of live music from top bands and artists.', 'https://example.com/images/music_festival.jpg', '2024-10-20 12:00:00', 500, true, true),
(default, 'Art Exhibition Opening', 'Experience the latest art exhibition at our gallery.', 'https://example.com/images/art_exhibition.jpg', '2024-09-22 18:00:00', 50, true, false),
(default, 'Food Truck Rally', 'Taste a variety of delicious street food from our food truck rally.', 'https://example.com/images/food_truck.jpg', '2024-10-05 11:00:00', 100, true, true),
(default, 'Classic Car Show', 'Admire classic cars and meet other enthusiasts at our car show.', 'https://example.com/images/car_show.jpg', '2024-11-01 09:00:00', 75, true, false),
(default, 'Comedy Night', 'Laugh out loud with performances from top comedians.', 'https://example.com/images/comedy.jpg', '2024-09-28 20:00:00', 80, true, true),
(default, 'Charity Run', 'Participate in our charity run to support a great cause.', 'https://example.com/images/charity_run.jpg', '2024-10-25 08:00:00', 150, true, false),
(default, 'Tech Innovations Conference', 'Explore the latest advancements in technology at our conference.', 'https://example.com/images/tech_conference.jpg', '2024-11-15 09:00:00', 200, true, true),
(default, 'Farmers Market', 'Shop for fresh, local produce and artisanal goods at our farmers market.', 'https://example.com/images/farmers_market.jpg', '2024-09-18 08:00:00', 50, true, false),
(default, 'Book Fair', 'Find your next great read at our annual book fair.', 'https://example.com/images/book_fair.jpg', '2024-10-12 10:00:00', 100, true, true),
(default, 'Dance Workshop', 'Learn new dance styles and techniques in our interactive workshop.', 'https://example.com/images/dance_workshop.jpg', '2024-11-05 16:00:00', 25, true, false);