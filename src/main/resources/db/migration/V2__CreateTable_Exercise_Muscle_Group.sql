CREATE TABLE exercise_muscle_groups (
    exercise_id char(36) NOT NULL,
    muscle_group ENUM('CHEST', 'BACK', 'SHOULDERS', 'BICEPS', 'TRICEPS', 'LEGS', 'ABS', 'CALVES', 'GLUTES') NOT NULL,
    PRIMARY KEY (exercise_id, muscle_group),
    CONSTRAINT fk_exercise
        FOREIGN KEY (exercise_id)
        REFERENCES workout_exercises(id)
        ON DELETE CASCADE
);
