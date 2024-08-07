package com.github.GuilhermeBauer16.FitnessTracking.controller;

import com.github.GuilhermeBauer16.FitnessTracking.controller.contract.PersonalizedWorkoutTrainingControllerContract;
import com.github.GuilhermeBauer16.FitnessTracking.service.PersonalizedWorkoutTrainingService;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.PersonalizedWorkoutTrainingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/personalizedWorkout")
public class PersonalizedWorkoutTrainingController implements PersonalizedWorkoutTrainingControllerContract {

    @Autowired
    private PersonalizedWorkoutTrainingService service;

    @Override
    public ResponseEntity<PersonalizedWorkoutTrainingVO> create(@RequestBody PersonalizedWorkoutTrainingVO target) {
        PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVOS = service.create(target);
        return new ResponseEntity<>(personalizedWorkoutTrainingVOS, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PersonalizedWorkoutTrainingVO> update(@RequestBody PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {

        PersonalizedWorkoutTrainingVO update = service.update(personalizedWorkoutTrainingVO);
        return ResponseEntity.ok(update);
    }

    @Override
    public ResponseEntity<PersonalizedWorkoutTrainingVO> findById(String id) {
        PersonalizedWorkoutTrainingVO byId = service.findById(id);
        return ResponseEntity.ok(byId);
    }


    @Override
    public ResponseEntity<Page<PersonalizedWorkoutTrainingVO>> findAll(@PageableDefault(sort = "repetitions", size = 20) Pageable pageable) {
        Page<PersonalizedWorkoutTrainingVO> all = service.findAll(pageable);
        return ResponseEntity.ok(all);
    }

    @Override
    public ResponseEntity<List<PersonalizedWorkoutTrainingVO>> findPersonalizedWorkoutTrainingByMuscleGroup(PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {
        List<PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOS = service.findPersonalizedWorkoutTrainingByMuscleGroup(personalizedWorkoutTrainingVO);
        return ResponseEntity.ok(personalizedWorkoutTrainingVOS);
    }

    @Override
    public ResponseEntity<List<PersonalizedWorkoutTrainingVO>> findPersonalizedWorkoutTrainingByDifficultLevel(PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {
        List<PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOS = service.findPersonalizedWorkoutTrainingByDifficultLevel(personalizedWorkoutTrainingVO);
        return ResponseEntity.ok(personalizedWorkoutTrainingVOS);
    }

    @Override
    public ResponseEntity<List<PersonalizedWorkoutTrainingVO>> findPersonalizedWorkoutTrainingByName(PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {
        List<PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOS = service.findPersonalizedWorkoutTrainingByName(personalizedWorkoutTrainingVO);
        return ResponseEntity.ok(personalizedWorkoutTrainingVOS);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
