package com.github.GuilhermeBauer16.FitnessTracking.model;

import jakarta.persistence.*;
@Entity
@Table(name = "personalized_workout_training")
public class PersonalizedWorkoutTrainingEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "workout_exercise_id")
    private WorkoutExerciseEntity workoutExerciseEntity;
    private Integer repetitions;
    private Integer series;
    private Double weight;


    public PersonalizedWorkoutTrainingEntity() {
    }

    public PersonalizedWorkoutTrainingEntity(String id, WorkoutExerciseEntity workoutExerciseEntity, Integer repetitions, Integer series, Double weight) {
        this.id = id;
        this.workoutExerciseEntity = workoutExerciseEntity;
        this.repetitions = repetitions;
        this.series = series;
        this.weight = weight;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorkoutExerciseEntity getWorkoutExerciseEntity() {
        return workoutExerciseEntity;
    }

    public void setWorkoutExerciseEntity(WorkoutExerciseEntity workoutExerciseEntity) {
        this.workoutExerciseEntity = workoutExerciseEntity;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
