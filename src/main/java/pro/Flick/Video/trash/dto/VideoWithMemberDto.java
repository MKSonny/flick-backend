package pro.Flick.Video.trash.dto;

import lombok.Data;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.member.entity.Member;
import pro.Flick.entity.Video;

@Data
public class VideoWithMemberDto {
    private Long id;
    private String title;
    private String uri;
    private GetMemberByIdResponseDto member;
    private String thumbnailUri; // 추가된 필드


    public static VideoWithMemberDto fromVideoAndMember(Video video, Member member) {
        VideoWithMemberDto dto = new VideoWithMemberDto();
        dto.id = video.getId();
        dto.title = video.getTitle();
        dto.uri = video.getUri();
        dto.member = new GetMemberByIdResponseDto(member);

//            dto.uri = "/videos-v1/" + video.getStoreFileName();

        // 썸네일 파일명이 존재할 경우, 전체 URL을 생성하여 DTO에 추가
        if (video.getThumbnailStoreFileName() != null) {
            dto.thumbnailUri = "/thumbnails/" + video.getThumbnailStoreFileName(); // 썸네일을 제공할 경로
        } else {
            dto.thumbnailUri = null; // 또는 기본 이미지 URL
        }

        return dto;
    }

    public VideoWithMemberDto() {
    }

    public VideoWithMemberDto(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.uri = video.getUri();
        this.member = new GetMemberByIdResponseDto(video.getMember());

        if (video.getThumbnailStoreFileName() != null) {
            this.thumbnailUri = "/thumbnails/" + video.getThumbnailStoreFileName(); // 썸네일을 제공할 경로
        } else {
            this.thumbnailUri = null; // 또는 기본 이미지 URL
        }
    }
}