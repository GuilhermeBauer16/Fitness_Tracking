CREATE TABLE personalized_workout_training  (
    id CHAR(36) NOT NULL,
    workout_exercise_id CHAR(36) NOT NULL,
    repetitions INT,
    series INT,
    weight DOUBLE,
    PRIMARY KEY (id),
    CONSTRAINT fk_workout_exercise
        FOREIGN KEY (workout_exercise_id)
        REFERENCES workout_exercises(id)
        ON DELETE CASCADE
);

