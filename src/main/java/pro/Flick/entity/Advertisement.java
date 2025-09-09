package pro.Flick.entity;

import jakarta.persistence.*;

@Entity
public class Advertisement {
    @Id @GeneratedValue
    private Long id;

    private String title;

    private String uri;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    public Advertisement(String title, String uri, File file, Video video) {
        this.title = title;
        this.uri = uri;
        this.file = file;
        this.video = video;
    }
}
