package app.task_manager.tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    @PostMapping
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagDTO tagDTO) {
        TagDTO createdTag = tagService.create(tagDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/tag/" + createdTag.getTag_id());
        headers.add("X-Tag-Creation-Timestamp", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(createdTag);
    }


    @PutMapping("{id}")
    public ResponseEntity<TagDTO> update(@PathVariable Long id, @Valid @RequestBody TagDTO tagDTO) {
        TagDTO updatedTag = tagService.update(id, tagDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Tag-Update-Timestamp", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(updatedTag);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Deleted-Tag-ID", String.valueOf(id));

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
    }


    @GetMapping
    public ResponseEntity<List<TagDTO>> findAll() {
        List<TagDTO> tags = tagService.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Tags", String.valueOf(tags.size()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> findById(@PathVariable Long id) {
        TagDTO tag = tagService.findById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Tag-ID", String.valueOf(tag.getTag_id()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(tag);
    }
}
