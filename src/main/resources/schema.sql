-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- User Settings table
CREATE TABLE IF NOT EXISTS user_settings (
    user_settings_id BIGSERIAL PRIMARY KEY,
    goal_kcal INTEGER DEFAULT 2020,
    goal_protein FLOAT DEFAULT 170.0,
    goal_fat FLOAT DEFAULT 60.0,
    goal_carbs FLOAT DEFAULT 200.0,
    user_id BIGINT UNIQUE REFERENCES users(user_id)
);

-- Foods table
CREATE TABLE IF NOT EXISTS foods (
    food_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    producer VARCHAR(255),
    serving_size FLOAT,
    serving_units VARCHAR(50),
    kcal INTEGER,
    protein FLOAT,
    fat FLOAT,
    carbs FLOAT
);

-- Entries table
CREATE TABLE IF NOT EXISTS entries (
    entry_id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP,
    meal VARCHAR(50),
    quantity FLOAT,
    food_id BIGINT REFERENCES foods(food_id),
    user_id BIGINT REFERENCES users(user_id)
);