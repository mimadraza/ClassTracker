package com.Khata.Khata.Controller;

import com.Khata.Khata.Dto.ClassEntryRequest;
import com.Khata.Khata.Entity.ClassEntry;
import com.Khata.Khata.Service.ClassEntryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClassEntryController
{
    private final ClassEntryService classEntryService;

    ClassEntryController(ClassEntryService classEntryService)
    {
        this.classEntryService = classEntryService;
    }

    @PostMapping
    public ResponseEntity<ClassEntry> create(Authentication auth, @Valid @RequestBody ClassEntryRequest request)
    {
        return ResponseEntity.ok(classEntryService.create(userId(auth), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassEntry> update(Authentication auth, @PathVariable Integer id,
                                             @Valid @RequestBody ClassEntryRequest request)
    {
        return ResponseEntity.ok(classEntryService.update(userId(auth), id, request));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ClassEntry> setConfirmed(Authentication auth, @PathVariable Integer id,
                                                   @RequestBody Map<String, Boolean> body)
    {
        boolean confirmed = Boolean.TRUE.equals(body.get("confirmed"));
        return ResponseEntity.ok(classEntryService.setConfirmed(userId(auth), id, confirmed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Integer id)
    {
        classEntryService.delete(userId(auth), id);
        return ResponseEntity.noContent().build();
    }

    private Integer userId(Authentication auth)
    {
        return (Integer) auth.getPrincipal();
    }
}
