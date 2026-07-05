package com.Khata.Khata.Controller;

import com.Khata.Khata.Entity.ClassType;
import com.Khata.Khata.Service.ClassTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/class-types")
public class ClassTypeController
{
    private final ClassTypeService classTypeService;

    ClassTypeController(ClassTypeService classTypeService)
    {
        this.classTypeService = classTypeService;
    }

    @GetMapping
    public List<ClassType> list(Authentication auth)
    {
        return classTypeService.list(userId(auth));
    }

    @PostMapping
    public ResponseEntity<ClassType> create(Authentication auth, @RequestBody ClassType classType)
    {
        return ResponseEntity.ok(classTypeService.create(userId(auth), classType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassType> update(Authentication auth, @PathVariable Integer id,
                                            @RequestBody ClassType classType)
    {
        return ResponseEntity.ok(classTypeService.update(userId(auth), id, classType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Integer id)
    {
        classTypeService.delete(userId(auth), id);
        return ResponseEntity.noContent().build();
    }

    private Integer userId(Authentication auth)
    {
        return (Integer) auth.getPrincipal();
    }
}
