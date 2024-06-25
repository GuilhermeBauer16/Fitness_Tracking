CREATE TABLE workout_exercises (
    id char(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    calories_burned INT,
    exercise_type ENUM('CARDIO', 'STRENGTH', 'FLEXIBILITY', 'BALANCE') NOT NULL,
    equipment_needed VARCHAR(255),
    difficulty_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') NOT NULL,
    PRIMARY KEY (id)
);

