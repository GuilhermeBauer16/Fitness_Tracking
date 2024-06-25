package com.github.GuilhermeBauer16.FitnessTracking.model;

import com.github.GuilhermeBauer16.FitnessTracking.enums.DifficultyLevel;
import com.github.GuilhermeBauer16.FitnessTracking.enums.ExerciseType;
import com.github.GuilhermeBauer16.FitnessTracking.enums.MuscleGroup;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "workout_exercises")
public class WorkoutExerciseEntity {

    @Id
    private String id;

    private String name;

    private String description;

    private Integer caloriesBurned;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseType exerciseType;

    private String equipmentNeeded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficultyLevel;

    @ElementCollection(targetClass = MuscleGroup.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "exercise_muscle_groups", joinColumns = @JoinColumn(name = "exercise_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group")
    private Set<MuscleGroup> muscleGroups;

    public WorkoutExerciseEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getEquipmentNeeded() {
        return equipmentNeeded;
    }

    public void setEquipmentNeeded(String equipmentNeeded) {
        this.equipmentNeeded = equipmentNeeded;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Set<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(Set<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutExerciseEntity that = (WorkoutExerciseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(caloriesBurned, that.caloriesBurned) && exerciseType == that.exerciseType && Objects.equals(equipmentNeeded, that.equipmentNeeded) && difficultyLevel == that.difficultyLevel && Objects.equals(muscleGroups, that.muscleGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, caloriesBurned, exerciseType, equipmentNeeded, difficultyLevel, muscleGroups);
    }
}
