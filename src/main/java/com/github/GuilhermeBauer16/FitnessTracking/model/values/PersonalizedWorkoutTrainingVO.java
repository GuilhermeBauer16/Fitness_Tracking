package com.github.GuilhermeBauer16.FitnessTracking.model.values;

import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;


public class PersonalizedWorkoutTrainingVO {

    private String id;

    WorkoutExerciseEntity workoutExerciseEntity;
    private Integer repetitions;
    private Integer series;
    private Double weight;

    public PersonalizedWorkoutTrainingVO(String id, WorkoutExerciseEntity workoutExerciseEntity, Integer repetitions, Integer series, Double weight) {
        this.id = id;
        this.workoutExerciseEntity = workoutExerciseEntity;
        this.repetitions = repetitions;
        this.series = series;
        this.weight = weight;
    }

    public PersonalizedWorkoutTrainingVO() {
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
