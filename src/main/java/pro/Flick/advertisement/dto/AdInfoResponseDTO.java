package pro.Flick.advertisement.dto;

import lombok.Data;

@Data
public class AdInfoResponseDTO {

    private Long id;
    private String title;
    private String uri;
    private String image_uri;

    public AdInfoResponseDTO(Long id, String title, String uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }

    public AdInfoResponseDTO(Long id, String title, String uri, String image_uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
        this.image_uri = image_uri;
    }
}
